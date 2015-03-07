package com.bignerdranch.androidRunTracker;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.*;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.support.v4.app.LoaderManager.*;

/**
 * Created by ksrchen on 6/29/14.
 */
public class RunListFragment extends ListFragment implements LoaderCallbacks<Cursor> {
    private static final int REQUEST_NEW_RUN = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getLoaderManager().initLoader(0, null, this);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.run_list_options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_run:
                Intent i = new Intent(getActivity(), RunActivity.class);
                startActivityForResult(i, REQUEST_NEW_RUN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_RUN){
            getLoaderManager().restartLoader(0, null, this);

        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(getActivity(), RunActivity.class);
        i.putExtra(RunActivity.EXTRA_RUN_ID, id);
        startActivity(i);
    }




    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new RunListCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        RunCursorAdapter runCursorAdapter = new RunCursorAdapter(getActivity(),
                (RunDatabaseHelper.RunCursor) data);
        setListAdapter(runCursorAdapter);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        setListAdapter(null);
    }

    private  static class RunListCursorLoader extends  SQLiteCursorLoader{
        public RunListCursorLoader(Context context) {
            super(context);
        }

        @Override
        protected Cursor loadCursor() {
            return RunManager.get(getContext()).queryRun();
        }
    }
    private class RunCursorAdapter extends CursorAdapter {
        private RunDatabaseHelper.RunCursor mCursor;

        public  RunCursorAdapter(Context context, RunDatabaseHelper.RunCursor cursor){
            super(context, cursor, 0);
            mCursor = cursor;

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            RunDatabaseHelper.RunCursor runCursor = (RunDatabaseHelper.RunCursor) cursor;
            Run run = runCursor.getRun();
            TextView textView = (TextView)view;
            String cellText = context.getString(R.string.cell_text, run.getStartDate());
            textView.setText(cellText);

        }
    }
}

