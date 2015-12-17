package com.example.maxi.swenapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatesUtility {

    static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd-hh:mm:ss");

    public static String dateFormater(String date) {
        String dateReturned = date.replace("-", "/").replace("T", "-").substring(0, 19);
        return dateReturned;
    }

    public static Date changeToDate(String stringDate) throws ParseException {
        Date date = dateFormat.parse(stringDate);
        return date;
    }
}
