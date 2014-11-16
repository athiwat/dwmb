package com.thoughtworks;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class DWMBActivityTest {
    @Test
    public void testName() throws Exception {
        DWMBActivity activity = Robolectric.buildActivity(DWMBActivity.class).create().get();

        TextView textView = (TextView) activity.findViewById(R.id.textview);
        Button button = (Button) activity.findViewById(R.id.button);

        button.performClick();

        assertThat(textView.getText().toString(), is("Dude, Where is my Bart ?"));
    }
}