package com.bignerdranch.androidRunTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by ksrchen on 6/8/14.
 */
public class LocationReciever extends BroadcastReceiver {
    private static final String TAG = "LocationReciever";
    @Override
    public void onReceive(Context context, Intent intent) {
        Location location = (Location) intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if (location != null){
            onLocationRecieved(context, location);
            return;
        }

        if (intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)) {
            boolean enabled = intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
            onProviderEnabled(enabled);
        }
    }

    protected void onProviderEnabled(boolean enabled) {
        Log.i(TAG, "Provider " + (enabled? "enabled" : "disabled")) ;
    }

    protected void onLocationRecieved(Context context, Location location) {
        Log.i(TAG, this + " Got location from " +
                location.getProvider() + "," + location.getLatitude() + ":" + location.getLongitude() );
    }
}
