package com.example.dv.myalbum;

import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailedImageActivity extends AppCompatActivity {
    private ImageView imgDetail;
    private ArrayList<String> fileList;
    private int curPos;
    private int maxPos;
    private SomeGlobalVar vars;
    private int rotate;
    private Map<Integer, Integer> orientationInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_image);
        init();
        getList();
        setImg();
    }
    private void init(){
        vars = new SomeGlobalVar();
        orientationInfo = new HashMap<>();
        orientationInfo.put(0, 0);
        orientationInfo.put(ExifInterface.ORIENTATION_NORMAL, 0);
        orientationInfo.put(ExifInterface.ORIENTATION_ROTATE_90, 90);
        orientationInfo.put(ExifInterface.ORIENTATION_ROTATE_180, 180);
        orientationInfo.put(ExifInterface.ORIENTATION_ROTATE_270, 270);
        imgDetail = (ImageView)findViewById(R.id.imgDetail);
        btnPreClicked((Button)findViewById(R.id.btnPre));
        btnNextClicked((Button)findViewById(R.id.btnNext));
    }
    private void getList(){
        Intent intent = getIntent();
        curPos = intent.getIntExtra(vars.AT,-1);
        fileList = intent.getStringArrayListExtra(vars.PATH);
        setTitle(intent.getStringExtra(vars.ALBUM_NAME));
        maxPos = fileList.size()-1;
    }
    private void setImg(){
        rotate = 0;
        checkImageRoateInfo();
        imgDetail.setRotation(rotate);
        imgDetail.setImageURI(Uri.parse(fileList.get(curPos)));
    }
    private void checkImageRoateInfo(){

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(
                    fileList.get(curPos));
        } catch (IOException e) {
            e.printStackTrace();
        }
        rotate = orientationInfo.get(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL));

    }

    private void btnPreClicked(Button b){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curPos > 0){
                    curPos--;
                    setImg();
                }

            }
        });
    }
    private void btnNextClicked(Button b){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curPos < maxPos){
                    curPos++;
                    setImg();
                }
            }
        });
    }
}
