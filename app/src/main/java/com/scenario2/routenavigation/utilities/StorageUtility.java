package com.scenario2.routenavigation.utilities;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to check Storage availability, etc
 */
public class StorageUtility {
    private static String TAG = "StorageUtility";
     /**
     * Check if file saved after download, exists
     * on the physical memory of the device
     * @return true if file exists, false otherwise
     */
    public static boolean  isFileExistsOffline(String fileName) {
        /* if media is not available for reading, return false */
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return false;
        }
        // Get the text file
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        /* check if file exist */
        return file.exists();
    }

    /**
     * Get the json String from the
     * saved file, if it exists
     * @return jsonString
     */
    public static String getDetailsFromMemory(String fileName) {
        StringBuilder text;
        if (isFileExistsOffline(fileName)) {
            /* Read text from file */
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
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
}
