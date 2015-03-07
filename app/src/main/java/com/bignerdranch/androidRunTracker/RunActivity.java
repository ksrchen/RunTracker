package com.bignerdranch.androidRunTracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class RunActivity extends SingleFragmentActivity {
    public static final String EXTRA_RUN_ID = "com.bignerdranch.android.runtracker.run_id";
    @Override
    protected Fragment CreateFragment() {
        long runId = getIntent().getLongExtra(EXTRA_RUN_ID, -1);
        if (runId != -1){
            return RunFragment.newInstance(runId);
        }
        else {
            return new RunFragment();
        }
    }
}
