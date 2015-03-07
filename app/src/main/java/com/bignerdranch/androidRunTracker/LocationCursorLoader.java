package com.bignerdranch.androidRunTracker;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by ksrchen on 8/17/14.
 */
public class LocationCursorLoader extends SQLiteCursorLoader {
    private long mRunId;

    public LocationCursorLoader(Context c,  long runId){
        super(c);
        mRunId = runId;
    }
    @Override
    protected Cursor loadCursor() {
        return RunManager.get(getContext()).queryLocationForRun(mRunId);
    }
}
