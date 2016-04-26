package com.scenario2.routenavigation.task;

import com.app.demoapp.Constants;
import com.scenario2.routenavigation.model.ItemLocation;
import com.scenario2.routenavigation.model.TransportMode;
import com.scenario2.routenavigation.model.TransportationDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Class to process JSON and save it as object
 */
public class JSONProcess {
    /* LinkedHashMAp to store key value pairings, while maintaining the order */
    private HashMap<String, TransportationDetails> mTransportationMap = new LinkedHashMap<>();

    public HashMap<String, TransportationDetails> processJsonData(String jsonString) {
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
                        detail.setId(wrJsonObj.get().getString(Constants.JSON_KEY_ID));

                        //set name
                        if (wrJsonObj.get().has(Constants.JSON_KEY_NAME))
                            detail.setName(wrJsonObj.get().getString(Constants.JSON_KEY_NAME));

                        //set transportation mode details
                        TransportMode mode = parseTransportMode(wrJsonObj.get().getJSONObject(Constants.JSON_KEY_MODE));
                        if (mode != null)
                            detail.setTransportMode(mode);

                        //set Location details
                        ItemLocation location = parseTransportLocation(wrJsonObj.get().getJSONObject(Constants.JSON_KEY_LOC));
                        if(location != null)
                            detail.setLocation(location);

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
            boolean hasLat = wrJsonLoc.get().has(Constants.JSON_KEY_LAT);
            boolean hasLong = wrJsonLoc.get().has(Constants.JSON_KEY_LONG);
            if (hasLat && hasLong) {
                parsedLocation = new ItemLocation(wrJsonLoc.get().getString(Constants.JSON_KEY_LAT),
                        wrJsonLoc.get().getString(Constants.JSON_KEY_LONG));
            } else if (!hasLat && hasLong) {
                parsedLocation = new ItemLocation("", wrJsonLoc.get().getString(Constants.JSON_KEY_LONG));
            } else if (hasLat && !hasLong) {
                parsedLocation = new ItemLocation(wrJsonLoc.get().getString(Constants.JSON_KEY_LAT), "");
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
            boolean hasCarDetails = wrJsonMode.get().has(Constants.JSON_KEY_CAR);
            boolean hasTrainDetails = wrJsonMode.get().has(Constants.JSON_KEY_TRAIN);
            if (hasCarDetails && hasTrainDetails) {
                parsedMode = new TransportMode(wrJsonMode.get().getString(Constants.JSON_KEY_CAR),
                        wrJsonMode.get().getString(Constants.JSON_KEY_TRAIN));
            } else if (!hasCarDetails && hasTrainDetails) {
                parsedMode = new TransportMode("", wrJsonMode.get().getString(Constants.JSON_KEY_TRAIN));
            } else if (hasCarDetails && !hasTrainDetails) {
                parsedMode = new TransportMode(wrJsonMode.get().getString(Constants.JSON_KEY_CAR), "");
            } else {
                parsedMode = new TransportMode("", "");
            }
        }
        return parsedMode;
    }

}

