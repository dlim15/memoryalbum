package com.example.dv.myalbum;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import java.util.concurrent.Callable;

/**
 * Created by DV on 11/15/2016.
 */

public class imgCallable implements Callable<Bitmap> {
    String path;
    final static int THUMB_SIZE = 150;
    public imgCallable(String p){
        path = p;
    }
    @Override
    public Bitmap call() throws Exception {
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path),THUMB_SIZE,THUMB_SIZE);
    }
}
