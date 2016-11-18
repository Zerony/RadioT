package com.lohika.ovashchenko.radiot;

import android.app.Application;
import android.content.res.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ovashchenko on 11/8/16.
 */

public class RadioApplication extends Application {
    private static RadioApplication instance;
    private boolean isSynced = false;

    public void synced() {
        isSynced = true;
    }

    public boolean isSynced () {
        return isSynced;
    }

    public static RadioApplication getInstance() {
        return instance;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public RadioStationData getRadioStationData() {
        return RadioStationData.getInstance();
    }

    private static void generateStations(int number) {
        if (RadioStationData.getInstance().size() > 0) {
            return;
        }
        RadioStation radio = new RadioStation("RadioT", "http://feeds.rucast.net/radio-t");
        RadioStationData.getInstance().addStation(radio);
    }

    public static class RadioStationData {
        private static RadioStationData instance;// = new RadioStationData();
        private Map<String, RadioStation> allRadioStations = new LinkedHashMap<>();
        private List<Integer> dataWithImages;

        private static RadioStationData getInstance() {
            if (instance == null) {
                instance = new RadioStationData();
                RadioApplication.generateStations(1);
            }
            return instance;
        }

        public Collection<RadioStation> getAllRadioStations() {
            return allRadioStations.values();
        }

        public void addStation(RadioStation radioStation) {
            allRadioStations.put(radioStation.getURL(), radioStation);
        }

        public RadioStation getStation(String index) {
            if (allRadioStations.containsKey(index)) {
                return allRadioStations.get(index);
            }
            return null;
        }

        public int size () {
            return allRadioStations.size();
        }

        public int getImageByNumber(int number) {
            return dataWithImages.get(number);
        }
        public int getImagesSize() {
            return dataWithImages.size();
        }

        private RadioStationData() {
            allRadioStations = new HashMap<>();

            dataWithImages = new ArrayList<>();
            dataWithImages.add(R.drawable.rock);
            dataWithImages.add(R.drawable.rock2);
            dataWithImages.add(R.drawable.rock3);
            dataWithImages.add(R.drawable.rock4);
            dataWithImages.add(R.drawable.rock5);
            dataWithImages.add(R.drawable.rock6);
        }
    }

}