package com.example.nikola.finalmovieapp.utility;

import java.util.Calendar;
import java.util.Locale;

public class Utility {


    public static String loadDate(String date) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
        calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7)));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(8, 10)));

        return (calendar.get(Calendar.DAY_OF_MONTH)
                + " " + calendar.getDisplayName(calendar.MONTH, Calendar.LONG, Locale.US)
                + " " + calendar.get(Calendar.YEAR));

    }
}
