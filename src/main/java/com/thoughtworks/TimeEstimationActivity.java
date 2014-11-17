package com.thoughtworks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.widget.ListView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TimeEstimationActivity extends Activity {

    private static final String URL_GET_TIME_ESTIMATION = "http://api.bart.gov/api/etd.aspx?cmd=etd&orig=%s&key=MW9S-E7SL-26DU-VV8V";
    private String currentStation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_list);
        currentStation = getIntent().getStringExtra(StationListActivity.CURRENT_STATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetTimeEstimationTask().execute();
    }



    class GetTimeEstimationTask extends AsyncTask {
        @Override
        protected Map<String, String> doInBackground(Object[] objects) {
            try {
                return getStations(String.format(URL_GET_TIME_ESTIMATION, currentStation));
            } catch (IOException e) {
                return new HashMap<String, String>();
            }
        }

        private Map<String,String> getStations(String address) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                is = conn.getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(is, null);
                parser.nextTag();
                return readRoot(parser);

            } catch (Exception e) {
                Map<String, String> map = new HashMap();
                map.put("Error", e.getMessage());
                return map;
            }
            finally {
                if (is != null) {
                    is.close();
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
                if ("station".equals(name)) {
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
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the entry tag
                if ("etd".equals(name)) {
                    map.putAll(readETD(parser));
                } else {
                    skip(parser);
                }
            }
            return map;
        }

        private Map<String, String> readETD(XmlPullParser parser) throws IOException, XmlPullParserException {
            Map<String, String> map = new HashMap<String, String>();
            parser.require(XmlPullParser.START_TAG, null, "etd");
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
                } else {
                    skip(parser);
                }

            }
            map.put(destination, estimate);
            return  map;
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

        @Override
        protected void onPostExecute(Object estimates) {
            super.onPostExecute(estimates);

            ListView stationList = (ListView) findViewById(R.id.station_list);
            TimeEstimationAdapter adapter = new TimeEstimationAdapter(TimeEstimationActivity.this, (Map<String, String>) estimates);
            stationList.setAdapter(adapter);
        }
    }
}