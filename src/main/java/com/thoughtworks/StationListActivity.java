package com.thoughtworks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class StationListActivity extends Activity {

    private static final String URL_GET_STATION = "http://api.bart.gov/api/stn.aspx?cmd=stns&key=MW9S-E7SL-26DU-VV8V";
    public static final String CURRENT_STATION = "CurrentStation";

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

                BartStationListXMLParser parser = new BartStationListXMLParser();
                return parser.parse(is);

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

        @Override
        protected void onPostExecute(Object stations) {
            super.onPostExecute(stations);
            ListView stationList = (ListView) findViewById(R.id.station_list);
            StationListAdapter adapter = new StationListAdapter(StationListActivity.this, (Map<String, String>) stations);
            stationList.setAdapter(adapter);
            stationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String abbr = (String) view.getTag();
                    Intent myIntent = new Intent(StationListActivity.this, TimeEstimationActivity.class);
                    myIntent.putExtra(CURRENT_STATION, abbr);
                    startActivity(myIntent);
                }
            });
        }

    }
}
