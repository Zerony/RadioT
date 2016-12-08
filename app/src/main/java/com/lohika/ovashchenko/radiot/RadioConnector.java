package com.lohika.ovashchenko.radiot;

import android.os.Handler;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

/**
 * Created by ovashchenko on 11/14/16.
 */
public class RadioConnector implements Runnable{
    private static String LOG_TAG = "XML PARSER";
    private  final HttpClient client;
    private  final RadioDB db;
    private  final PlayController playController;
    private Handler handler;
    private Collection<RadioStation> radioStation;

    public RadioConnector(HttpClient client, RadioDB db, PlayController playController) {
        this.client = client;
        this.db = db;
        this.playController = playController;
    }

    @Override
    public void run() {
        if (playController.isSynced()) {
            return;
        }
        for (RadioStation item : this.radioStation) {
            connect(item);
        }
        playController.synced();
    }

    private void connect(RadioStation station) {
        HttpGet httpget = new HttpGet(station.getURL());
        HttpResponse response;
        db.open();

        try {
            response = client.execute(httpget);
            Log.i("Response status",response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                List<RadioStation.Song> songsToInsert = station.parseXML(result);
                int totalInserted = db.addSongs(songsToInsert);
                if (totalInserted > 0) {
                    handler.sendEmptyMessage(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setRadioStation(Collection<RadioStation> radioStation) {
        this.radioStation = radioStation;
    }
}
