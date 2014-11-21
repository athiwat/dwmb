package com.thoughtworks;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DWMBActivityTest {

    private DWMBActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(DWMBActivity.class).create().get();

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