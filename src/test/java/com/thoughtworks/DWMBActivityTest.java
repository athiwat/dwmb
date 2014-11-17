package com.thoughtworks;

import android.widget.Button;
import android.widget.TextView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class DWMBActivityTest {

    private DWMBActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(DWMBActivity.class).create().get();

    }

    @Test
    public void shouldSetTextAfterClicked() throws Exception {
        TextView textView = (TextView) activity.findViewById(R.id.textview);
        Button button = (Button) activity.findViewById(R.id.button);

        button.performClick();

        String expectedString = activity.getResources().getString(R.string.app_name);
        assertThat(textView.getText().toString(), is(expectedString));
    }
//
//    @Test
//    public void shouldLaunchNewActivityAfterClicked() throws Exception {
//        Button button = (Button) activity.findViewById(R.id.button_show_stations);
//
//        button.performClick();
//
//        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
//        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
//        ShadowIntent shadowIntent = Robolectric.shadowOf(nextStartedActivity);
//        assertThat(shadowIntent.getComponent().getClassName(), is("ss"));
//    }
}