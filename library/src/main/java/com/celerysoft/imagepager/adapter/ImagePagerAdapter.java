package com.celerysoft.imagepager.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Celery on 2015-11-18.
 */
public class ImagePagerAdapter extends ImagePagerAdapterBase {

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
    }


    public ImagePagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public PhotoView getItem(int position) {
        PhotoView photoView = new PhotoView(mContext);
        if (mImageResIds != null && mImageResIds.length > position) {
            photoView.setImageResource(mImageResIds[position]);
        } else if (mImagePaths != null && mImagePaths.length > position) {

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
