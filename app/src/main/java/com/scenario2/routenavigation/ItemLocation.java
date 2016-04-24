package com.scenario2.routenavigation;

/**
 * Class to save the latitude and longitude of the item in json
 */
public class ItemLocation {
    private String latitude;
    private String longitude;

    //Constructor
    public ItemLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
