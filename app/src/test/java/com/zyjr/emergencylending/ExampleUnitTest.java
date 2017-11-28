package com.two.emergencylending;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
        String tv_entrydata = "2016-5-8";
        String[] date = tv_entrydata.split("-");
        tv_entrydata = date[0] + "-" +
                (date[1].length() < 2 ? "0" + date[1] : date[1]) + "-" +
                (date[2].length() < 2 ? "0" + date[2] : date[2]);
        System.out.println(tv_entrydata);
    }
}