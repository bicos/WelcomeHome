package com.rhpark.welcomehome;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by rhpark on 2015. 9. 1..
 */
public class GeofenceReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent.setClass(context, LocationService.class);
        intent.setAction(LocationService.ACTION_GEOFENCE_TRIGGERED);
        startWakefulService(context, intent);
    }
}
