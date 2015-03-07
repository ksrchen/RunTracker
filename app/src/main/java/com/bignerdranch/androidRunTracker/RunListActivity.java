package com.bignerdranch.androidRunTracker;

import android.support.v4.app.Fragment;

/**
 * Created by ksrchen on 6/29/14.
 */
public class RunListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment CreateFragment() {
        return new RunListFragment();
    }
}
