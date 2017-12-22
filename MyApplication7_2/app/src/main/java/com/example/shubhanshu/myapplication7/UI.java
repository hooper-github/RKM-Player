package com.example.shubhanshu.myapplication7;

import android.app.Activity;
import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import android.view.MotionEvent;

import com.example.shubhanshu.myapplication7.Utilities;

import java.io.IOException;



public class UI extends Activity implements MediaPlayer.OnCompletionListener,SeekBar.OnSeekBarChangeListener {
    private ImageView play,pause,next,prev,forw,backw,play2,pause2,next2,prev2,forw2,backw2;
    private ImageView iv;
    private static MediaPlayer mediaPlayer;
    private long startTime = 0;
    private long finalTime = 0;
    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private int i = 0;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;
    private Utilities utils;
    private static boolean MediaPlayerCreated = false;

    /**
     * Receiving song index from playlist view
     * and play the song
     * */
  /* @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            currentSongIndex = data.getExtras().getInt("songIndex");
            // play selected song
            playSong(currentSongIndex);
        }

    }
*/
    /**
     * Function to play a song
     * @param //songIndex - index of song
     * */
    public void  playSong(String Path, String Title){
        // Play song
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(Path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            // Displaying Song title
            String songTitle = "MySong";
            tx2.setText(Title);


        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        tx3.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
        );
        int MINU = (int) TimeUnit.MILLISECONDS.toMinutes((long) startTime);
        int SECS = (int) TimeUnit.MILLISECONDS.toSeconds((long) startTime);
        tx1.setText(String.format("%d min, %d sec", MINU, SECS - TimeUnit.MINUTES.toSeconds(MINU)));

        seekbar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 100);

        pause.setVisibility(View.VISIBLE);
        play.setVisibility(View.INVISIBLE);
        pause.requestFocus();
        // pause.setEnabled(true);
        //play.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main32);

        next = (ImageView) findViewById(R.id.next);
        pause = (ImageView) findViewById(R.id.pause);
        play=(ImageView)findViewById(R.id.play);
        prev=(ImageView)findViewById(R.id.prev);
        forw=(ImageView)findViewById(R.id.forw);
        backw=(ImageView)findViewById(R.id.backw);
        iv=(ImageView)findViewById(R.id.imageView);

        tx1=(TextView)findViewById(R.id.tx1);
        tx2=(TextView)findViewById(R.id.tx2);
        tx3=(TextView)findViewById(R.id.tx3);

//        bundle.putInt("currsongpos", position);
//        bundle.putInt("totalsongs", l);
//        bundle.putLongArray("songids", imgid);
//        bundle.putStringArrayList("songpaths", path );
//        bundle.putStringArrayList("songtitles", itemname);
//        bundle.putStringArrayList("songartist", artist);
        Bundle bundle = getIntent().getExtras();
        final int currsongpos = bundle.getInt("currsongpos");
        int totalsongs = bundle.getInt("totalsongs");
        final ArrayList<String> songPaths = bundle.getStringArrayList("songpaths");
        final ArrayList<String> songTitles = bundle.getStringArrayList("songtitles");
        ArrayList<String> songArtist = bundle.getStringArrayList("songartist");


        //set the title
        tx2.setText(songTitles.get(currsongpos));

        if(!MediaPlayerCreated)
        {
            File aud = new File(songPaths.get(currsongpos));
            Uri pathUri = Uri.fromFile(aud);
            String TAG = "MEDIAPLAYERUI";
//            Log.d(TAG, "path:" + songPaths.get(currsongpos));
//            Log.d(TAG, "Uri:" + pathUri);

            mediaPlayer = MediaPlayer.create(this, pathUri);
            if(mediaPlayer == null)
                Log.e(TAG, "MEDIAPLAYERCREATEDNULL");

            MediaPlayerCreated = true;
        }
        else
        {

            mediaPlayer.stop();
            try {
                mediaPlayer.setDataSource(songPaths.get(currsongpos));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        seekbar=(SeekBar)findViewById(R.id.seekBar);
        utils = new Utilities();
        seekbar.setOnSeekBarChangeListener(this);
        try
        {
            mediaPlayer.setOnCompletionListener(this);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            //Log.d("YOYO", "MEDIAPLAYERCRASHED");
        }



        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playSong(songPaths.get(currsongpos),songTitles.get(currsongpos) );

                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                pause.requestFocus();
            }
        });

        play.callOnClick();

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MMMPause","pausing");
                Toast.makeText(getApplicationContext(), "Pausing sound",Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();

                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
                play.requestFocus();
            }
        });

        forw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int temp = (int)startTime;

                if((temp+forwardTime)<=finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        backw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    public void updateProgressBar() {
        myHandler.postDelayed(UpdateSongTime, 100);
    }
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            int totalDuration = mediaPlayer.getDuration();
            tx1.setText(String.format("%d min, %d sec",

                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );
            // Updating progress bar
            seekbar.setProgress((int) (((startTime / 1000.0) * (seekbar.getMax() / 1000.0) / totalDuration) * 1000000.0));  //(int)(progress * totalDuration / 100)

//            Log.d("SEEK", (int) (progress * totalDuration / 100) + "\t" + String.valueOf(startTime));

            updateProgressBar();
        }
    };


    public boolean thumbnail(byte[] art)
    {
        Bitmap bmp = BitmapFactory.decodeByteArray(art, 0, art.length);
        if(bmp!=null) iv.setImageBitmap(bmp);
        return (bmp!=null);
    }

    @Override
    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekbar) {
        myHandler.removeCallbacks(UpdateSongTime);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekbar) {
//        myHandler.removeCallbacks(UpdateSongTime);
        // forward or backward to certain seconds
        mediaPlayer.seekTo((int)utils.progressToTimer(seekbar, mediaPlayer.getDuration()));
        // update and re-register timer progress again
        updateProgressBar();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

}