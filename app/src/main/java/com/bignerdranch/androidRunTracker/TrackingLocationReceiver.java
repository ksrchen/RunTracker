package com.bignerdranch.androidRunTracker;

import android.content.Context;
import android.location.Location;
import android.util.Log;

/**
 * Created by ksrchen on 6/22/14.
 */
public class TrackingLocationReceiver extends LocationReciever {
    private static final String TAG = "TrackingLocationReceiver";

    @Override
    protected void onLocationRecieved(Context context, Location location) {
        Log.i(TAG, "Received location.");
        RunManager.get(context).insertLocation(location);
    }
}
