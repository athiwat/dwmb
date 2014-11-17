package com.thoughtworks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class StationListActivity extends Activity {

    private static final String URL_GET_STATION = "http://api.bart.gov/api/stn.aspx?cmd=stns&key=MW9S-E7SL-26DU-VV8V";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DownloadStationTask downloadStationTask = new DownloadStationTask();
        downloadStationTask.execute();

    }

    class DownloadStationTask extends AsyncTask {
        @Override
        protected Map<String, String> doInBackground(Object[] objects) {
            try {
                return getStations(URL_GET_STATION);
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

        @Override
        protected void onPostExecute(Object stations) {
            super.onPostExecute(stations);
            Toast.makeText(StationListActivity.this, "Post Execute", Toast.LENGTH_SHORT).show();

            ListView stationList = (ListView) findViewById(R.id.station_list);
            StationListAdapter adapter = new StationListAdapter(StationListActivity.this, (Map<String, String>) stations);
            stationList.setAdapter(adapter);
            stationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String abbr = (String) view.getTag();

                    Toast.makeText(getApplicationContext(),abbr, Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(StationListActivity.this, TimeActivity.class);
                    startActivity(myIntent);
                }
            });
        }

    }
}
