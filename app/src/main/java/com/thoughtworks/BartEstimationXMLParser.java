//Based on http://developer.android.com/training/basics/network-ops/xml.html

package com.thoughtworks;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BartEstimationXMLParser {

    public List<Map<String, String>> parse(InputStream inputStream) throws IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readRoot(parser);

        } catch (Exception e) {
            return new ArrayList<>();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }


    private List readRoot(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "root");
        List entries = new ArrayList();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if ("station".equals(name)) {
                entries.addAll(readStation(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private List readStation(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "station");
        List entries = new ArrayList();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if ("etd".equals(name)) {
                entries.addAll(readETD(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private List readETD(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "etd");
        List entries = new ArrayList();
        String destination = "";
        String estimate = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if ("destination".equalsIgnoreCase(name)) {
                destination = readName(parser, "destination");
            } else if ("estimate".equalsIgnoreCase(name)) {
                estimate = readEstimate(parser);
                HashMap<String, String> map = new HashMap<>();
                map.put(destination, estimate);
                entries.add(map);
            } else {
                skip(parser);
            }

        }
        return entries;
    }


    private String readEstimate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "estimate");
        String minutes = "";
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if ("minutes".equalsIgnoreCase(name)) {
                minutes = readName(parser, "minutes");
            } else {
                skip(parser);
            }
        }
        return minutes;
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
