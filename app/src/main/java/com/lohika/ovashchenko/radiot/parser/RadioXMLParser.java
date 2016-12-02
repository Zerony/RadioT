package com.lohika.ovashchenko.radiot.parser;

import com.lohika.ovashchenko.radiot.RadioStation;
import java.util.List;

/**
 * Created by ovashchenko on 11/24/16.
 */
public interface RadioXMLParser {
    String LOG_TAG = "RadioXMLParser PARSER";
    List<RadioStation.Song> parseXML(String text, RadioStation radioStation);
}