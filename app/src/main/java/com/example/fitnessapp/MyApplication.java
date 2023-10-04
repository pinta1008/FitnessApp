package com.example.fitnessapp;

import android.app.Application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyApplication extends Application {

    public static final String formatTimestamp(long timestamp){
        try{
            Date netDate =(new Date(timestamp));
            SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            return sfd.format(netDate);
        }
        catch (Exception e)
        {
            return "date";
        }
    }
    public static boolean pocetna = true;
}
