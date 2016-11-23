package com.lohika.ovashchenko.radiot;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

    private static void generateStations() {
        if (RadioStationData.getInstance().size() > 0) {
            return;
        }
        RadioDB db = new RadioDB(getInstance());
        RadioStation radioT = new RadioStation("RadioT", "http://feeds.rucast.net/radio-t");
        db.open();
        db.delAllSong();
        db.addStation(radioT);
        radioT.setId(db.getStationId(radioT));

        RadioStation.Song song = radioT.new Song("SonName", "", "https://pp.vk.me/c637717/v637717670/148ef/HbtJRf52u9g.jpg", new Date(System.currentTimeMillis()));
        List<RadioStation.Song> toInsert = new ArrayList<>();
        toInsert.add(song);
        db.addSong(song);
        //radioT.addSong(song);


        RadioStationData.getInstance().addStation(radioT);

        //db.close();
    }

    public static class RadioStationData {
        private static RadioStationData instance;
        private Map<String, RadioStation> allRadioStations = new LinkedHashMap<>();

        private static RadioStationData getInstance() {
            if (instance == null) {
                instance = new RadioStationData();
                RadioApplication.generateStations();
            }
            return instance;
        }

        public Collection<RadioStation> getAllRadioStations() {
            return allRadioStations.values();
        }

        public void addStation(RadioStation radioStation) {
            allRadioStations.put(radioStation.getURL(), radioStation);
        }

        public void addSongsToRadioData(List<RadioStation.Song> songs) {
            for (RadioStation.Song song : songs) {
                RadioStation station = getStationByIndex(song.getStationId());
                if (station.getId() >=0) {
                    station.addSong(song);
                }
            }
        }

        public RadioStation getStation(String url) {
            if (allRadioStations.containsKey(url)) {
                return allRadioStations.get(url);
            } else {
                return null;
            }
        }

        public RadioStation getStationByIndex(int index) {
            RadioStation station = new RadioStation();
            station.setId(-1);
            for (RadioStation item : allRadioStations.values()) {
                if (item.getId() == index) {
                    station = item;
                    break;

                }
            }
            return station;
        }

        public int size () {
            return allRadioStations.size();
        }

        private RadioStationData() {
            allRadioStations = new HashMap<>();
        }
    }

}