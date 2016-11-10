package com.lohika.ovashchenko.radiot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ovashchenko on 11/8/16.
 */
public class RadioStationSingleton { // TODO: move to application
    private static RadioStationSingleton ourInstance = new RadioStationSingleton();

    private  Map<String, RadioStation> allRadioStations = new HashMap<>();
    private List<RadioStation> allRadioStationsList = new ArrayList<>();

    private List<Integer> dataWithImages;

    public static RadioStationSingleton getInstance() {
        return ourInstance;
    }

    public  List<RadioStation> getAllRadioStations() {
        return allRadioStationsList;
    }

    public void addStation(RadioStation radioStation) {
        allRadioStations.put(radioStation.getId(), radioStation);
        allRadioStationsList.add(radioStation);
    }

    public RadioStation getStation(String index) {
        if (allRadioStations.containsKey(index)) {
            return allRadioStations.get(index);
        }
        return null;
    }

    public RadioStation getStation(int index) {
        if (index >= allRadioStations.size()) {
            return null;
        }
        return allRadioStationsList.get(index);
    }

    public int size () {
        return allRadioStationsList.size();
    }

    public int getImageByNumber(int number) {
        return dataWithImages.get(number);
    }

    public int getImagesSize() {
        return dataWithImages.size();
    }

    private RadioStationSingleton() {
        allRadioStations = new HashMap<>();
        allRadioStationsList = new ArrayList<>();

        dataWithImages = new ArrayList<>();
        dataWithImages.add(R.drawable.rock);
        dataWithImages.add(R.drawable.rock2);
        dataWithImages.add(R.drawable.rock3);
        dataWithImages.add(R.drawable.rock4);
        dataWithImages.add(R.drawable.rock5);
        dataWithImages.add(R.drawable.rock6);
    }
}
