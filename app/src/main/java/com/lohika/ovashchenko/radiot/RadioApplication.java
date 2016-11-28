package com.lohika.ovashchenko.radiot;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;

import com.lohika.ovashchenko.radiot.parser.RadioTParser;

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
    private boolean isPlaying = false;
    private String playingSong = "";
    public void synced() {
        isSynced = true;
    }

    public boolean isSynced () {
        return isSynced;
    }

    public static RadioApplication getInstance() {
        return instance;
    }

    public String getPlayingSong() {
        return playingSong;
    }

    /*public void playPause(String url){
        if (isPlaying) {
            pausePlaying();
            //playingSong = "";
        } else {
            playSong(url);
            playingSong = url;
        }
    }*/

    public void playSong(String url) {
        Intent playbackServiceIntent = new Intent(this, PlayService.class);
        playbackServiceIntent.putExtra(Constants.SONG_URL, url);
        playbackServiceIntent.setAction(PlayService.ACTION_PLAY);
        startService(playbackServiceIntent);

        isPlaying = true;

    }

    public void pausePlaying() {
        if (!isPlaying) {
            return;
        }
        Intent playbackServiceIntent = new Intent(this, PlayService.class);
        playbackServiceIntent.setAction(PlayService.ACTION_PAUSE);
        startService(playbackServiceIntent);
        isPlaying = false;
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
        radioT.setParser(new RadioTParser());
        db.open();
        //db.delAllSong();
        db.addStation(radioT);
        radioT.setId(db.getStationId(radioT));

        /*Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.MONTH, -1);
        RadioStation.Song song = new RadioStation.Song("SonName", "", "https://pp.vk.me/c637717/v637717670/148ef/HbtJRf52u9g.jpg", new Date(rightNow.getTimeInMillis()), radioT.getId());
        List<RadioStation.Song> toInsert = new ArrayList<>();
        toInsert.add(song);
        db.addSong(song);*/

        RadioStationData.getInstance().addStation(radioT);

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