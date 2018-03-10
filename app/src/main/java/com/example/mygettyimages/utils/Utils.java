package com.example.mygettyimages.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import java.util.Calendar;

public class Utils {

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static void logLongMessage(String tag, String msg, int logLevel) {
        if (msg == null || tag == null) {
            return;
        }
        int len = 3000;
        if (msg.length() > len) {
            switch (logLevel) {
                case Log.DEBUG:
                    Log.d(tag, msg.substring(0, len));
                    break;
                case Log.INFO:
                    Log.i(tag, msg.substring(0, len));
                    break;
                case Log.VERBOSE:
                    Log.v(tag, msg.substring(0, len));
                    break;
                case Log.ERROR:
                    Log.e(tag, msg.substring(0, len));
                    break;
                case Log.WARN:
                    Log.w(tag, msg.substring(0, len));
                    break;
            }

            logLongMessage(tag, msg.substring(len), logLevel);
        } else {
            switch (logLevel) {
                case Log.DEBUG:
                    Log.d(tag, msg);
                    break;
                case Log.INFO:
                    Log.i(tag, msg);
                    break;
                case Log.VERBOSE:
                    Log.v(tag, msg);
                    break;
                case Log.ERROR:
                    Log.e(tag, msg);
                    break;
                case Log.WARN:
                    Log.w(tag, msg);
                    break;
            }
        }
    }

    public static long getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }
}
