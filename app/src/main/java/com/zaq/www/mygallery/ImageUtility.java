package com.zaq.www.mygallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageUtility {
    private static final String TAG = "CREATING BITMAP";

    static public InputStream getInputStreamFromUri(Uri uri, Context context) {
        try {
            File imageFile = new File(uri.getPath());
            return new FileInputStream(imageFile);
        } catch (FileNotFoundException e) {
            Log.w(TAG, "getInputStreamFromUri: exception while opening input stream from file. try from content resolver");
        }

        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "getInputStreamFromUri: exception while opening input stream from content resolver. returning");
        }

        return null;
    }

    static public byte[] compressImage(Bitmap bitmap) {
        final int maxSize = 600;
        int outWidth;
        int outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();
        if (inWidth > inHeight) {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        return baos.toByteArray();
    }
}
