package com.celerysoft.imagepager.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import com.celerysoft.imagepager.util.ImageUtil;

import uk.co.senab.photoview.PhotoView;

/**
 * Simple image pager adapter
 * Created by Celery on 2015-11-18.
 */
public class SimpleImagePagerAdapter extends ImagePagerAdapter {

    private Context mContext;

    private int[] mImageResIds;
    public void setImageResIds(int[] imageResIds) {
        removeAllCollection();
        mImageResIds = imageResIds;
    }

    private String[] mImagePaths;
    public void setImagePaths(String[] imagePaths) {
        removeAllCollection();
        mImagePaths = imagePaths;
        mImageBitmaps = new Bitmap[imagePaths.length];
    }
    private Bitmap[] mImageBitmaps;

    private String[] mImageUris;
    public void setImageUris(String[] imageUris) {
        removeAllCollection();
        mImageUris = imageUris;
    }

    public SimpleImagePagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public PhotoView getItem(int position) {
        PhotoView photoView = new PhotoView(mContext);
        if (mImageResIds != null && mImageResIds.length > position) {
            photoView.setImageResource(mImageResIds[position]);
        } else if (mImagePaths != null && mImagePaths.length > position) {
            Bitmap bitmap;
            if (mImageBitmaps[position] != null) {
                bitmap = mImageBitmaps[position];
            } else {
                bitmap = ImageUtil.getBitmap(mContext, mImagePaths[position]);
                mImageBitmaps[position] = bitmap;
            }
            photoView.setImageBitmap(bitmap);
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

    private void removeAllCollection() {
        mImageResIds = null;
        mImagePaths = null;
        mImageBitmaps = null;
        mImageUris = null;
    }
}
