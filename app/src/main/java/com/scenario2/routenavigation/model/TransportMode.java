package com.scenario2.routenavigation.model;

/**
 * Class to save the mode of transport available for an item in the json
 */
public class TransportMode {
    private String car;
    private String train;

    //Constructor
    public TransportMode(String carDetails, String trainDetails) {
        this.car = carDetails;
        this.train = trainDetails;
    }

    public String getCarDetails() {
        return car;
    }

    public String getTrainDetails() {
        return train;
    }

}
