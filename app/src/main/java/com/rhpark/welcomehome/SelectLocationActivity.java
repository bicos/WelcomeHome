package com.rhpark.welcomehome;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.internal.ParcelableGeofence;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rhpark.welcomehome.data.Constants;
import com.rhpark.welcomehome.data.Pref;
import com.rhpark.welcomehome.data.User;
import com.rhpark.welcomehome.data.UserHomeMap;
import com.rhpark.welcomehome.data.UserVolume;


public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_LAST_LOCATION = "last_location";

    private Location lastLocation;

    private Marker homeMarker;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        if (getIntent() != null) {
            lastLocation = getIntent().getParcelableExtra(EXTRA_LAST_LOCATION);
        }

        MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (lastLocation != null) {
            map = googleMap;
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(final LatLng latLng) {
                    showSaveHomeLocationAlertDialog(latLng);
                }
            });
            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                }

                @Override
                public void onMarkerDragEnd(final Marker marker) {
                    showSaveHomeLocationAlertDialog(marker.getPosition());
                }
            });

            LatLng lastLatLng = new LatLng(
                    lastLocation.getLatitude(),
                    lastLocation.getLongitude());

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(lastLatLng, 15f);
            googleMap.moveCamera(update);

            homeMarker = googleMap.addMarker(new MarkerOptions()
                    .position(lastLatLng)
                    .title("My Home is Here!!")
                    .draggable(true)
                    .visible(true));
        }
    }

    private void showSaveHomeLocationAlertDialog(final LatLng latLng) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(SelectLocationActivity.this);
        builder.setTitle("알림");
        builder.setMessage("현재 선택된 위치를 집으로 저장하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                homeMarker.setPosition(latLng);
            }
        });
        builder.setNegativeButton("취소", null);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        LatLng lastLatLng = homeMarker.getPosition();

        Geofence geofence = new Geofence.Builder()
                .setCircularRegion(lastLatLng.latitude, lastLatLng.longitude, Constants.TRIGGER_RADIUS)
                .setRequestId(Constants.GEOPENCE_REQ_ID)
                .setTransitionTypes(Constants.TRIGGER_TRANSITION)
                .setExpirationDuration(Constants.EXPIRATION_DURATION)
                .build();

        // 지오펜스 등록
        LocationService.addGeofence(getApplicationContext(), (ParcelableGeofence) geofence);

        // 유저 정보 생성
        createUserInfo();

        setResult(RESULT_OK);
        finish();
    }

    private void createUserInfo() {
        User user = new User();
        createUserHomeMap(user);
    }

    private void createUserHomeMap(final User user) {
        if (map != null) {
            map.snapshot(new GoogleMap.SnapshotReadyCallback() {
                @Override
                public void onSnapshotReady(Bitmap bitmap) {
                    String path = Utils.saveHomeImg(SelectLocationActivity.this, bitmap);
                    if (TextUtils.isEmpty(path) == false) {
                        UserHomeMap homeMap = new UserHomeMap(path, "저의 집입니다.", homeMarker.getPosition());
                        user.addContent(homeMap);
                        createUserVolume(user);
                    }
                }
            });
        }
    }

    private void createUserVolume(final User user) {
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int mediaVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        int ringVolume = audio.getStreamVolume(AudioManager.STREAM_RING);

        UserVolume userVolume = new UserVolume(mediaVolume, ringVolume, 0, 0);
        user.addContent(userVolume);

        Pref.setUser(user);

        Toast.makeText(getApplicationContext(),
                "마지막으로 선택된 위치가 집으로 저장되었습니다.",
                Toast.LENGTH_SHORT).show();
    }

    public static void startSelectLocationActivity(Activity activity, Location location, int reqCode) {
        Intent intent = new Intent(activity, SelectLocationActivity.class);
        intent.putExtra(EXTRA_LAST_LOCATION, location);
        activity.startActivityForResult(intent, reqCode);
    }
}
