package com.ixf.kds;

import com.ixf.kds.utils.NumberFormat;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testMain() {

        String[] audioNameList = {"q", "0", "1", "0", "0", "qc"};
        String[] kdsNumberArray = NumberFormat.number2String4(123).split("");
        System.arraycopy(kdsNumberArray, 0, audioNameList, 1, 4);

        System.out.println(Arrays.toString(audioNameList));
    }
}