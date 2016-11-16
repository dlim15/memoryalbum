package com.example.dv.myalbum;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by DV on 11/13/2016.
 */

public class DateRelated {
    private Activity activity;
    private Calendar calendar;
    private Button btnD;
    private DateFormat dateFormat;
    private DatePickerDialog.OnDateSetListener listener;

    public DateRelated(Activity act){
        init(act);
    }
    public void setButton(Button b){
        btnD = b;
    }
    public void setButtonTextToDate(){
        btnD.setText(dateFormat.format(calendar.getTime()));
    }
    private void init(Activity act){
        activity = act;
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance();
        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                setButtonTextToDate();
                Toast.makeText(activity.getApplicationContext(), "date:"+ calendar.getTimeInMillis(), Toast.LENGTH_SHORT).show();

            }
        };
    }
    public long dateDifference(long startDate){
        calendar = Calendar.getInstance();
        return (calendar.getTimeInMillis() - startDate) / (24*60*60*1000) + 1;
    }
    public String getCurrentDate(){
        return dateFormat.format(calendar.getTime());
    }
    public long getDate(){
        return calendar.getTimeInMillis();
    }
    public Calendar getCalendar(){
        return calendar;
    }
    public void openDatePicker(){
        new DatePickerDialog(activity,listener,
                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}
