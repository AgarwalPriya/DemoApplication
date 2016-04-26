package com.scenario2.routenavigation.model;

/**
 * Class to save each item in JSON, as an object
 */
public class TransportationDetails {
    private String id;
    private String name;
    private TransportMode fromcentral ;
    private ItemLocation location;

    //Constructor
    public TransportationDetails() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TransportMode getTransportMode() {
        return fromcentral;
    }

    public ItemLocation getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return id + " - " + name + " - (" + fromcentral + ") "+ " - (" + location + ")";
    }
}
