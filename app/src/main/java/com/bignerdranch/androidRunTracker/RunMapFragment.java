package com.bignerdranch.androidRunTracker;

import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import org.w3c.dom.ProcessingInstruction;

import java.util.Date;

/**
 * Created by ksrchen on 8/10/14.
 */
public class RunMapFragment extends SupportMapFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARG_RUN_ID = "RUN_ID";
    private static final int LOAD_LOCATIONS = 0;

    private GoogleMap mGoogleMap;
    private RunDatabaseHelper.LocationCursor mLocationCursor;

    public static RunMapFragment newInstance(long runId){
        Bundle args = new Bundle();
        args.putLong(ARG_RUN_ID, runId);
        RunMapFragment rf = new RunMapFragment();
        rf.setArguments(args);
        return rf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            long runId = args.getLong(ARG_RUN_ID, -1);
            if (runId != -1){
                LoaderManager lm = getLoaderManager();
                lm.initLoader(LOAD_LOCATIONS, args, this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, container, savedInstanceState);
        mGoogleMap = getMap();
        mGoogleMap.setMyLocationEnabled(true);

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long runId = args.getLong(ARG_RUN_ID, - 1);
        return new LocationCursorLoader(getActivity(), runId );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLocationCursor = (RunDatabaseHelper.LocationCursor) data;
        updateUI();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLocationCursor.close();
        mLocationCursor = null;

    }

    private void updateUI(){
        if (mGoogleMap == null || mLocationCursor == null){
            return;
        }
        PolylineOptions line = new PolylineOptions();
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        mLocationCursor.moveToFirst();
        while (!mLocationCursor.isAfterLast()){
            Location location = mLocationCursor.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            Resources r = getResources();

            if (mLocationCursor.isFirst()) {
                String startDate = new Date(location.getTime()).toString();
                MarkerOptions startMarkerOptions = new MarkerOptions().position(latLng)
                        .title(r.getString(R.string.run_start))
                        .snippet(r.getString(R.string.run_started_at_format, startDate));
                mGoogleMap.addMarker(startMarkerOptions);
            }else if (mLocationCursor.isLast()){
                String Date = new Date(location.getTime()).toString();
                MarkerOptions startMarkerOptions = new MarkerOptions().position(latLng)
                        .title(r.getString(R.string.run_finished))
                        .snippet(r.getString(R.string.run_finished_at_format, Date));
                mGoogleMap.addMarker(startMarkerOptions);
            }
            line.add(latLng);
            latLngBuilder.include(latLng);
            mLocationCursor.moveToNext();
        }

        mGoogleMap.addPolyline(line);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        LatLngBounds latLngBounds = latLngBuilder.build();
        CameraUpdate movement = CameraUpdateFactory.newLatLngBounds(latLngBounds,
                display.getWidth(), display.getHeight(), 15);

        mGoogleMap.moveCamera(movement);

    }
}
