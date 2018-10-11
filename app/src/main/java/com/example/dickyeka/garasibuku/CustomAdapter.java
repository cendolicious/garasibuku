package com.example.dickyeka.garasibuku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by DICKYEKA on 16/05/2017.
 */

public class CustomAdapter extends BaseAdapter {
    String nama[];
    int foto[];
    Context context;
    LayoutInflater inflater;

    public CustomAdapter(String[] nama, int[] foto, Context context) {
        this.nama = nama;
        this.foto = foto;
        this.context = context;
        inflater = (LayoutInflater.from(context));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_listview,parent,false);
        ImageView ft = (ImageView) convertView.findViewById(R.id.foto);


        ft.setImageResource(foto[position]);

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return nama[position];
    }

    @Override
    public int getCount() {
        return nama.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
