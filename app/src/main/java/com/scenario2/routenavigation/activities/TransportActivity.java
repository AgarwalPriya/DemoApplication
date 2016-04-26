package com.scenario2.routenavigation.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.demoapp.R;
import com.scenario2.routenavigation.model.TransportationDetails;
import com.scenario2.routenavigation.task.GetTDetailsTaskInterface;
import com.scenario2.routenavigation.task.GetTransportationDetailsTask;
import com.scenario2.routenavigation.utilities.NetworkUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Class which will download the JSON, save it
 * Render the UI according to the parsed JSON
 */
public class TransportActivity extends AppCompatActivity implements GetTDetailsTaskInterface {
    private static final String TAG = "TransportActivity";

    private ProgressDialog mProgressDialog = null;
    private Spinner mSpinner = null;
    private LinearLayout mCarLayout = null;
    private LinearLayout mTrainLayout = null;
    private TextView mCarData = null;
    private TextView mTrainData = null;
    private Button mNavButton = null;

    /* LinkedHashMAp to store key value pairings, while maintaining the order */
    private HashMap<String, TransportationDetails> mTransportationMap = new LinkedHashMap<>();
    TransportationDetails selectedItemDetail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Show an error message if file is not available on the physical memory
         * and if network is also unavailable
         */
        if (!NetworkUtility.haveNetworkConnection(getApplicationContext())) {
            showErrorDialog(getString(R.string.alert_title), getString(R.string.alert_message));
        } else {
            Log.e(TAG,"<<<<<<< onCreate >>>>>>");
            setContentView(R.layout.activity_transport);
            initUIComponents();
            // Calling async task to get json
            new GetTransportationDetailsTask(this).execute();
        }
    }

    /**
     * method to initialize the UI components of the activity
     */
    private void initUIComponents() {
        mSpinner = (Spinner) findViewById(R.id.place_spinner);
        mCarData = (TextView) findViewById(R.id.car_detail);
        mTrainData = (TextView) findViewById(R.id.train_detail);
        mCarLayout = (LinearLayout) findViewById(R.id.car_info);
        mTrainLayout = (LinearLayout) findViewById(R.id.train_info);
        mNavButton = (Button) findViewById(R.id.nav_btn);
    }

    /**
     * Populate the spinner and other view components
     * depending on what item have been selected in the
     * spinner
     */
    private void renderDetails() {
        setSpinner();
        /* on item selected listener for the spinner */
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    updateUI(false, null);
                } else {
                    /* get item text which have been selected in the spinner */
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    if (selectedItem != null && mTransportationMap != null) {
                        /** Get the transportation details object from the map
                         *  for the selected item, which matches with the key in the map
                         */
                        selectedItemDetail = mTransportationMap.get(selectedItem);
                        updateUI(true, selectedItemDetail);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateUI(boolean show, TransportationDetails slectedDetails) {
        if (!show) {
            /** hide car, train layout and
             * disable the navigation button if "select" is selected
             */
            if (mCarLayout != null)
                mCarLayout.setVisibility(View.GONE);
            if (mTrainLayout != null)
                mTrainLayout.setVisibility(View.GONE);
            if (mNavButton != null)
                mNavButton.setEnabled(false);
        } else {
            if (slectedDetails != null) {
            /* enable the button if a valid item is selected */
                if (mNavButton != null)
                    mNavButton.setEnabled(true);
                /** if no car detail is found, hide the car details layout from the UI
                 * else show the details
                 */
                if (mCarLayout != null) {
                    if (slectedDetails.getTransportMode().getCarDetails().equals("")) {
                        mCarLayout.setVisibility(View.GONE);
                    } else {
                        mCarLayout.setVisibility(View.VISIBLE);
                        if (mCarData != null)
                            mCarData.setText(slectedDetails.getTransportMode().getCarDetails());
                    }
                }
                /** if no train detail is found, hide the train details layout from the UI
                 * else show the details
                 */
                if (mTrainLayout != null) {
                    if (slectedDetails.getTransportMode().getTrainDetails().equals("")) {
                        mTrainLayout.setVisibility(View.GONE);
                    } else {
                        mTrainLayout.setVisibility(View.VISIBLE);
                        if (mTrainData != null)
                            mTrainData.setText(slectedDetails.getTransportMode().getTrainDetails());
                    }
                }
            }
        }
    }
    /**
     * populate the items in the spinner
     * by setting the respective adapter for it
     */
    private void setSpinner() {
        ArrayList<String> list = new ArrayList();
        ArrayAdapter<String> dataAdapter;
        /* Add first item as "Select" in the spinner */
        list.add(getString(R.string.select));
        for (String name : mTransportationMap.keySet()) {
            /** Add all the keys in the list from the transportationMap object
             * i.e., name of the places
             */
            list.add(name);
        }
        dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        /* set adapter for the spinner */
        mSpinner.setAdapter(dataAdapter);
    }

    /**
     * on click listener for navigation button
     * @param view is the navigation button
     */
    public void navOnClick(View view) {
        if (selectedItemDetail == null) {
            /* if no item is selected from the spinner, disable the button */
            view.setEnabled(false);
        } else {
            /**
             *  Launch the Map intent, for the selected location.
             *  with latitude and longitude set to that of the selected item's location object
             */
            view.setEnabled(true);
            if (selectedItemDetail.getLocation().getLatitude().equals("") ||
                    selectedItemDetail.getLocation().getLongitude().equals("")) {
                Log.e(TAG, "Latitude/Longitude not available for the selected item");
                return;
            }
            double latitude = Double.parseDouble(selectedItemDetail.getLocation().getLatitude());
            double longitude = Double.parseDouble(selectedItemDetail.getLocation().getLongitude());
            String uriBegin = "geo:" + latitude + "," + longitude;
            String query = latitude + "," + longitude;
            String encodedQuery = Uri.encode(query);
            String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
            Uri uri = Uri.parse(uriString);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    /**
     * Method to display error dialog to the end-user,
     * in case of any error
     * @param title for error dialog
     * @param msg for error dialog
     */
    private void showErrorDialog(String title, String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(TransportActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        TransportActivity.this.finish();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onTaskStarted() {
        mProgressDialog = new ProgressDialog(TransportActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.show();
    }

    @Override
    public void onTaskCompleted(HashMap<String, TransportationDetails> mapObject) {
        mProgressDialog.dismiss();
        /*..........if json string is null show error dialog to the user................*/
        if (mapObject == null || (mapObject.size() == 0)) {
            showErrorDialog(getString(R.string.async_error_title), getString(R.string.async_error_msg));
        }
        /*..........Display the processed JSON data................*/
        mTransportationMap = mapObject;
        renderDetails();
    }

    @Override
    protected void onDestroy() {
        //set objects to null, so that they can be cleaned up by GC whenever, it runs
        //local objects will be GCed as they scope out.
        super.onDestroy();
        selectedItemDetail = null;
        if (mTransportationMap != null) {
            mTransportationMap.clear();
            mTransportationMap = null;
        }
    }
}
