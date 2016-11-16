package com.example.dv.myalbum;

import android.app.Activity;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

/**
 * Created by DV on 11/4/2016.
 */

public class FileInputOutput {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public File getFileDirection(int albumNum){
        File folder = new File(Environment.getExternalStorageDirectory() + "/albumApp/"+ +albumNum);

        if(!folder.exists()){
            folder.mkdir();
        }
        File imgFile = new File(folder, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".jpg");
        return imgFile;
    }

    public String getPathFromUri(Activity activity, Uri uri){
        String path = "";
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri,projection,null,null,null);
        if(cursor.moveToFirst()){
            int col_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path = cursor.getString(col_index);
        }
        cursor.close();
        return path;
    }
//    public Bitmap getImageBitmap(byte[] blob){
//        return BitmapFactory.decodeByteArray(blob,0,blob.length);
//    }
//    public byte[] toBlobFormat(Bitmap b){
//        Bitmap photo = b;
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        photo.compress(Bitmap.CompressFormat.JPEG,100,stream);
//        return stream.toByteArray();
//    }

}
