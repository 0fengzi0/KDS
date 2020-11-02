package com.ixf.kds.utils;

import android.util.Log;

public class MLog {

    public static void debug(String logName, String logValue) {
        logName = logName == null ? "" : logName;
        logValue = logValue == null ? "" : logValue;
        Log.d("========[" + logName + "]", logValue);
    }
}
