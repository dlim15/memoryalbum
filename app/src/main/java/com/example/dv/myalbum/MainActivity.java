package com.example.dv.myalbum;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ButtonClickEvent btns;
    private DatabaseHelper databaseHelper;
    private PermissionCheck pCheck;
    private SQLStatements sqs;
    private SomeGlobalVar gVar;
    private DatabaseDb database;
    private DateRelated date;

    @Override
    protected void onPause() {
        super.onPause();
        databaseHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        buttonHandles();
    }

    private void init(){
        gVar = new SomeGlobalVar();
        sqs = new SQLStatements();
        date = new DateRelated(this);
        pCheck = new PermissionCheck();
        pCheck.verifyStoragePermissions(this);
        btns = new ButtonClickEvent(getApplicationContext(),this);
//        this.deleteDatabase("albumdb");
        databaseHelper = new DatabaseHelper(getApplicationContext(),"albumdb", null, 2);
        database = new DatabaseDb(databaseHelper.getWritableDatabase());

    }
    private void buttonHandles(){
        btns.btnNewClicked((Button)findViewById(R.id.btnNew));
        btns.btnLoadClicked((Button)findViewById(R.id.btnLoad));
        btns.btnTakeNewClicked((Button)findViewById(R.id.btnTakeNew));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1 :
                if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(getApplicationContext(), "You have to allow storage permission in order to use the app.",Toast.LENGTH_LONG).show();
                    finish();
                }

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case 1002 :
                    String fileLoc = data.getStringExtra(gVar.PATH);
                    int albumNum = data.getIntExtra(gVar.INTENT_ALBUM_NO,-1);
                    Cursor c = database.sqlSelect(sqs.C_ALBUM_FIRST_DAY,sqs.T_ALBUM,sqs.C_ALBUM_ID+"="+albumNum);
                    c.moveToNext();
                    int today = (int)date.dateDifference(c.getLong(0));
                    database.insertFile(albumNum,today,fileLoc);
                    break;

            }
        }
    }
}
