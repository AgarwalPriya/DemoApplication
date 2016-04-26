package com.scenario2.routenavigation.model;

/**
 * Class to save the latitude and longitude of the item in json
 */
public class ItemLocation {
    private String latitude;
    private String longitude;

    //Constructor
    public ItemLocation() {
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "lat: "+latitude + " & " + "long: "+longitude;
    }
}
