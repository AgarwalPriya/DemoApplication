package com.scenario2.routenavigation.task;

import com.scenario2.routenavigation.model.TransportationDetails;

import java.util.HashMap;

/**
 * interface to interact between the activity and the async task.
 * UI for th activity will be updated using this interface
 */
public interface GetTDetailsTaskInterface {
    void onTaskStarted();
    void onTaskCompleted(HashMap<String, TransportationDetails> mapObject);
}
