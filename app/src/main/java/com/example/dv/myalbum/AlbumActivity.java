package com.example.dv.myalbum;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AlbumActivity extends AppCompatActivity {
    private GridView gridAlbum;
    private ArrayList<Boolean> filledInDays;
    private int albumNum;
    private String albumName;
    private int today;
    private int selectedDay;
    private ButtonClickEvent btns;
    private SomeGlobalVar vars;
    private SQLStatements sqs;
    private FileInputOutput files;
    private Spinner spinnerDate;
    private DateRelated date;
    private DatabaseDb database;
    private DatabaseHelper helper;
    private Button btnFromGallery;
    private String[] path;
    private ArrayList<Bitmap> fileList;
    private ArrayList<String> filePath;
    private GridAdapter imageAdapter;
    private ExecutorService executor;
    private List<imgCallable> imgs;
    private ArrayList<Integer> changedDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        database.setDb(helper.getWritableDatabase());
    }

    @Override
    protected void onPause() {
        super.onPause();
        helper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.close();
    }

    private void init(){
        executor = Executors.newFixedThreadPool(10);
        imgs = new ArrayList<imgCallable>();
        filledInDays = new ArrayList<Boolean>();
        path = new String[1];
        filePath = new ArrayList<String>();
        fileList = new ArrayList<Bitmap>();
        Intent intent = getIntent();
        btnFromGallery = (Button)findViewById(R.id.btnFromGallery);
        helper = DatabaseHelper.getsInstance(getApplicationContext());
        database = new DatabaseDb(helper.getWritableDatabase());
        sqs = new SQLStatements();
        date = new DateRelated(this);
        vars = new SomeGlobalVar();
        files = new FileInputOutput();
        albumNum = intent.getIntExtra(vars.INTENT_ALBUM_NO,-1);
        btns = new ButtonClickEvent(getApplicationContext(),this);
        btns.btnTakePicClicked((Button)findViewById(R.id.btnTakePic),albumNum,path);
        btns.btnBtnFromGallery(btnFromGallery);
        spinnerDate = (Spinner)findViewById(R.id.spinnerDays);
        changedDays = new ArrayList<Integer>();
        setSpinnerItem();
        setGridView();
        setSpinnerListener();

    }
    private void setGridView(){
        gridAlbum = (GridView)findViewById(R.id.gridAlbum);
        imageAdapter = new GridAdapter(getApplicationContext(), fileList, changedDays);
        gridAlbum.setAdapter(imageAdapter);
        gridAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),DetailedImageActivity.class);
                intent.putExtra(vars.PATH,filePath);
                intent.putExtra(vars.ALBUM_NAME,albumName);
                intent.putExtra(vars.AT,i);
                startActivity(intent);

            }
        });
    }
    private void setSpinnerItem(){
        List<String> list = new ArrayList<String>();
        Cursor c = database.sqlSelect(sqs.C_ALBUM_NAME + "," + sqs.C_ALBUM_FIRST_DAY,sqs.T_ALBUM,sqs.C_ALBUM_ID+"="+albumNum);
        c.moveToNext();
        albumName = c.getString(0);
        setTitle(albumName);
        long curDate = date.dateDifference(c.getLong(1));
        for(long i=0; i<curDate; i++){
            filledInDays.add(false);
            list.add((i+1)+"");
            if(i == curDate-1)
                today = (int)i+1;
        }
        list.add("View All");
        setFilledInDays();
        aAdapter<String> adapter = new aAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        adapter.setPicDays(filledInDays);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(adapter);
        spinnerDate.setSelection(list.size()-1);
        selectedDay = spinnerDate.getSelectedItemPosition()+1;
    }
    private void setFilledInDays(){
        Cursor c = database.sqlSelect(sqs.C_FILE_DAY, sqs.T_FILE, sqs.C_FILE_ID + "=" + albumNum);
        int size = c.getCount();
        for(int i=0; i<size; i++){
            c.moveToNext();
            filledInDays.set(c.getInt(0)-1,true);
        }
        filledInDays.add(true);
    }
    private void reloadAlbum(){
        loadAlbum();
        imageAdapter.clearData();
        imageAdapter.addAll(fileList);
        imageAdapter.addAllCD(changedDays);
        imageAdapter.notifyDataSetChanged();
    }
    private boolean isViewAll(){
        return today+1 == selectedDay;
    }
    private void loadAlbum(){
        if(executor.isShutdown())
            executor = Executors.newFixedThreadPool(10);
        imgs.clear();
        fileList.clear();
        filePath.clear();
        changedDays.clear();
        Cursor c = database.sqlSelect(sqs.C_FILE_LOCATION + "," +sqs.C_FILE_DAY, sqs.T_FILE,
                 sqs.C_FILE_ID + "=" + albumNum + " AND " +
                        (isViewAll() ? "1" : sqs.C_FILE_DAY + "=" + selectedDay)+" ORDER BY " + sqs.C_FILE_DAY);
        int size = c.getCount();
        for(int i=0; i<size; i++){
            c.moveToNext();
            String p = c.getString(0);
            changedDays.add(c.getInt(1));
            filePath.add(p);
            imgs.add(new imgCallable(p));

        }

        try {
            List<Future<Bitmap>>results;
            results= executor.invokeAll(imgs);

            executor.shutdown();
            findViewById(R.id.progressBar2).setVisibility(View.GONE);
            for(Future<Bitmap> result:results){
                fileList.add(result.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case 1000 :
                    if(!isViewAll()){
                        String fileLocation = files.getPathFromUri(this,data.getData());

                        if(database.getSelectRowNum("*",sqs.T_FILE,
                                sqs.C_FILE_ID + "=" + albumNum + " AND " +
                                        sqs.C_FILE_LOCATION+"='"+fileLocation+"'") == 0){
                            database.insertFile(albumNum,selectedDay,fileLocation);
                            filledInDays.set(selectedDay-1,true);
                            spinnerDate.invalidate();
                            reloadAlbum();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Selected image is already existing!",Toast.LENGTH_LONG).show();
                    }else
                        Toast.makeText(getApplicationContext(),"You have to select the day first!",Toast.LENGTH_LONG).show();

                    break;
                case 1001 :
                    String fileLoc = path[0];
                    database.insertFile(albumNum,today,fileLoc);
                    filledInDays.set(today-1,true);
                    spinnerDate.invalidate();
                    reloadAlbum();
                    break;

            }
        }
    }
    private void setSpinnerListener(){
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
                    }
                });
                selectedDay = i+1;
                reloadAlbum();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
