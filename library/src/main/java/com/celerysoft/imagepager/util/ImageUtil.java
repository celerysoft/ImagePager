package com.celerysoft.imagepager.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2015-11-20.
 */
public class ImageUtil {
    /**
     * get bitmap by file absolute path
     * @param fileName
     * @return
     */
    public static final Bitmap getBitmap(Context context, String fileName) {
        Bitmap bitmap = null;
        try {
            int targetWidth = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
            int targetHeight = context.getResources().getDisplayMetrics().heightPixels;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);

            int photoWidth = options.outWidth;
            int photoHeight = options.outHeight;

            int scaleFactor = Math.min(photoWidth/targetWidth, photoHeight/targetHeight);

            options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                    (double) options.outWidth / 1024f,
                    (double) options.outHeight / 1024f)));
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(fileName, options);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

        return bitmap;
    }
}
