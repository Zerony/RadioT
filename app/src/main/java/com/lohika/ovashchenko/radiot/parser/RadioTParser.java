package com.lohika.ovashchenko.radiot.parser;

import android.util.Log;

import com.lohika.ovashchenko.radiot.RadioStation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ovashchenko on 11/24/16.
 */
public class RadioTParser implements RadioXMLParser, Serializable{
    public static final String ENCLOSURE = "enclosure";
    public static final String TITLE = "title";
    public static final String ITEM = "item";
    public static final String DESCRIPTION = "description";
    public static final String PUB_DATE = "pubDate";
    public static final String URL = "url";
    public static final String REGEX  = "src\\s*=\\s*\"([^\"]+)\"";

    @Override
    public List<RadioStation.Song> parseXML(String text, RadioStation radioStation) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(text));
            boolean lastSongFound = false;
            List<RadioStation.Song> songsToInsert = new ArrayList<>();
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT && !lastSongFound) {
                Log.d(LOG_TAG, "START_TAG: name = " + xpp.getName()
                        + ", depth = " + xpp.getDepth() + ", attrCount = "
                        + xpp.getAttributeCount());
                if ( xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(ITEM)) {
                    RadioStation.Song song = new RadioStation.Song();
                    song.setStationId(radioStation.getId());
                    while (xpp.getEventType() != XmlPullParser.END_TAG || !xpp.getName().equals(ITEM)) {

                        if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(TITLE)) {
                            xpp.next();
                            song.setName(xpp.getText());
                        } else if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(ENCLOSURE)) {
                            for (int i = 0; i < xpp.getAttributeCount(); i++) {
                                if (!xpp.getAttributeName(i).equals(URL)) {
                                    continue;
                                }
                                song.setLinkToSong(xpp.getAttributeValue(i));
                                break;
                            }

                        } else if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(DESCRIPTION)) {
                            xpp.next();
                            Pattern p = Pattern.compile(REGEX);

                            Matcher m = p.matcher(xpp.getText());
                            if (m.find()) {
                                song.setImageURL(m.group(1));
                            }
                        } else if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals(PUB_DATE)) {
                            xpp.next();
                            long pubDate2 = Date.parse(xpp.getText());

                            if (radioStation.getLastSongTime() >= pubDate2) {
                                lastSongFound = true;
                                break;
                            }
                            Date pubDate = new Date(pubDate2);
                            song.setPubDate(pubDate);
                        }
                        xpp.next();
                    }
                    if (!lastSongFound) {
                        songsToInsert.add(song);
                    }
                }
                xpp.next();
            }
            Log.d(LOG_TAG, "END_DOCUMENT");


            return songsToInsert;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
