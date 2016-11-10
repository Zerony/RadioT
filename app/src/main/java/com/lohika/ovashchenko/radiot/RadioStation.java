package com.lohika.ovashchenko.radiot;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;
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
        return songs;
    }

    public static List<RadioStation> getAllRadioStations() {
        return RadioStationSingleton.getInstance().getAllRadioStations();
    }
    // </editor-fold>  

    private String name;
    private String id;
    private int index;
    private ArrayList<Song> songs;

    private RadioStation() {
        index = RadioStationSingleton.getInstance().size();
        name = "Station " + index;
        id = name.replaceAll(" ", "");
        songs = new ArrayList<>();
        RadioStationSingleton singleton = RadioStationSingleton.getInstance();
        singleton.addStation(this);

    }

    public static void generateStations(int number) {
        if (RadioStationSingleton.getInstance().size() > 0) {
            return;
        }
        for(int i=0; i<number; i++) {
            RadioStation radio = new RadioStation();
            for(int j=0; j<5; j++) {
                Song song = radio.new Song(radio.getName() + j, radio.getName() + "| SongName "+ j, "Link " + j, "Image" + j);
                radio.songs.add(song);
            }

        }
    }

    public static RadioStation getStation(String index) {
        return RadioStationSingleton.getInstance().getStation(index);
    }

    public static RadioStation getStation(int index) {
        return RadioStationSingleton.getInstance().getStation(index);
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
        private String image;

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



        public void setImage(String image) {
            this.image = image;
        }
        // </editor-fold>

        public Song() {
            this.artist = "Rammstein";
            this.name = "Du Hust ";
            this.linkToSong = "some link";
            this.image = "Some image";
        }

        public Song(String artist, String name, String linkToSong, String image) {
            this();
            this.artist = artist;
            this.name = name;
            this.linkToSong = linkToSong;
            this.image = image;
        }
        
        public int getImage() {// add context remove from class
            int rand = Utils.generateRand(RadioStationSingleton.getInstance().getImagesSize());
            return RadioStationSingleton.getInstance().getImageByNumber(rand);
            // return context.getResources().getDrawable(RadioStationSingleton.getInstance().getImageByNumber(rand));
        }
    }
}
