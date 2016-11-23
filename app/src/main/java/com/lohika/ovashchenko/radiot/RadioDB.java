package com.lohika.ovashchenko.radiot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ovashchenko on 11/21/16.
 */
public class RadioDB {
    private static final String DB_NAME = "playDB";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_SONGS = "songs";
    private static final String DB_TABLE_STATIONS = "stations";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_IMG_URL = "imgURL";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CREATED_DATE = "createdDate";
    public static final String COLUMN_SONG_URL = "songURL";
    public static final String COLUMN_RADIO_URL = "radioURL";
    public static final String COLUMN_STATION_ID = "radioID";

    private static Map<String, RadioStation> allStations = new HashMap<>();

    private static final String DB_CREATE_SONGS =
            "create table " + DB_TABLE_SONGS + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_IMG_URL + " text, " +
                    COLUMN_NAME + " text," +
                    COLUMN_CREATED_DATE + " integer," +
                    COLUMN_STATION_ID + " integer," +
                    COLUMN_SONG_URL + " text" +
                    ");";

    private static final String DB_CREATE_STATIONS =
            "create table " + DB_TABLE_STATIONS + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NAME + " text," +
                    COLUMN_RADIO_URL + " text" +
                    ");";

    public static List<RadioStation> cursorToStations(Cursor cursor) {
        List<RadioStation> result = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()){
            int nameColumn = cursor.getColumnIndex(COLUMN_NAME);
            int urlColumn = cursor.getColumnIndex(COLUMN_RADIO_URL);
            int idColumn = cursor.getColumnIndex(COLUMN_ID);
            do{
                RadioStation itemStation = new RadioStation(cursor.getString(nameColumn), cursor.getString(urlColumn), cursor.getInt(idColumn));
                result.add(itemStation);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public static List<RadioStation.Song> cursorToListSongs(Cursor cursor) {
        List<RadioStation.Song> result = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()){
            int nameColumn = cursor.getColumnIndex(COLUMN_NAME);
            int songURLColumn = cursor.getColumnIndex(COLUMN_SONG_URL);
            int createdDateColumn = cursor.getColumnIndex(COLUMN_CREATED_DATE);
            int imageURLColumn = cursor.getColumnIndex(COLUMN_IMG_URL);
            int stationIdColumn = cursor.getColumnIndex(COLUMN_STATION_ID);

            do{
                RadioStation station = RadioApplication.getInstance().getRadioStationData().getStationByIndex(cursor.getInt(stationIdColumn));
                RadioStation.Song song = station.new Song(
                        cursor.getString(nameColumn),
                        cursor.getString(songURLColumn),
                        cursor.getString(imageURLColumn),
                        new Date (cursor.getLong(createdDateColumn)));
                result.add(song);
            } while (cursor.moveToNext());
        }
        return result;
    }

    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public RadioDB(Context ctx) {
        mCtx = ctx;
    }

    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    public Cursor getAllSongs() {
        return mDB.query(DB_TABLE_SONGS, null, null, null, null, null, null);
    }

    public Cursor getAllStations() {
        return mDB.query(DB_TABLE_STATIONS, null, null, null, null, null, null);
    }

    public long addSong(RadioStation.Song song) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IMG_URL, song.getImageURL());
        cv.put(COLUMN_SONG_URL, song.getLinkToSong());
        cv.put(COLUMN_NAME, song.getName());
        cv.put(COLUMN_CREATED_DATE, song.getPubDate().getTime());
        cv.put(COLUMN_STATION_ID, song.getStationId());
        return mDB.insert(DB_TABLE_SONGS, null, cv);
    }

    public int addSongs(Collection<RadioStation.Song> songs) {

        Map<String, RadioStation.Song> songsInDB  = new HashMap<>();
        for (RadioStation.Song item : cursorToListSongs(getAllSongs())) {
            songsInDB.put(item.getLinkToSong(), item);
        }
        mDB.beginTransaction();
        int totalInserted = 0;
        for (RadioStation.Song item : songs) {
            if (songsInDB.containsKey(item.getLinkToSong())) {
                continue;
            }
            long index = this.addSong(item);
            if (index > 0) {
                totalInserted++;
            }
        }
        mDB.setTransactionSuccessful();
        mDB.endTransaction();
        return totalInserted;
    }

    public void addStation(RadioStation station) {
        if (allStations.size() == 0) {
            Cursor cursor = this.getAllStations();
            for (RadioStation itemStation : cursorToStations(cursor)) {
                allStations.put(itemStation.getURL(), itemStation);
            }
        }
        if (allStations.containsKey(station.getURL())) {
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, station.getName());
        cv.put(COLUMN_RADIO_URL, station.getURL());
        long stationId = mDB.insert(DB_TABLE_STATIONS, null, cv);
        station.setId((int)stationId);
        allStations.put(station.getURL(), station);
    }

    public int getStationId(RadioStation station) {
        if (allStations.size() == 0) {
            Cursor cursor = this.getAllStations();
            for (RadioStation itemStation : cursorToStations(cursor)) {
                allStations.put(itemStation.getURL(), itemStation);
            }
        }

        if (!allStations.containsKey(station.getURL())) {
            return -1;
        }

        return allStations.get(station.getURL()).getId();
    }

    public void delSong(long id) {
        mDB.delete(DB_TABLE_SONGS, COLUMN_ID + " = " + id, null);
    }

    public void delAllSong() {
        mDB.delete(DB_TABLE_SONGS, null, null);
    }


    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_SONGS);
            db.execSQL(DB_CREATE_STATIONS);

//            ContentValues cv = new ContentValues();
//
//            cv.put(COLUMN_NAME, "RadioT");
//            cv.put(COLUMN_RADIO_URL, "http://feeds.rucast.net/radio-t");
//            db.insert(DB_CREATE_STATIONS, null, cv);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
