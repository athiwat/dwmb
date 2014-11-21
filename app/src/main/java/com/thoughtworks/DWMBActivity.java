package com.thoughtworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class DWMBActivity extends Activity {

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View button_nearest_station = findViewById(R.id.button_nearest_station);
        button_nearest_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DWMBActivity.this, "Implement geo location here", Toast.LENGTH_LONG).show();
            }
        });

        View showStationsButton = findViewById(R.id.button_choose_a_station);
        showStationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(DWMBActivity.this, StationListActivity.class);
                startActivity(newIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(com.thoughtworks.R.menu.main, menu);
	return true;
    }

}

