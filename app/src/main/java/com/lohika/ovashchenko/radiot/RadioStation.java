package com.lohika.ovashchenko.radiot;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ovashchenko on 11/7/16.
 */
public class RadioStation implements Serializable{
    private String name;
    private String URL;
    private int id;
    private Map<String, Song> songs;

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

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getId() {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public long getLastSongTime() {
        if (this.getLastSong() == null) {
            return 0L;
        }
        return 0L;
        //return this.getLastSong().getPubDate().getTime();
    }

    public Song getLastSong () {
        if (this.songs.size() == 0) {
            return null;
        }
        Song result = new Song();
        result.setPubDate(new Date(0L));
        for (Song item : this.songs.values()) {
            if (item.getPubDate().getTime() > result.getPubDate().getTime()) {
                result = item;
            }
        }
        return result;
    }

    public List<Song> getSongs() {
        List<Song> result = new ArrayList<>();
        for(Song item : this.songs.values()) {
            result.add(item);
        }
        return result;
    }

    public void addSong(Song song) {
        if (this.songs.containsKey(song.getLinkToSong())) {
            return;
        }
        this.songs.put(song.getLinkToSong(), song);
    }
    // </editor-fold>  

    public RadioStation() {
        name = "NoName";
        this.URL = "";
        songs = new LinkedHashMap<>();
    }

    public RadioStation(String name, String URL) {
        this();
        this.name = name;
        this.URL = URL;
        this.id = 0;
    }

    public RadioStation(String name, String URL, int id) {
        this(name, URL);
        this.id = id;
    }

    public class Song implements Serializable {
        private String name;
        private String linkToSong;
        private String imageURL;
        private Date pubDate;

        // <editor-fold desc="Getters And Setters">
        public String getName() {
            return name;
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

        public int getStationId() {
            return RadioStation.this.getId();
        }
        // </editor-fold>

        public Song() {
            this.name = "Du Hust ";
            this.linkToSong = "some link";
            this.imageURL = "";
            this.pubDate = new Date();
        }

        public Song(String name, String linkToSong, String imageURL, Date pubDate) {
            this();
            this.name = name;
            this.linkToSong = linkToSong;
            this.imageURL = imageURL;
            this.pubDate = pubDate;

        }
    }
}
