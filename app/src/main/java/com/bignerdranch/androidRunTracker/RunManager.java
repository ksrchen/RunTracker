package com.bignerdranch.androidRunTracker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by ksrchen on 6/8/14.
 */
public class RunManager {
    private static final String TAG = "RunManager";
    private static final String PREF_FILE    = "runs";
    private static final String PREF_CURRENT_RUN_ID  = "RunManager.currentRunId";

    public static final String ACTION_LOCATION = "com.bignerdranch.android.runtracker.ACTION_LOCATION";

    private static RunManager sRunManager;
    private Context mAppContext;
    private LocationManager sLocationManager;

    private RunDatabaseHelper mHelper;
    private SharedPreferences mPrefs;
    private long mCurrentRunId;

    private RunManager(Context appContext){
        mAppContext = appContext;
        sLocationManager =  (LocationManager)mAppContext.getSystemService(Context.LOCATION_SERVICE);

        mHelper = new RunDatabaseHelper(mAppContext);
        mPrefs = mAppContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        mCurrentRunId = mPrefs.getLong(PREF_CURRENT_RUN_ID, -1);


    }

    public static RunManager get(Context c) {
        if (sRunManager == null){
            sRunManager = new RunManager(c.getApplicationContext());
        }

        return sRunManager;
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate){
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);

    }
    public void startLocationUpdates() {
        String provider = LocationManager.GPS_PROVIDER;
        Location location = sLocationManager.getLastKnownLocation(provider);
        if (location != null){
            location.setTime(System.currentTimeMillis());
            broadcastLocation(location);
        }
        PendingIntent pi = getLocationPendingIntent(true);
        sLocationManager.requestLocationUpdates(provider, 0, 0, pi);
    }

    private void broadcastLocation(Location location) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
        mAppContext.sendBroadcast(broadcast);
    }

    public void stopLocationUpdates() {
         PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null){
            sLocationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    public boolean isTrackingRun() {
        return getLocationPendingIntent(false) != null;
    }
    public boolean isTrackingRun(Run run) {
        return run != null && run.getId() == mCurrentRunId;
    }


    public Run startNewRun() {
        Run run = insertRun();
        startTrackingRun(run);
        return run;
    }

    public void stopRun() {
        stopLocationUpdates();
        mCurrentRunId = -1;
        mPrefs.edit().remove(PREF_CURRENT_RUN_ID).commit();

    }
    public void startTrackingRun(Run run) {
        mCurrentRunId = run.getId();
        mPrefs.edit().putLong(PREF_CURRENT_RUN_ID, mCurrentRunId).commit();
        startLocationUpdates();
    }
    private Run insertRun(){
        Run run = new Run();
        run.setId(mHelper.insertRun(run));
        return run;
    }

    public void insertLocation(Location location){
        if (mCurrentRunId != -1){
            mHelper.insertLocation(mCurrentRunId, location);
        } else {
            Log.e(TAG, "location received with no tracking run; ignoring.");
        }
    }
    public RunDatabaseHelper.RunCursor queryRun(){
        return mHelper.queryRuns();
    }

    public  Run getRun(long id){
        Run run = null;
        RunDatabaseHelper.RunCursor cursor = mHelper.queryRuns(id);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            run = cursor.getRun();
        }
        cursor.close();
        return run;
    }

    public  Location getLastLocationForRun(long id){
        Location location = null;
        RunDatabaseHelper.LocationCursor cursor = mHelper.queryLastLocation(id);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            location = cursor.getLocation();
        }
        cursor.close();
        return location;
    }
    public RunDatabaseHelper.LocationCursor queryLocationForRun(long runId){
        return mHelper.queryLocationForRun(runId);
    }
}


