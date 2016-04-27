package com.scenario2.routenavigation.task;

import android.util.Log;

import com.app.demoapp.Constants;
import com.scenario2.routenavigation.caching.JsonLruCache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class to make HTTP request and download the json file form the server
 * The downloaded file is saved on the sd-card of the device for further use
 */

public class HTTPRequestAPI {
    private static String TAG = "HTTPRequestAPI";
    private static int TIMEOUT = 30000;

    /**
     * Download the json file from the URL
     * and save it in the memory for later use,
     * to avoid repeated downloading overhead
     * @param server_url
     * @return jsonString
     */
    public static String getData(String server_url) {
        Log.e(TAG,"JSON not available in the cache, download from the URL");
        /** TO-DO
         * Need to re-load the json using e-tag ,
         * so that the json used is updated periodically
         */
        HttpURLConnection connection = null;
        try {
            URL url = new URL(server_url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-length", "0");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.connect();
            int status = connection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    //get the file from the URL
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    // End reading
                    /**Save the downloaded json to the LRU cache,
                     *so that download operation is not performed each time
                     */
                    JsonLruCache.getInstance().addJsonToMemoryCache(Constants.CACHE_KEY,sb.toString());
                    return sb.toString();
                default:
                    Log.e(TAG, "Error retrieving JSON");
            }

        } catch (MalformedURLException ex) {
            Log.e(TAG, "exception " + ex);
        } catch (IOException ex) {
            Log.e(TAG, "exception " + ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Log.e(TAG, "exception " + ex);
                }
            }
        }
        return null;
    }
}
