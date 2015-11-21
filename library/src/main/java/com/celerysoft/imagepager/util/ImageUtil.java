package com.celerysoft.imagepager.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;

/**
 * Created by Administrator on 2015-11-20.
 */
public class ImageUtil {

    private static final String TAG = ImageUtil.class.getSimpleName();

    /**
     * get bitmap by file absolute path
     * @param filePath file absolute path
     * @return bitmap
     */
    public static final Bitmap getBitmap(Context context, String filePath) {
        File f = new File(filePath);
        if (f != null) {
            if (!f.exists()) {
                Log.w(TAG, "No such file: " + filePath);
                return null;
            }
        }

        Bitmap bitmap = null;
        try {
            int targetWidth = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
            int targetHeight = context.getResources().getDisplayMetrics().heightPixels;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            int photoWidth = options.outWidth;
            int photoHeight = options.outHeight;

            int scaleFactor = Math.min(photoWidth/targetWidth, photoHeight/targetHeight);

            options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                    (double) options.outWidth / 1024f,
                    (double) options.outHeight / 1024f)));
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

        return bitmap;
    }
}
