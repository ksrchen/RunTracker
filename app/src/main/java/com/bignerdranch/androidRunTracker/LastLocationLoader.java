package com.bignerdranch.androidRunTracker;

import android.content.Context;
import android.location.Location;

/**
 * Created by ksrchen on 7/6/14.
 */
public class LastLocationLoader extends DataLoader<Location> {
    private long mRunid;

    public LastLocationLoader(Context context, long runId){
        super(context);
        mRunid = runId;
    }
    @Override
    public Location loadInBackground() {
        return RunManager.get(getContext()).getLastLocationForRun(mRunid);
    }
}
