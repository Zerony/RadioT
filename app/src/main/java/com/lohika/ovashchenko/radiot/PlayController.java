package com.lohika.ovashchenko.radiot;

import android.content.Context;
import android.content.Intent;

import com.lohika.ovashchenko.radiot.parser.RadioTParser;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ovashchenko on 12/8/16.
 */
public class PlayController {
    public static final String RADIO_T = "RadioT";
    private static RadioStationData radioStationData;
    private boolean isSynced = false;
    private boolean isPlaying = false;
    private String playURL = "";
    private RadioDB db;
    private Context context;

    PlayController (RadioDB db, Context context) {
        this.db = db;
        this.context = context;
    }


    public String getPlayURL() {
        return playURL;
    }

    public void synced() {
        isSynced = true;
    }

    public boolean isSynced () {
        return isSynced;
    }

    public void playSong(RadioStation.Song song) {
        Intent playbackServiceIntent = new Intent(context, PlayService.class);
        playbackServiceIntent.putExtra(Constants.SERVICE_ACTION, PlayService.ACTION_PLAY);
        playbackServiceIntent.putExtra(Constants.SONG_URL, song.getLinkToSong());
        playbackServiceIntent.putExtra(Constants.SONG_NAME, song.getName());

        context.startService(playbackServiceIntent);
        playURL = song.getLinkToSong();
        isPlaying = true;

    }

    public void pausePlaying() {
        if (!isPlaying) {
            return;
        }
        Intent playbackServiceIntent = new Intent(context, PlayService.class);
        playbackServiceIntent.putExtra(Constants.SERVICE_ACTION, PlayService.ACTION_PAUSE);
        context.startService(playbackServiceIntent);
        playURL = "";
        isPlaying = false;
    }



    private void generateStations() {
        if (getStationData().size() > 0) {
            return;
        }
        //RadioDB db = new RadioDB(context);
        RadioStation radioT = new RadioStation(RADIO_T, BuildConfig.RU_CAST_SERVER_URL);
        radioT.setParser(new RadioTParser());
        db.open();
        //db.delAllSong();
        db.addStation(radioT);
        radioT.setId(db.getStationId(radioT));

        getStationData().addStation(radioT);

    }

    public RadioStationData getStationData() {
        if (radioStationData == null) {
            radioStationData = new RadioStationData();
            PlayController.this.generateStations();
        }
        return radioStationData;
    }

    public class RadioStationData {
        private Map<String, RadioStation> allRadioStations = new LinkedHashMap<>();

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
