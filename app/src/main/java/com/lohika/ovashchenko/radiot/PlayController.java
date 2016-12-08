package com.lohika.ovashchenko.radiot;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ovashchenko on 12/8/16.
 */
public class PlayController {

    public static class RadioStationData {
        private static RadioStationData instance;
        private Map<String, RadioStation> allRadioStations = new LinkedHashMap<>();

        private static RadioStationData getInstance() {
            if (instance == null) {
                instance = new RadioStationData();
               // RadioApplication.generateStations();
            }
            return instance;
        }

        public Collection<RadioStation> getAllRadioStations() {
            return allRadioStations.values();
        }

        public void addStation(RadioStation radioStation) {
            if (allRadioStations.containsKey(radioStation.getURL())) {
                return;
            }
            allRadioStations.put(radioStation.getURL(), radioStation);
        }

        public void clearSongs() {
            for (RadioStation item : getAllRadioStations()) {
                item.clearSongs();
            }
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
