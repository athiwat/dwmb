package com.thoughtworks;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class BartEstimationXMLParserTest {

    @Test
    public void testParse() throws Exception {
        BartEstimationXMLParser parser = new BartEstimationXMLParser();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("bart_estimation.xml");
        List estimates = parser.parse(stream);

        List value = newArrayList(map("Dublin/Pleasanton","Leaving"), map("Dublin/Pleasanton","33"), map("SF Airport","114"), map("SFO/Millbrae","3"), map("SFO/Millbrae","23"),map("SFO/Millbrae","44"));

        assertThat(estimates, CoreMatchers.<List>is(value));
    }

    private Map<String, String> map(String station, String minutes) {
        Map map = new HashMap();
        map.put(station, minutes);
        return map;
    }
}