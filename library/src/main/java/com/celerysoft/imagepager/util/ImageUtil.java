package com.celerysoft.imagepager.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;

/**
 * Created by Administrator on 2015-11-20.
 * ImageUtil
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

            int inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);

            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int targetWidth, int targetHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > targetHeight || width > targetWidth) {
            while ((height / inSampleSize) > targetHeight && (width / inSampleSize) > targetWidth) {
                //设置inSampleSize为2的幂是因为解码器最终还是会对非2的幂的数进行向下处理，获取到最靠近2的幂的数。
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
