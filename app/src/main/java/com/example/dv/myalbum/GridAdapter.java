package com.example.dv.myalbum;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DV on 11/14/2016.
 */

public class GridAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ArrayList<Bitmap> fileList;
    private ArrayList<Integer> changedDayList;
    public GridAdapter(Context context, ArrayList<Bitmap>list, ArrayList<Integer> cDList){
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        fileList = new ArrayList<Bitmap>();
        changedDayList = new ArrayList<>();
        addAll(list);
        addAllCD(cDList);
    }
    public void clearData(){
        fileList.clear();
        changedDayList.clear();
    }
    public void addAll(ArrayList<Bitmap> list){
        fileList.addAll(list);
    }
    public void addAllCD(ArrayList<Integer> list){
        changedDayList.addAll(list);
    }
    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int i) {
        return fileList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.single_grid,null);
            holder.textView = (TextView)view.findViewById(R.id.txtHeader);
            holder.imageView = (ImageView)view.findViewById(R.id.imgSingle);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        holder.textView.setText("Day " + changedDayList.get(i));
        holder.imageView.setImageBitmap(fileList.get(i));
        holder.imageView.setRotation(90);

        return view;
    }
}
