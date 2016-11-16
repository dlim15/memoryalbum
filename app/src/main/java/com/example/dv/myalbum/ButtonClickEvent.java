package com.example.dv.myalbum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


/**
 * Created by DV on 10/22/2016.
 */

public class ButtonClickEvent {
    private Context context;
    private Activity activity;
    private FileInputOutput files;
    private SomeGlobalVar vars;
    public ButtonClickEvent(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        init();
    }
    private void init(){
        files = new FileInputOutput();
        vars = new SomeGlobalVar();
    }
    public void btnNewClicked(Button b){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,CreateNewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });
    }
    public void btnSelectDateClicked(Button b, final DateRelated date){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                date.openDatePicker();
            }
        });
    }
    public void btnLoadClicked(Button b){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAlbum(0);
            }

        });
    }
    public void btnTakeNewClicked(Button b){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAlbum(1);
            }
        });
    }
    private void loadAlbum(int num){
        Intent selectAlbum = new Intent(context, SelectAlbumActivity.class);
        selectAlbum.putExtra(vars.WHICH_LOAD_CALLED,num);
        if(num ==1){
            activity.startActivityForResult(selectAlbum, vars.SELECT_TO_MAIN_REQUEST);
        }else
            activity.startActivity(selectAlbum);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void runCamera(int albumNum, String[] path){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(files.getFileDirection(albumNum));
        path[0] = uri.getPath();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(cameraIntent,vars.CAMERA_REQUEST);
    }
    public void btnTakePicClicked(Button b, final int albumNum, final String[] path){
        b.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                runCamera(albumNum, path);
            }
        });
    }
    private void finishActivity(){
        activity.finish();
    }
    public void btnNewCancelClicked(Button b){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
    }
    public void btnSelectBackClicked(Button b){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
    }
    public void btnSelectSelectClicked(Button b,final RadioGroup rg, final int from, final String[] path, final int[] alb){
        b.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                int albumNum = rg.getCheckedRadioButtonId();
                if(from == 0)
                    openAlbum(albumNum);
                else{
                    alb[0] = albumNum;
                    runCamera(albumNum, path);
                }

            }
        });
    }
    public void btnNewSubmitClicked(Button b, final DateRelated date, final DatabaseDb database){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.insertAlbum(((EditText)activity.findViewById(R.id.txtAlbumName)).getText().toString(), date.getDate());

                openAlbum(database.selectAlbumNo());
            }
        });
    }
    private void openAlbum(int albumNum){
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra(vars.INTENT_ALBUM_NO,albumNum);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
    public void btnBtnFromGallery(Button b){
        b.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra("return-data", true);
                activity.startActivityForResult(photoPickerIntent, vars.LOAD_FROM_GALLERY_REQUEST);
            }
        });
    }
}
