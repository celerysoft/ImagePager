package com.celerysoft.imagepager.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.celerysoft.imagepager.util.ImageUtil;

import java.util.ArrayList;
import java.util.Arrays;

import uk.co.senab.photoview.PhotoView;

/**
 * Simple image pager adapter
 * Created by Celery on 2015-11-18.
 */
public class SimpleImagePagerAdapter extends ImagePagerAdapter {
    private static final String TAG = SimpleImagePagerAdapter.class.getSimpleName();

    private Context mContext;

    private ArrayList<Integer> mImageResIds;
    public void setImageResIds(ArrayList<Integer> imageResIds) {
        removeAllCollection();
        mImageResIds = imageResIds;
    }

    private ArrayList<String> mImagePaths;
    public void setImagePaths(ArrayList<String> imagePaths) {
        removeAllCollection();
        mImagePaths = imagePaths;
        mImageBitmaps = new ArrayList<>();
        int imageCount = mImagePaths.size();
        for (int i = 0; i < imageCount; ++i) {
            mImageBitmaps.add(null);
        }
    }
    private ArrayList<Bitmap> mImageBitmaps;

    private ArrayList<String> mImageUrls;
    public void setImageUrls(ArrayList<String> imageUrls) {
        removeAllCollection();
        mImageUrls = imageUrls;
    }

    public SimpleImagePagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public PhotoView getItem(int position) {
        PhotoView photoView = new PhotoView(mContext);
        if (mImageResIds != null && mImageResIds.size() > position) {
            photoView.setImageResource(mImageResIds.get(position));
        } else if (mImagePaths != null && mImagePaths.size() > position) {
            Bitmap bitmap;
            if (mImageBitmaps.get(position) != null) {
                bitmap = mImageBitmaps.get(position);
            } else {
                bitmap = ImageUtil.getBitmap(mContext, mImagePaths.get(position));
                mImageBitmaps.set(position, bitmap);
            }
            photoView.setImageBitmap(bitmap);
        } else if (mImageUrls != null && mImageUrls.size() > position) {
            // TODO handle image from internet
        }
        return photoView;
    }

    @Override
    public int getCount() {
        if (mImageResIds != null) {
            return mImageResIds.size();
        } else if (mImagePaths != null) {
            return mImagePaths.size();
        } else if (mImageUrls != null) {
            return mImageUrls.size();
        }
        return 0;
    }

    @Override
    public boolean removeImage(int imagePosition) {
        boolean succeeded = true;
        try {
            if (mImageResIds != null) {
                mImageResIds.remove(imagePosition);
            } else if (mImagePaths != null) {
                mImagePaths.remove(imagePosition);
                mImageBitmaps.remove(imagePosition);
            } else if (mImageBitmaps != null) {
                mImageBitmaps.remove(imagePosition);
            } else if (mImageUrls != null) {
                mImageUrls.remove(imagePosition);
            } else {
                succeeded = false;
                Log.w(TAG, "remove image failed, no collection to handle removing operation.");
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            Log.w(TAG, "remove image failed, image position: " + imagePosition
                    + ", image count: " + getCount());
            Log.w(TAG, "detail: " + e.getMessage());
            succeeded = false;
        }

        return succeeded;
    }

    private void removeAllCollection() {
        mImageResIds = null;
        mImagePaths = null;
        mImageBitmaps = null;
        mImageUrls = null;
    }
}
