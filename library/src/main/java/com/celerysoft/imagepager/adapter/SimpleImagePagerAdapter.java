package com.celerysoft.imagepager.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.celerysoft.imagepager.util.ImageUtil;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Celery on 2015-11-18.
 */
public class SimpleImagePagerAdapter extends ImagePagerAdapter {

    private Context mContext;

    private int[] mImageResIds;
    public void setImageResIds(int[] imageResIds) {
        mImageResIds = imageResIds;
        mImagePaths = null;
    }

    private String[] mImagePaths;
    public void setImagePaths(String[] imagePaths) {
        mImageResIds = null;
        mImagePaths = imagePaths;
        mImageDrawables = new Drawable[imagePaths.length];
    }
    private Drawable[] mImageDrawables;

    public SimpleImagePagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public PhotoView getItem(int position) {
        PhotoView photoView = new PhotoView(mContext);
        if (mImageResIds != null && mImageResIds.length > position) {
            photoView.setImageResource(mImageResIds[position]);
        } else if (mImagePaths != null && mImagePaths.length > position) {
            Drawable drawable;
            if (mImageDrawables[position] != null) {
                drawable = mImageDrawables[position];
            } else {
                Bitmap bitmap = ImageUtil.getBitmap(mContext, mImagePaths[position]);
                drawable = new BitmapDrawable(mContext.getResources(), bitmap);
                //Drawable drawable = new BitmapDrawable(bitmap);
            }
            photoView.setImageDrawable(drawable);
        }
        return photoView;
    }

    @Override
    public int getCount() {
        if (mImageResIds != null) {
            return mImageResIds.length;
        } else if (mImagePaths != null) {
            return mImagePaths.length;
        }
        return 0;
    }
}
