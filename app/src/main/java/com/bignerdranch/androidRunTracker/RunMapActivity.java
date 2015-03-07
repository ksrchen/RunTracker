package com.bignerdranch.androidRunTracker;

import android.support.v4.app.Fragment;

/**
 * Created by ksrchen on 8/10/14.
 */
public class RunMapActivity extends SingleFragmentActivity {
    public static final String EXTRA_RUN_ID = "com.bignerdranch.android.runTracker.run_id";

    @Override
    protected Fragment CreateFragment() {
        long runId = getIntent().getLongExtra(EXTRA_RUN_ID, -1);
        if (runId != -1){
            return RunMapFragment.newInstance(runId);
        } else{
            return new RunMapFragment();
        }
    }
}
