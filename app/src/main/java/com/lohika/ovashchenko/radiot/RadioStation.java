package com.lohika.ovashchenko.radiot;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.lohika.ovashchenko.radiot.parser.RadioXMLParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ovashchenko on 11/7/16.
 */
public class RadioStation implements Serializable{
    private String name;
    private final String URL;
    private int id;
    private LinkedHashSet<Song> songs;
    private Song lastSyncedSong;
    private RadioXMLParser parser;

    // <editor-fold desc="Getters And Setters">  
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getURL() {
        return URL;
    }

    public int getId() {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public long getLastSongTime() {
        if (this.lastSyncedSong == null) {
            return 0L;
        }
        return lastSyncedSong.getPubDate().getTime();
    }

    public Set<Song> getSongs() {
        return this.songs;
    }

    public Song getSong(int index) {
        if (this.songs.size() > index) {
            int i = 0;
            Song result = new Song();
            for (Song item : this.songs) {
                if (i == index) {
                    result = item;
                    break;
                }
                i++;
            }
            return result;
        } else {
            return null;
        }
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }

    public void clearSongs() {
        this.songs.removeAll(this.songs);
    }

    public void setParser(RadioXMLParser parser) {
        this.parser = parser;
    }
    // </editor-fold>  

    public List<Song> parseXML(String text) {
        return this.parser.parseXML(text, this);
    }

    public void fillLastSyncedSong() {
        if (this.songs.size() == 0) {
            return;
        }
        lastSyncedSong = new Song();
        lastSyncedSong.setPubDate(new Date(0L));
        for (Song item : this.songs) {
            if (item.getPubDate().getTime() > lastSyncedSong.getPubDate().getTime()) {
                lastSyncedSong = item;
            }
        }
    }

    // <editor-fold desc="Constructors">  
    public RadioStation() {
        this("NoName", "");
    }

    public RadioStation(String name, String URL) {
        this(name, URL, 0);
    }

    public RadioStation(String name, String URL, int id) {
        this.name = name;
        this.URL = URL;
        this.id = 0;
        songs = new LinkedHashSet<>();
        this.id = id;
    }
    //</editor-fold>  

    //<editor-fold desc="Song class">
    public static class Song implements Serializable {
        private String name;
        private String linkToSong;
        private String imageURL;
        private Date pubDate;
        private int stationId;

        // <editor-fold desc="Getters And Setters">
        public String getName() {
            return name;
        }

        public int getStationId() {
            return stationId;
        }

        public void setStationId(int stationId) {
            this.stationId = stationId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLinkToSong() {
            return linkToSong;
        }

        public void setLinkToSong(String linkToSong) {
            this.linkToSong = linkToSong;
        }

        public void setImageURL(String url) {
            this.imageURL = url;
        }

        public Date getPubDate() {
            return pubDate;
        }

        public void setPubDate(Date pubDate) {
            this.pubDate = pubDate;
        }

        public String getImageURL() {
            return imageURL;
        }

        // </editor-fold>

        public Song() {
            this.name = "Du Hust ";
            this.linkToSong = "some link";
            this.imageURL = "";
            this.pubDate = new Date();
        }

        @Override
        public int hashCode() {
            return this.linkToSong.hashCode();
        }

        public Song(String name, String linkToSong, String imageURL, Date pubDate, int stationId) {
            this();
            this.name = name;
            this.linkToSong = linkToSong;
            this.imageURL = imageURL;
            this.pubDate = pubDate;
            this.stationId = stationId;
        }
    }
    //</editor-fold>
}
