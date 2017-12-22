package com.example.shubhanshu.myapplication7;

import android.util.Log;
import android.widget.SeekBar;

/**
 * Created by USER-1 on 04-09-2016.
 */
public class Utilities {

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }
//
//    /**
//     * Function to get Progress percentage
//     * @param currentDuration
//     * @param totalDuration
//     * */
//    public double getProgressPercentage(long currentDuration, long totalDuration){
//        Double percentage = (double) 0;
//
//        long currentSeconds = (int) (currentDuration / 1000);
//        long totalSeconds = (int) (totalDuration / 1000);
//
//        // calculating percentage
//        percentage =(((double)currentSeconds)/totalSeconds)*100;
//
//        // return percentage
//        return percentage;
//    }

    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public double progressToTimer(SeekBar S, int totalDuration) {
        Log.e("SEEKPROGRESS", String.valueOf(S.getProgress()));
        Log.e("SEEKMAX", String.valueOf(S.getMax()));
        Log.e("SEEKTOTALDURATION", String.valueOf(totalDuration));
        return ((S.getProgress()/1000.0)*(totalDuration/1000.0))/S.getMax() * 1000000.0;
    }
}