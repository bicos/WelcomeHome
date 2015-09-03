package com.rhpark.welcomehome;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.internal.ParcelableGeofence;
import com.google.android.gms.maps.model.LatLng;
import com.rhpark.welcomehome.data.Constants;
import com.rhpark.welcomehome.data.Pref;
import com.rhpark.welcomehome.data.User;
import com.rhpark.welcomehome.data.UserVolume;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;
import static com.google.android.gms.location.LocationServices.GeofencingApi;

/**
 * Created by rhpark on 2015. 9. 1..
 */
public class LocationService extends IntentService{

    private static final String TAG = "WH.LocationService";

    public static final String ACTION_GEOFENCE_TRIGGERED = "geofence_triggered";
    private static final String ACTION_ADD_GEOFENCES = "add_geofences";
    private static final String ACTION_LOCATION_UPDATED = "location_updated";
    private static final String ACTION_REQUEST_LOCATION = "request_location";

    public static final String EXTRA_GEOFENCE_DATA = "geofence_data";

    public LocationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (action != null) {
            switch (action) {
                case ACTION_ADD_GEOFENCES:
                    addGeofencesInternal(intent);
                    break;
                case ACTION_GEOFENCE_TRIGGERED:
                    geofenceTriggered(intent);
                    break;
                case ACTION_REQUEST_LOCATION:
                    requestLocationInternal();
                    break;
                case ACTION_LOCATION_UPDATED:
                    locationUpdated(intent);
                    break;
            }
        }
    }

    public static void addGeofence(Context context, ParcelableGeofence geofence){
        Intent intent = new Intent(context, LocationService.class);
        intent.setAction(LocationService.ACTION_ADD_GEOFENCES);
        intent.putExtra(EXTRA_GEOFENCE_DATA, geofence);
        context.startService(intent);
    }

    /**
     * 5초마다 업데이트
     * @param context
     */
    public static void requestLocation(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        intent.setAction(LocationService.ACTION_REQUEST_LOCATION);
        context.startService(intent);
    }

    /**
     * Add geofences using Play Services
     */
    private void addGeofencesInternal(Intent intent) {
        Log.v(TAG, ACTION_ADD_GEOFENCES);

        Geofence geofence = intent.getParcelableExtra(EXTRA_GEOFENCE_DATA);
        if (geofence == null) return;

        GeofencingRequest request = new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .build();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                Constants.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this, 0, new Intent(this, GeofenceReceiver.class), 0);
            GeofencingApi.addGeofences(googleApiClient,
                    request, pendingIntent);
            googleApiClient.disconnect();
        } else {
            Log.e(TAG, String.format(Constants.GOOGLE_API_CLIENT_ERROR_MSG,
                    connectionResult.getErrorCode()));
        }
    }

    /**
     * Called when a geofence is triggered
     */
    private void geofenceTriggered(Intent intent) {
        Log.v(TAG, ACTION_GEOFENCE_TRIGGERED);

        // Extract the geofences from the intent
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        List<Geofence> geofences = event.getTriggeringGeofences();

        if (geofences != null && geofences.size() > 0) {
            switch (event.getGeofenceTransition()) {
                case Geofence.GEOFENCE_TRANSITION_ENTER: // 집에 도착
                    showNotification(event.getGeofenceTransition());
                    startUserSetting(event.getGeofenceTransition());
                    break;

                case Geofence.GEOFENCE_TRANSITION_EXIT: // 집에서 출발
                    showNotification(event.getGeofenceTransition());
                    startUserSetting(event.getGeofenceTransition());
                    break;
            }
        }
        GeofenceReceiver.completeWakefulIntent(intent);
    }

    private void startUserSetting(int geofenceTransition) {
        User user = Pref.getUser();
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (Geofence.GEOFENCE_TRANSITION_ENTER == geofenceTransition) { // 집에 도착
            UserVolume userVolume = (UserVolume) user.getUserContent(Constants.TYPE_VOLUMN);
            if (userVolume != null) {
                int ring = userVolume.getIndoorRingVolume();
                int media = userVolume.getOutdoorMediaVolume();
                setUserVolume(audio, ring, media);
            }
        } else { // 집에서 출발
            UserVolume userVolume = (UserVolume) user.getUserContent(Constants.TYPE_VOLUMN);
            if (userVolume != null) {
                int ring = userVolume.getOutdoorRingVolume();
                int media = userVolume.getOutdoorMediaVolume();
                setUserVolume(audio, ring, media);
            }
        }
    }

    private void setUserVolume(AudioManager audio, int ring, int media) {
        audio.setStreamVolume(AudioManager.STREAM_RING, ring, 0);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, media, 0);
        if (ring == 0) {
            audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        } else {
            audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    /**
     * Called when a location update is requested
     */
    private void requestLocationInternal() {
        Log.v(TAG, ACTION_REQUEST_LOCATION);
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                Constants.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {

            Intent locationUpdatedIntent = new Intent(this, LocationService.class);
            locationUpdatedIntent.setAction(ACTION_LOCATION_UPDATED);

            // Send last known location out first if available
            Location location = FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                Intent lastLocationIntent = new Intent(locationUpdatedIntent);
                lastLocationIntent.putExtra(
                        FusedLocationProviderApi.KEY_LOCATION_CHANGED, location);
                startService(lastLocationIntent);
            }

            // Request new location
            LocationRequest mLocationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setFastestInterval(5 * 1000);
            FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest,
                    PendingIntent.getService(this, 0, locationUpdatedIntent, 0));

            googleApiClient.disconnect();
        } else {
            Log.e(TAG, String.format(Constants.GOOGLE_API_CLIENT_ERROR_MSG,
                    connectionResult.getErrorCode()));
        }
    }

    /**
     * Called when the location has been updated
     */
    private void locationUpdated(Intent intent) {
        Log.v(TAG, ACTION_LOCATION_UPDATED);

        // Extra new location
        Location location =
                intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);

        if (location != null) {
            LatLng latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());

            // Store in a local preference as well
//            Utils.storeLocation(this, latLngLocation);

            // Send a local broadcast so if an Activity is open it can respond
            // to the updated location
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    /**
     * Show the notification. Either the regular notification with wearable features
     * added to enhance, or trigger the full micro app on the wearable.
     *
     * @param action Geofence action
     */
    private void showNotification(int action) {

        String title = (action == Geofence.GEOFENCE_TRANSITION_ENTER) ?
                "집에 도착하셨습니다." :
                "집에서 출발하셨습니다.";
        String msg = DateFormat.getDateTimeInstance().format(new Date());

        // The intent to trigger when the notification is tapped
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                MainActivity.getLaunchIntent(this),
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The intent to trigger when the notification is dismissed, in this case
        // we want to clear remote notifications as well
//        PendingIntent deletePendingIntent =
//                PendingIntent.getService(this, 0, getClearRemoteNotificationsIntent(this), 0);

        // Construct the main notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle(title)
                        .bigText(msg))
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.cast_ic_notification_on)
                .setContentIntent(pendingIntent)
//                .setDeleteIntent(deletePendingIntent)
                .setCategory(Notification.CATEGORY_EVENT)
                .setAutoCancel(true);

        // Trigger the notification
        NotificationManagerCompat.from(this).notify(
                Constants.MOBILE_NOTIFICATION_ID, builder.build());
    }
}
