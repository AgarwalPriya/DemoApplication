package com.scenario2.routenavigation.task;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scenario2.routenavigation.model.TransportationDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Class to process JSON and save it as object
 */
public class JSONProcess {
    private static String TAG = "JSONProcess";
    /* LinkedHashMAp to store key value pairings, while maintaining the order */
    private HashMap<String, TransportationDetails> mTransportationMap = new LinkedHashMap<>();

    public HashMap<String, TransportationDetails> processJsonData(String jsonString) {
        if (jsonString != null && jsonString.length() > 0) {
            try {
                // Get the full HTTP Data as JSONArray as it do not have any name
                JSONArray items = new JSONArray(jsonString);
                Gson gson = new GsonBuilder().create();
                mTransportationMap.clear();
                for (int index = 0; index < items.length(); index++) {
                    JSONObject post = items.getJSONObject(index);
                    if (post != null) {
                        //Saving the data in the TransportationDetails object directly using gson
                        TransportationDetails detail = gson.fromJson(post.toString(), TransportationDetails.class);
                        Log.e(TAG,"GSON : "+detail);
                        if (detail != null)
                            mTransportationMap.put(detail.getName(), detail);
                    }
                }
                return mTransportationMap;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

