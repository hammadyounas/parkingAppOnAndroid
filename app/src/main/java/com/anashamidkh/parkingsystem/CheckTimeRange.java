package com.anashamidkh.parkingsystem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by anash on 7/30/2017.
 */

public final class CheckTimeRange {
    private final int startMinOfDay;
    private final int endMinOfDay;

    public CheckTimeRange(String text) {
        Pattern p = Pattern.compile("(\\d{1,2}):(\\d{2}) - (\\d{1,2}):(\\d{2})");
        Matcher m = p.matcher(text);
        if (! m.matches())
            throw new IllegalArgumentException("Invalid time range: " + text);
        this.startMinOfDay = minOfDay(m.group(1), m.group(2));
        this.endMinOfDay = minOfDay(m.group(3), m.group(4));
    }
    private static int minOfDay(String hour, String minute) {
        int h = Integer.parseInt(hour);
        int m = Integer.parseInt(minute);
        if (m >= 60 || h >= 24)
            throw new IllegalArgumentException("Invalid time: " + hour + ":" + minute);
        return h * 60 + m;
    }
    public boolean overlaps(CheckTimeRange subRange) {
        return (this.startMinOfDay < subRange.endMinOfDay && this.endMinOfDay > subRange.startMinOfDay);
        //Returns "true" if "Not available"
        //Returns "False" if "Available"
    }
}
