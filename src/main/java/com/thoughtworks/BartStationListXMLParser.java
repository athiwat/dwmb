package com.thoughtworks;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class BartStationListXMLParser {

    public Map<String, String> parse(InputStream inputStream) throws IOException {
    try {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        parser.nextTag();
        return readRoot(parser);

    } catch (Exception e) {
        Map<String, String> map = new HashMap();
        map.put("Error", e.getMessage());
        return map;
    }
    finally {
        if (inputStream != null) {
            inputStream.close();
        }
    }
}


    private Map readRoot(XmlPullParser parser) throws XmlPullParserException, IOException {
        Map<String, String> map = new HashMap<String, String>();
        parser.require(XmlPullParser.START_TAG, null, "root");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("stations")) {
                map.putAll(readStations(parser));
            } else {
                skip(parser);
            }
        }
        return map;
    }

    private Map<String, String> readStations(XmlPullParser parser) throws IOException, XmlPullParserException {
        Map<String, String> map = new HashMap<String, String>();
        parser.require(XmlPullParser.START_TAG, null, "stations");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("station")) {
                map.putAll(readStation(parser));
            } else {
                skip(parser);
            }
        }
        return map;
    }

    private Map<String, String> readStation(XmlPullParser parser) throws IOException, XmlPullParserException {
        Map<String, String> map = new HashMap<String, String>();
        parser.require(XmlPullParser.START_TAG, null, "station");
        String stationName = "";
        String stationAbbr = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if ("name".equalsIgnoreCase(name)) {
                stationName = readName(parser, "name");
            } else if ("abbr".equalsIgnoreCase(name)) {
                stationAbbr = readName(parser, "abbr");
            } else {
                skip(parser);
            }

        }
        map.put(stationAbbr, stationName);
        return map;
    }

    private String readName(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        String station = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, tag);
        return station;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
