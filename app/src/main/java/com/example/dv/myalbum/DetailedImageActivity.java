package com.example.dv.myalbum;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailedImageActivity extends AppCompatActivity {
    private ImageView imgDetail;
    private ArrayList<String> fileList;
    private ArrayList<Uri> uriList;
    private ArrayList<Bitmap> fileInBit;
    private int curPos;
    private int maxPos;
    private SomeGlobalVar vars;
    private int rotate;
    private Map<Integer, Integer> orientationInfo;
    private int width,height;
    private float sX,sY,eX,eY;
    private boolean isDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_image);
        init();
        getScreenWidthAndHeight();
        getList();
        setUriList();
        setImg();
//        rotateCheck();

    }
    private void getScreenWidthAndHeight(){
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;
        height = size.y;

    }
    private void init(){
        vars = new SomeGlobalVar();
        uriList = new ArrayList<Uri>();
        fileInBit = new ArrayList<>();
        orientationInfo = new HashMap<>();
        orientationInfo.put(0, 0);
        orientationInfo.put(ExifInterface.ORIENTATION_NORMAL, 0);
        orientationInfo.put(ExifInterface.ORIENTATION_ROTATE_90, 90);
        orientationInfo.put(ExifInterface.ORIENTATION_ROTATE_180, 180);
        orientationInfo.put(ExifInterface.ORIENTATION_ROTATE_270, 270);
        imgDetail = (ImageView)findViewById(R.id.imgDetail);
        picEvent(imgDetail);
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
        checkImageRotateInfo();
//        imgDetail.setRotation(rotate);
//        if(isDone)
//            imgDetail.setImageBitmap(fileInBit.get(curPos));
//        else
            imgDetail.setImageURI(uriList.get(curPos));

    }
//    private void rotateCheck(){
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                int i;
//                for(i=0; i<fileList.size(); i++){
//                    ExifInterface exif = null;
//                    try {
//                        exif = new ExifInterface(
//                                fileList.get(i));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    orientationInfo.get(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL));
//                    Bitmap b = BitmapFactory.decodeFile(fileList.get(i));
//                    b = Bitmap.createScaledBitmap(b,width,height,true);
//
//                    Matrix matrix = new Matrix();
//                    matrix.postRotate(orientationInfo.get(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL)));
//
//                    fileInBit.add(Bitmap.createBitmap(b,0,0,b.getWidth(),b.getHeight(),matrix,true));
//
//                }
//                isDone = true;
//            }
//        };
//        Thread a = new Thread(runnable);
//        a.start();
//    }
    private void setUriList(){
        for(String u:fileList){
            uriList.add(Uri.parse(u));
        }
    }
    private void checkImageRotateInfo(){

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(
                    fileList.get(curPos));
        } catch (IOException e) {
            e.printStackTrace();
        }
        rotate = orientationInfo.get(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL));

    }
    private void picEvent(final ImageView img){
        img.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                    view.startDrag(ClipData.newPlainText("",""), new View.DragShadowBuilder(view), view, 0);
//                    view.setVisibility(View.INVISIBLE);
                    return true;
                }
                return false;
            }
        });
        img.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch(dragEvent.getAction()){
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        sX = dragEvent.getX();
                        sY = dragEvent.getY();
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        eX = dragEvent.getX();
                        eY = dragEvent.getY();
                        picSlideCheck(sX,eX);
                        break;
                    case DragEvent.ACTION_DROP:
                        eX = dragEvent.getX();
                        eY = dragEvent.getY();
                        picSlideCheck(sX,eX);
                        break;

                }
                return true;
            }
        });
    }
    private void picSlideCheck(float s, float e){
        float result = s-e;
        if(result <  0)
            moveToPrePic();
        else if(result > 0)
            moveToNextPic();

    }
    private void moveToNextPic(){
        if(curPos<fileList.size()-1){
            curPos++;
            setImg();
        }

    }
    private void moveToPrePic(){
        if(curPos > 0){
            curPos--;
            setImg();
        }

    }

}
