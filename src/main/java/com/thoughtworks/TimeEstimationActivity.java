package com.thoughtworks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

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
        protected List doInBackground(Object[] objects) {
            try {
                return getStations(String.format(URL_GET_TIME_ESTIMATION, currentStation));
            } catch (IOException e) {
                return newArrayList();
            }
        }

        private List getStations(String address) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                is = conn.getInputStream();
                BartEstimationXMLParser parser = new BartEstimationXMLParser();
                List<Map<String, String>> estimates = parser.parse(is);
                return estimates;
            }
            finally {
                if (is != null) {
                    is.close();
                }
            }
        }



        @Override
        protected void onPostExecute(Object estimates) {
            super.onPostExecute(estimates);

            ListView stationList = (ListView) findViewById(R.id.station_list);
            TimeEstimationAdapter adapter = new TimeEstimationAdapter(TimeEstimationActivity.this, (List<Map<String, String>>) estimates);
            stationList.setAdapter(adapter);
        }
    }
}