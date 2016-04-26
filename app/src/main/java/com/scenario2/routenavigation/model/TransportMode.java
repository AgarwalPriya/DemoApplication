package com.scenario2.routenavigation.model;

/**
 * Class to save the mode of transport available for an item in the json
 */
public class TransportMode {
    private String car;
    private String train;

    //Constructor
    public TransportMode() {
    }

    public String getCarDetails() {
        return car;
    }

    public String getTrainDetails() {
        return train;
    }

    @Override
    public String toString() {
        return "car: "+car + " & " +"train: "+ train;
    }

}
