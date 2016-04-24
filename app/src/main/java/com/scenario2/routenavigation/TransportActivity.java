package com.scenario2.routenavigation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Class which will download the JSON, save it
 * Render the UI according to the parsed JSON
 */
public class TransportActivity extends AppCompatActivity {
    private static final String TAG = "TransportActivity";
    private static final String DOWNLOAD_URL = "http://express-it.optusnet.com.au/sample.json";

    private static final String JSON_KEY_ID = "id";
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_MODE = "fromcentral";
    private static final String JSON_KEY_LOC = "location";
    private static final String JSON_KEY_CAR = "car";
    private static final String JSON_KEY_TRAIN = "train";
    private static final String JSON_KEY_LAT = "latitude";
    private static final String JSON_KEY_LONG = "longitude";

    /* LinkedHashMAp to store key value pairings, while maintaining the order */
    private HashMap<String, TransportationDetails> mTransportationMap = new LinkedHashMap<>();
    private ProgressDialog mProgressDialog = null;
    private Spinner mSpinner = null;
    private LinearLayout mCarLayout = null;
    private LinearLayout mTrainLayout = null;
    private TextView mCarData = null;
    private TextView mTrainData = null;
    private Button mNavButton = null;

    private static File file = null;
    TransportationDetails selectedItemDetail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Show an error message if file is not available on the physical memory
         * and if network is also unavailable
         */
        if (!isFileExistsOffline() && !haveNetworkConnection()) {
            AlertDialog alertDialog = new AlertDialog.Builder(TransportActivity.this).create();
            alertDialog.setTitle(getString(R.string.alert_title));
            alertDialog.setMessage(getString(R.string.alert_message));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            TransportActivity.this.finish();
                        }
                    });
            alertDialog.show();
        } else {
            setContentView(R.layout.activity_transport);
            mSpinner = (Spinner) findViewById(R.id.place_spinner);
            mCarData = (TextView) findViewById(R.id.car_detail);
            mTrainData = (TextView) findViewById(R.id.train_detail);
            mCarLayout = (LinearLayout) findViewById(R.id.car_info);
            mTrainLayout = (LinearLayout) findViewById(R.id.train_info);
            mNavButton = (Button) findViewById(R.id.nav_btn);
            // Calling async task to get json
            new GetTransportDetails().execute();
        }
    }

    private class GetTransportDetails extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(TransportActivity.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            /** Try to get the file from memory
             * If jsonFile is not available offline
             * go ahead with the download from the server
             */
            String jsonString = getDetailsFromMemory();
            if (jsonString != null && jsonString.length() != 0) {
                Log.d(TAG, "File is available offline so read from sd-card instead of downloading again");
                return jsonString;
            } else {
                jsonString = HTTPRequestAPI.getData(DOWNLOAD_URL);
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String transportationJson) {
            super.onPostExecute(transportationJson);
            mProgressDialog.dismiss();
            /*..........Process JSON DATA................*/
            processJsonData(transportationJson);
            /*..........Display the processed JSON data................*/
            renderDetails();
        }
    }

    /**
     * Get the json String from the
     * saved file, if it exists
     * @return jsonString
     */
    private static String getDetailsFromMemory() {
        StringBuilder text;
        if (isFileExistsOffline()) {
            /* Read text from file */
            text = new StringBuilder();
            try {
                if (file != null) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        text.append(line + "\n");
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Error reading the file");
            }
            //Return the text read from the file
            return text.toString();
        }
        return null;
    }

    /**
     * Check if file saved after download, exists
     * on the physical memory of the device
     * @return boolean
     */
    private static boolean isFileExistsOffline() {
        /* if media is not available for reading, return false */
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return false;
        }
        // Get the text file
        String FILE_NAME = "TransportationJson.txt";
        file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
        /* check if file exist */
        return file.exists();
    }

    /**
     * process the retrieved json string
     * save it as object for each item
     * @param jsonString, which will be parsed and saved as objects
     */
    private void processJsonData(String jsonString) {
        if (jsonString != null && jsonString.length() > 0) {
            try {
                // Get the full HTTP Data as JSONArray as it do not have any name
                JSONArray items = new JSONArray(jsonString);
                mTransportationMap.clear();
                for (int index = 0; index < items.length(); index++) {
                    JSONObject post = items.getJSONObject(index);
                    WeakReference<JSONObject> wrJsonObj = new WeakReference<>(post);
                    if (wrJsonObj.get() != null) {
                        //Saving the data in the TransportationDetails object
                        TransportationDetails detail = new TransportationDetails();
                        //set id
                        detail.setId(wrJsonObj.get().getString(JSON_KEY_ID));

                        //set name
                        if (wrJsonObj.get().has(JSON_KEY_NAME))
                            detail.setName(wrJsonObj.get().getString(JSON_KEY_NAME));

                        //set transportation mode details
                        TransportMode mode = parseTransportMode(wrJsonObj.get().getJSONObject(JSON_KEY_MODE));
                        if (mode != null)
                            detail.setTransportMode(mode);

                        //set Location details
                        ItemLocation location = parseTransportLocation(wrJsonObj.get().getJSONObject(JSON_KEY_LOC));
                        if(location != null)
                            detail.setLocation(location);

                        mTransportationMap.put(detail.getName(), detail);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param jsonLoc, is the location JSON object in an item
     * @returns the parses JSON location object
     * @throws JSONException
     */
    private ItemLocation parseTransportLocation(JSONObject jsonLoc) throws JSONException {
        WeakReference<JSONObject> wrJsonLoc = new WeakReference<>(jsonLoc);
        ItemLocation parsedLocation = null;
        if (wrJsonLoc.get() != null) {
            boolean hasLat = wrJsonLoc.get().has(JSON_KEY_LAT);
            boolean hasLong = wrJsonLoc.get().has(JSON_KEY_LONG);
            if (hasLat && hasLong) {
                parsedLocation = new ItemLocation(wrJsonLoc.get().getString(JSON_KEY_LAT),
                        wrJsonLoc.get().getString(JSON_KEY_LONG));
            } else if (!hasLat && hasLong) {
                parsedLocation = new ItemLocation("", wrJsonLoc.get().getString(JSON_KEY_LONG));
            } else if (hasLat && !hasLong) {
                parsedLocation = new ItemLocation(wrJsonLoc.get().getString(JSON_KEY_LAT), "");
            } else {
                parsedLocation = new ItemLocation("", "");
            }
        }
        return parsedLocation;
    }

    /**
     * @param jsonMode, is the Transport mode jsonObject in an item
     * @throws JSONException
     * @returns parsed JSONObject
     */

    private TransportMode parseTransportMode(JSONObject jsonMode) throws JSONException {
        WeakReference<JSONObject> wrJsonMode = new WeakReference<>(jsonMode);
        TransportMode parsedMode = null;
        if (wrJsonMode.get() != null) {
            boolean hasCarDetails = wrJsonMode.get().has(JSON_KEY_CAR);
            boolean hasTrainDetails = wrJsonMode.get().has(JSON_KEY_TRAIN);
            if (hasCarDetails && hasTrainDetails) {
                parsedMode = new TransportMode(wrJsonMode.get().getString(JSON_KEY_CAR),
                        wrJsonMode.get().getString(JSON_KEY_TRAIN));
            } else if (!hasCarDetails && hasTrainDetails) {
                parsedMode = new TransportMode("", wrJsonMode.get().getString(JSON_KEY_TRAIN));
            } else if (hasCarDetails && !hasTrainDetails) {
                parsedMode = new TransportMode(wrJsonMode.get().getString(JSON_KEY_CAR), "");
            } else {
                parsedMode = new TransportMode("", "");
            }
        }
        return parsedMode;
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
                    /** hide car, train layout and
                     * disable the navigation button if "select" is selected
                     */
                    updateUI(false, null);
                } else {
                    /* get item text which have been selected in the spinner */
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    if (selectedItem != null) {
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
     * Checks the network connection for the device
     * @return boolean
     */
    private boolean haveNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if ((activeNetwork != null)
                && ((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                || (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)))
            return true;
        else
            return false;
    }

    @Override
    protected void onDestroy() {
        //set objects to null, so that they can be cleaned up by GC whenever, it runs
        //local objects will be GCed as they scope out.
        super.onDestroy();
        selectedItemDetail = null;
        file = null;
    }
}
