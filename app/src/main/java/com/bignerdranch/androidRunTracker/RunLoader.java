package com.bignerdranch.androidRunTracker;

import android.content.Context;

/**
 * Created by ksrchen on 7/6/14.
 */
public class RunLoader extends DataLoader<Run> {
    private  long mRunId;

    public  RunLoader(Context context, long runId){
        super(context);
        mRunId = runId;
    }
    @Override
    public Run loadInBackground() {
        return RunManager.get(getContext()).getRun(mRunId);
    }
}
