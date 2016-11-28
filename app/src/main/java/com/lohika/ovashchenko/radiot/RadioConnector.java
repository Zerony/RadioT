package com.lohika.ovashchenko.radiot;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ovashchenko on 11/14/16.
 */
public class RadioConnector implements Runnable{
    private static String LOG_TAG = "XML PARSER";
    private Handler handler;
    private Collection<RadioStation> radioStation;
    public RadioConnector(Handler handler, Collection<RadioStation> radioStation) {
        this.handler = handler;
        this.radioStation = radioStation;
    }

    @Override
    public void run() {
        if (RadioApplication.getInstance().isSynced()) {
            return;
        }
        for (RadioStation item : this.radioStation) {
            connect(item);
        }
        RadioApplication.getInstance().synced();
    }

    private void connect(RadioStation station) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(station.getURL());
        HttpResponse response;
        RadioDB db = new RadioDB(RadioApplication.getInstance());
        db.open();

        try {
            response = httpclient.execute(httpget);
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
}
