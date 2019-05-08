/*
 * Copyright (C) 2018 Eric Afenyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.eric.quickheadline.utils;

import java.util.Calendar;

/**
 * Created by eric on 22/02/2018.
 * this class formats raw Date data into human-readable form.
 */

public class DateUtils {

    /**
     * @param date a date in the format (YYYY-MM-DD). For example: 2000-01-01
     * @return new date in the format "January 01,"
     */
    public static String getLocalDate(String date) {
        String year = getLocalYear(date);
        String day = getLocalDay(date);
        String month = convertMonth(getLocalMonth(date));

        return String.format("%s %s", month, day);
    }

    /**
     * @param unixTime unix time. for example: "1519937105822"
     * @return new date in the format "January 01, 2000"
     */
    public static String getUNIXDate(long unixTime) {
        String utcDate = convertUnixToUTC(unixTime);
        return getLocalDate(utcDate);
    }


    /**
     * @param month a month int a number form. For example: "01" for January
     * @return a month in its String format
     */
    public static String convertMonth(String month) {

        if (month.equals("01") || month.equals("1")) {
            return "January";
        } else if (month.equals("02") || month.equals("2")) {
            return "February";
        } else if (month.equals("03") || month.equals("3")) {
            return "March";
        } else if (month.equals("04") || month.equals("4")) {
            return "April";
        } else if (month.equals("05") || month.equals("5")) {
            return "May";
        } else if (month.equals("06") || month.equals("6")) {
            return "June";
        } else if (month.equals("07") || month.equals("7")) {
            return "July";
        } else if (month.equals("08") || month.equals("8")) {
            return "August";
        } else if (month.equals("09") || month.equals("9")) {
            return "September ";
        } else if (month.equals("10")) {
            return "October";
        } else if (month.equals("11")) {
            return "November";
        } else if (month.equals("12")) {
            return "December";
        } else {
            throw new IllegalArgumentException("Unknown month format: " + month);
        }

    }

    /**
     * @param utcDateAndTime date in the format "2000-08-16T15:23:01Z"
     * @return a date in the format (YYYY-MM-DD). For example: 2000-01-01
     */
    public static String getUTCDate(String utcDateAndTime) {
        String[] arr = new String[2];
        final int dateIndex = 0;
        if (utcDateAndTime != null && utcDateAndTime.length() != 0) {
            arr = utcDateAndTime.split("T");
        }
        return arr[dateIndex];
    }

    private static String getLocalDay(String date) {
        return date.substring(8);
    }


    private static String getLocalYear(String date) {
        return date.substring(0, 4);
    }

    private static String getLocalMonth(String date) {
        return date.substring(5, 7);
    }


    private static Calendar provideCalender(long unixTime) {
        return Calendar.getInstance();
    }


    private static String getUNIXDay(long unixTime) {
        String unixDay = String.valueOf(provideCalender(unixTime).get(Calendar.DAY_OF_MONTH));
        if (unixDay.length() == 1) {
            return "0" + unixDay;
        } else if (unixDay.length() == 2) {
            return unixDay;
        } else throw new IllegalArgumentException("Unknown unix time format: " + unixTime);
    }


    private static String getUNIXYear(long unixTime) {
        return String.valueOf(provideCalender(unixTime).get(Calendar.YEAR));
    }

    private static String getUNIXMonth(long unixTime) {
        int rawMonth = (provideCalender(unixTime).get(Calendar.MONTH));
        //Months are indexed from 0 not 1 in Calendar so we add 1 to get actual month
        String unixMonth = String.valueOf(rawMonth + 1);
        if (unixMonth.length() == 1) {
            return "0" + unixMonth;
        } else if (unixMonth.length() == 2) {
            return unixMonth;
        } else throw new IllegalArgumentException("Unknown unix time format: " + unixTime);
    }

    private static String convertUnixToUTC(long unixTime) {
        String year = getUNIXYear(unixTime);
        String day = getUNIXDay(unixTime);
        String month = getUNIXMonth(unixTime);
        return year + "-" + month + "-" + day;
    }


}
