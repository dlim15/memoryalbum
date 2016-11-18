package com.example.dv.myalbum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class CreateNewActivity extends AppCompatActivity {
    private DatabaseHelper helper;
    private DatabaseDb database;
    private String firstDate;
    private Button btnSelectDate;
    private ButtonClickEvent btns;
    private DateRelated date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);
        init();
    }
    @Override
    protected void onPause() {
        super.onPause();
        helper.close();
    }
    @Override
    protected void onResume() {
        super.onResume();
        database.setDb(helper.getWritableDatabase());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }

    private void init(){
        helper = DatabaseHelper.getsInstance(getApplicationContext());
        btnSelectDate = (Button)findViewById(R.id.btnSelectDate);
        date = new DateRelated(this);
        date.setButton(btnSelectDate);
        date.setButtonTextToDate();
        database = new DatabaseDb(helper.getWritableDatabase());
        btns = new ButtonClickEvent(getApplicationContext(),this);
        btns.btnNewCancelClicked((Button)findViewById(R.id.btnCreateNewCancel));
        btns.btnNewSubmitClicked((Button)findViewById(R.id.btnCreateNewSubmit),date, database);
        btns.btnSelectDateClicked(btnSelectDate, date);
    }



}
