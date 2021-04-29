package com.exemple.healtyapplication.ui.detected;

import com.google.android.gms.location.DetectedActivity;

public class Constant {
    private Constant() {}

    private static final String PACKAGE_NAME = "com.google.android.gms.location.activityrecognition";

    static final String KEY_ACTIVITY_UPDATES_REQUESTED = PACKAGE_NAME + ".ACTIVITY_UPDATES_REQUESTED";

    static final String KEY_DETECTED_ACTIVITIES = PACKAGE_NAME + ".DETECTED_ACTIVITIES";


    static final long DETECTION_INTERVAL_IN_MILLISECONDS = 30 * 1000; // 30 seconds

    public static final int[] MONITORED_ACTIVITIES = {
            DetectedActivity.STILL,
            DetectedActivity.ON_FOOT,
            DetectedActivity.WALKING,
            DetectedActivity.RUNNING,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.TILTING,
            DetectedActivity.UNKNOWN
    };
}
