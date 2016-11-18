package com.lohika.ovashchenko.radiot;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ovashchenko on 11/7/16.
 */
public class RadioStation implements Comparable<RadioStation>, Serializable{
    // <editor-fold desc="Getters And Setters">  
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Song> getSongs() {
        return this.songs;
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }

    // </editor-fold>  

    private String name;
    private String id;
    private int index;
    private ArrayList<Song> songs;

    public RadioStation(int index) {
        this.index = index;
        name = "Station " + index;
        id = name.replaceAll(" ", "");
        songs = new ArrayList<>();
    }


    @Override
    public int compareTo(RadioStation o) {
        if (o.index == this.index) {
            return 0;
        }
        return (o.index > this.index)?-1:1;
    }

    public class Song {
        private String artist;
        private String name;
        private String linkToSong;
        private String imageURL;
        private Date pubDate;

        public Drawable getImage() {
            return image;
        }

        public void setImage(Drawable image) {
            this.image = image;
        }

        private Drawable image;

        // <editor-fold desc="Getters And Setters">
        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

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
            this.artist = "Rammstein";
            this.name = "Du Hust ";
            this.linkToSong = "some link";
            this.imageURL = "";
        }

        public Song(String artist, String name, String linkToSong, String imageURL) {
            this();
            this.artist = artist;
            this.name = name;
            this.linkToSong = linkToSong;
            this.imageURL = imageURL;
        }


    }
}
