package com.scenario2.routenavigation.task;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private static String FILE_NAME = "TransportationJson.txt";

    /**
     * Download the json file from the URL
     * and save it in the memory for later use,
     * to avoid repeated downloading overhead
     *
     * @param server_url
     * @return jsonString
     */
    public static String getData(String server_url) {
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
                    /**Save the downloaded json to a file on physical memory,
                     *so that download operation is not performed each time
                     */
                    saveJson(sb.toString());
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

    /**
     * Method to save the downloaded json String
     * as text file on the sd-card/physical memory
     * @param jsonString
     */
    private static void saveJson(String jsonString) {
        if (jsonString != null) {
            byte[] jsonBytesArray = jsonString.getBytes();
            /* if external storage is not available, then return, no need to notify user about this background work failure*/
            if (!isExternalStorageAvailable()) {
                Log.e(TAG, "External Storage not available");
                return;
            }
            File fileToSaveJson = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
            BufferedOutputStream bos;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(fileToSaveJson));
                bos.write(jsonBytesArray);
                bos.flush();
                bos.close();
                Log.d(TAG, "File written");
            } catch (FileNotFoundException e) {
                Log.e(TAG, "FileNotFoundException " + e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, "IOException " + e);
                e.printStackTrace();
            } finally {
                jsonBytesArray = null;
                System.gc();
            }
        }
    }

    /**
     * Check if external storage is available for read and write
     * @return boolean
     */
    private static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
