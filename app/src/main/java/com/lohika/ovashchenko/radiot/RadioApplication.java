package com.lohika.ovashchenko.radiot;

import android.app.Application;
import android.content.res.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ovashchenko on 11/8/16.
 */

public class RadioApplication extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
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

    public void generateStations(int number) {
        if (RadioStationData.getInstance().size() > 0) {
            return;
        }
        for(int i=0; i<number; i++) {
            RadioStation radio = new RadioStation(i);
            for(int j=0; j<5; j++) {
                int rand = Utils.generateRand(RadioStationData.getInstance().getImagesSize());
                int image =  RadioStationData.getInstance().getImageByNumber(rand);
                RadioStation.Song song = radio.new Song(radio.getName() + j, radio.getName() + "| SongName "+ j, "Link " + j, image);
                radio.getSongs().add(song);
            }
            RadioStationData.getInstance().addStation(radio);
        }
    }

    public static class RadioStationData {
        private static RadioStationData instance = new RadioStationData();
        private Map<String, RadioStation> allRadioStations = new HashMap<>();
        private List<RadioStation> allRadioStationsList = new ArrayList<>();

        private List<Integer> dataWithImages;

        private static RadioStationData getInstance() {
            return instance;
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

        private RadioStationData() {
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

}