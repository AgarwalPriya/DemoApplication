package com.scenario2.routenavigation.task;

import android.os.AsyncTask;

import com.app.demoapp.Constants;
import com.scenario2.routenavigation.model.TransportationDetails;
import com.scenario2.routenavigation.utilities.StorageUtility;

import java.util.HashMap;

import static com.app.demoapp.Constants.FILE_NAME;

/**
 * Async task to download json file from the URL and
 * at the same time save it to physical memory for later use
 */
public class GetTransportationDetailsTask extends AsyncTask<String, String, HashMap<String, TransportationDetails>> {
    private GetTDetailsTaskInterface listener;

    public GetTransportationDetailsTask(GetTDetailsTaskInterface listener) {
        this.listener = listener;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(listener != null)
            listener.onTaskStarted();
    }
    @Override
    protected HashMap<String, TransportationDetails>  doInBackground(String... params) {
        /** Try to get the file from memory
         * If jsonFile is not available offline
         * go ahead with the download from the server
         */
        HashMap<String,TransportationDetails> map = null;
        String jsonString;
        jsonString = StorageUtility.getDetailsFromMemory(FILE_NAME);
        if (jsonString == null) {
            jsonString = HTTPRequestAPI.getData(Constants.DOWNLOAD_URL);
        }
        /*..........Process JSON DATA................*/
        if(jsonString != null) {
            map = new JSONProcess().processJsonData(jsonString);
        }
        return map;
    }
    @Override
    protected void onPostExecute(HashMap<String, TransportationDetails> result) {
        super.onPostExecute(result);
        if(listener != null)
            listener.onTaskCompleted(result);
    }
}
