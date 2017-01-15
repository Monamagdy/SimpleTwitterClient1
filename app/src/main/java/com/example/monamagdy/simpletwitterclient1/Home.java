package com.example.monamagdy.simpletwitterclient1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import android.app.ProgressDialog;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.BufferedReader;
import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;
import android.graphics.Bitmap;
import java.util.ArrayList;

/**
 * Created by monamagdy on 1/7/17.
 */
public class Home extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Current Activity", "Home");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        final ListView list;
        Bundle bundle = getIntent().getExtras();

     final ArrayList<String>   name_list = (ArrayList<String>) getIntent().getStringArrayListExtra("NAME");
        final ArrayList<String>  bio_list = (ArrayList<String>) getIntent().getStringArrayListExtra("BIOS");
        final ArrayList<Bitmap>bitmaps =  bundle.getParcelableArrayList("PICS");
        Log.d("nj",String.valueOf(bitmaps));
        final   CustomListAdapter adapter=new CustomListAdapter(this, name_list, bitmaps,bio_list);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub


                String Slectedname = name_list.get(+position);
                //Toast.makeText(getApplicationContext(), Slectedname, Toast.LENGTH_SHORT).show();
                Bitmap m = bitmaps.get(+position);
                Intent intent = new Intent(Home.this,FollowerInformation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ProfilePic",m);
                intent.putExtra("Name",Slectedname);
                startActivity(intent);


            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Home.this,HomePage.class);
        startActivity(a);
        Log.d("Home", "onBackPressed Called");
    }
}
