package com.ericafenyo.quickheadline.utils;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Moment {

    private static final String LOG_TAG = Moment.class.getName();

    private static final String TIME_UNIT_SECONDS = "sec ago";
    private static final String TIME_UNIT_MINUTES = "min ago";
    private static final String TIME_UNIT_HOURS = "h ago";
    private static final String TIME_UNIT_DAYS = "d ago";


    /**
     * Typical MySqL/SQL dateTime format with dash as separator
     */
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd";

    public enum DateTimeUnits {DAYS, HOURS, MINUTES, SECONDS, MILLISECONDS}

    public static Date formatDate(String dateString, Locale locale) {
        SimpleDateFormat iso8601Format = new SimpleDateFormat(DATE_TIME_PATTERN, locale);
        iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        if (dateString != null) {
            try {
                date = iso8601Format.parse(dateString.trim());
            } catch (ParseException e) {
                Log.e(LOG_TAG, "formatDate >> Fail to parse supplied date string >> " + dateString);
                e.printStackTrace();
            }
        }
        return date;
    }

    public static Date formatDate(String date) {
        return formatDate(date, Locale.getDefault());
    }

    private static int getDateDiff(Date nowDate, Date oldDate, DateTimeUnits dateDiff) {
        long diffInMs = nowDate.getTime() - oldDate.getTime();
        int days = (int) TimeUnit.MILLISECONDS.toDays(diffInMs);
        int hours = (int) (TimeUnit.MILLISECONDS.toHours(diffInMs) - TimeUnit.DAYS.toHours(days));
        int minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(diffInMs) - TimeUnit.HOURS
            .toMinutes(TimeUnit.MILLISECONDS.toHours(diffInMs)));
        int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(diffInMs);
        switch (dateDiff) {
            case DAYS:
                return days;
            case SECONDS:
                return seconds;
            case MINUTES:
                return minutes;
            case HOURS:
                return hours;
            case MILLISECONDS:
            default:
                return (int) diffInMs;
        }
    }

    public static String getTimeAgo(String dateString) {
        Date date = formatDate(dateString);
        double seconds = getDateDiff(new Date(), date, DateTimeUnits.SECONDS);
        double minutes = seconds / 60;
        double hours = minutes / 60;
        double days = hours / 24;

        String phrase;
        if (seconds < 60) {
            phrase = Math.round(seconds) + TIME_UNIT_SECONDS;
        } else if (seconds == 60 || minutes < 60) {
            phrase = Math.round(minutes) + TIME_UNIT_MINUTES;
        } else if (minutes == 60 || hours < 24) {
            phrase = Math.round(hours) + TIME_UNIT_HOURS;
        } else if (hours >= 24) {
            phrase = Math.round(Math.floor(days)) + TIME_UNIT_DAYS;
        } else {
            phrase = Math.round(seconds) + TIME_UNIT_SECONDS;
        }
        return phrase;
    }
}
