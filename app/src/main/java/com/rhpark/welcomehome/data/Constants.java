package com.rhpark.welcomehome.data;

import com.google.android.gms.location.Geofence;

/**
 * Created by rhpark on 2015. 9. 1..
 */
public class Constants {

    public static final int GOOGLE_API_CLIENT_TIMEOUT_S = 10; // 10 seconds
    public static final String GOOGLE_API_CLIENT_ERROR_MSG =
            "Failed to connect to GoogleApiClient (error code = %d)";

    // Notification IDs
    public static final int MOBILE_NOTIFICATION_ID = 100;

    // Geofence constants
    public static final String GEOPENCE_REQ_ID = "req_home";
    public static final float TRIGGER_RADIUS = 100; // 100M
    public static final int TRIGGER_TRANSITION = Geofence.GEOFENCE_TRANSITION_ENTER |
            Geofence.GEOFENCE_TRANSITION_EXIT;
    public static final long EXPIRATION_DURATION = Geofence.NEVER_EXPIRE;

    // user info
    public static final int TYPE_HOME_MAP   = 1;
    public static final String HOME_IMG     = "home_img.png";

    public static final int TYPE_VOLUMN     = 2;

    public static final int TYPE_MEMO       = 3;
}
