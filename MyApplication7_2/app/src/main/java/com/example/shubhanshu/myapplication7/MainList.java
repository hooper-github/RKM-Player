package com.example.shubhanshu.myapplication7;

import android.content.Intent;
import android.os.Bundle;



import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class MainList extends Activity{

    ListView list;
    String[] itemname = new String[]{
            "Local Music",
            "Favourites",
            "Most played",
            "Recently Played"," Create Playlist"
    };

    Integer[] imgid = new Integer[]{
            R.drawable.musi,
            R.drawable.musi,
            R.drawable.musi,
            R.drawable.musi,
            R.drawable.index

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        custom adapter=new custom(this, itemname, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

                String Slecteditem= itemname[+position];

                if(Slecteditem=="Local Music")
                {
                    try {
                        Intent intent = new Intent(MainList.this,LocalList.class);
                        startActivity(intent);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(Slecteditem==" Create Playlist")
                {

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Be Decent..!!", Toast.LENGTH_SHORT).show();

                }
        }});

//        list.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                 TODO Auto-generated method stub
//                String Slecteditem= itemname[+position];
//                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        };
