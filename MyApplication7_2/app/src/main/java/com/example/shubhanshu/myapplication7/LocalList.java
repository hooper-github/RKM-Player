package com.example.shubhanshu.myapplication7;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class LocalList extends Activity {


    private  ArrayList<Song> songList;
    ListView list;
    private ListView songView;
    private  ArrayList<String>itemname ;
    private  long imgid[];
    private  ArrayList<String> artist;
    private  ArrayList<String> path;
    int l;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylist);


        songList = new ArrayList<Song>();

        getSongList();

        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        l = songList.size();

        artist = new ArrayList<String>(l);
        path = new ArrayList<String>(l);
        itemname = new ArrayList<String>(l);
        imgid = new long[l];

        int i;
        for(i=0;i<l;i++) {
            itemname.add(songList.get(i).getTitle());
            artist.add(songList.get(i).getArtist());
            path.add(songList.get(i).getPath());
            imgid[i] = songList.get(i).getID();
        }

        customlistadapter adapter = new customlistadapter(this, itemname,artist, imgid, path);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //String Slecteditem = itemname[+position];
                Bundle bundle = new Bundle();

                bundle.putInt("currsongpos", position);
                bundle.putInt("totalsongs", l);
                bundle.putLongArray("songids", imgid);
                bundle.putStringArrayList("songpaths", path );
                bundle.putStringArrayList("songtitles", itemname);
                bundle.putStringArrayList("songartist", artist);


                Intent intent = new Intent(LocalList.this,UI.class);
                intent.putExtras(bundle);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });

        songList = null;
    }


    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                android.provider.MediaStore.Audio.Media.TITLE,
                android.provider.MediaStore.Audio.Media._ID,
                android.provider.MediaStore.Audio.Media.ARTIST,
                android.provider.MediaStore.Audio.Media.DATA
        };
        Cursor musicCursor = musicResolver.query(musicUri, projection, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int pathColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.DATA);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisPath = musicCursor.getString(pathColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist, thisPath ));
            }
            while (musicCursor.moveToNext());
        }
    }


    public class Song
    {

        private String title;
        private String artist;
        private String path;
        private long id;

        public Song(long songID, String songTitle, String songArtist, String p) {
//            count++;
            id=songID;
            title=songTitle;
            artist=songArtist;
            path = p;
        }

        public long getID(){return id;}
        public String getTitle(){return title;}
        public String getArtist(){return artist;}
        public String getPath(){return path;}

    }

}


