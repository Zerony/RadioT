package com.lohika.ovashchenko.radiot;

import android.os.Handler;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ovashchenko on 11/14/16.
 */
public class RadioConnector implements Runnable{
    Handler handler;
    private static String LOG_TAG = "XML PARSER";
    public RadioConnector(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        connect("http://feeds.rucast.net/radio-t");
    }

    private void connect(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            Log.i("Response status",response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                parseXML(result);
                // instream.close();
            }

        } catch (Exception e) {}
    }

    private RadioStation parseXML(String text) {
        RadioStation result = new RadioStation(0);
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(text));
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                Log.d(LOG_TAG, "START_TAG: name = " + xpp.getName()
                        + ", depth = " + xpp.getDepth() + ", attrCount = "
                        + xpp.getAttributeCount());
                if ( xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("item")) {
                        RadioStation.Song song = result.new Song();
                        while (xpp.getEventType() != XmlPullParser.END_TAG || !xpp.getName().equals("item")) {

                            if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("title")) {
                                xpp.next();
                                song.setName(xpp.getText());
                            } else if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("enclosure")) {
                                for (int i = 0; i < xpp.getAttributeCount(); i++) {
                                    if (!xpp.getAttributeName(i).equals("url")) {
                                        continue;
                                    }
                                    song.setLinkToSong(xpp.getAttributeValue(i));
                                    break;
                                }

                            } else if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("description")) {
                                xpp.next();

                                //Pattern p = Pattern.compile("src=\".*\"");
                                Pattern p = Pattern.compile("src\\s*=\\s*\"([^\"]+)\"");

                                Matcher m = p.matcher(xpp.getText());
                                if (m.find()) {
                                    song.setImageURL(m.group(1));
                                }
                            }
                            xpp.next();
                        }
                        result.addSong(song);
                        /*for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            tmp = tmp + xpp.getAttributeName(i) + " = "
                                    + xpp.getAttributeValue(i) + ", ";
                        }*/
                        // if (!TextUtils.isEmpty(tmp))
                        // Log.d(LOG_TAG, "Attributes: " + tmp);
                }
                xpp.next();
            }
            Log.d(LOG_TAG, "END_DOCUMENT");
//            for(RadioStation.Song item : result.getSongs()) {
//                item.setImage(Utils.drawableFromUrl(item.getImageURL()));
//            }
            RadioApplication.getInstance().getRadioStationData().addStation(result);
            handler.sendEmptyMessage(0);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
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
