package com.ktvdb.allen.satrok.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Allen on 15/7/11.
 */
public class TimeUtils
{
    private static StringBuilder mFormatBuilder = new StringBuilder();
    private static Formatter     mFormatter     = new Formatter(mFormatBuilder,
                                                                Locale.getDefault());

    public static String formatTimestmp(Timestamp timestamp)
    {
        if (timestamp != null)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(timestamp);
        }
        return null;
    }

    public static String stringForTime(int timeMs)
    {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0)
        {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        }
        else
        {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}
