package com.example.dv.myalbum;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DV on 11/14/2016.
 */

public class aAdapter<T> extends ArrayAdapter<T>{
    ArrayList<Boolean> hasPicDays;
    public aAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
    }
    public void setPicDays(ArrayList<Boolean> list){
        hasPicDays = list;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View cV = super.getDropDownView(position, convertView, parent);
        TextView text = (TextView)cV;
        if(hasPicDays.get(position)){
            text.setTextColor(Color.parseColor("#CD853F"));
        }else{
            text.setTextColor(Color.GRAY);
        }

        return cV;
    }
}