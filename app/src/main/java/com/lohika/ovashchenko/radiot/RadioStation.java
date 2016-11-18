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

    public void setId(String URL) {
        this.URL = URL;
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

    public RadioStation(String URL) {
        name = "NoName";
        this.URL = URL;
        songs = new LinkedHashMap<>();
    }

    public RadioStation(String name, String URL) {
        this(URL);
        this.name = name;
    }

    public class Song {
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
