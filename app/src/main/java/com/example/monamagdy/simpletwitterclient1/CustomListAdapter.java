package com.example.monamagdy.simpletwitterclient1;

/**
 * Created by monamagdy on 1/9/17.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<Bitmap> pics_list = new ArrayList<Bitmap>();
    ArrayList<String> name_list = new ArrayList<String>();
    ArrayList<String> bio_list = new ArrayList<String>();
    public CustomListAdapter(Activity context,  ArrayList<String> name_list ,   ArrayList<Bitmap> pics_list ,  ArrayList<String> bio_list) {
        super(context, R.layout.mylist, name_list);
        // TODO Auto-generated constructor stub
        Log.d("custom","custom");
        this.context=context;
        this.name_list=name_list;
        this.pics_list=pics_list;
        this.bio_list=bio_list;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return name_list.size();
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
        txtTitle.setText(name_list.get(position));
      //  Log.d("PO", String.valueOf(position));
        Bitmap image = pics_list.get(position);
        Log.d(String.valueOf(image),String.valueOf(position));
        imageView.setImageBitmap(image);
        imageView.clearColorFilter();
        extratxt.setText(bio_list.get(position));
        return rowView;

    }

}
