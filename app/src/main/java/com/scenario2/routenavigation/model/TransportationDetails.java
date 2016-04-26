package com.scenario2.routenavigation.model;

/**
 * Class to save each item in JSON, as an object
 */
public class TransportationDetails {
    private String id;
    private String name;
    private TransportMode modeofTransport;
    private ItemLocation location;

    //Constructor
    public TransportationDetails() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public TransportMode getTransportMode() {
        return modeofTransport;
    }

    public void setTransportMode(TransportMode modeofTransport) {
        this.modeofTransport = modeofTransport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemLocation getLocation() {
        return location;
    }

    public void setLocation(ItemLocation location) {
        this.location = location;
    }
}
