package com.celerysoft.imagepager.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.celerysoft.imagepager.R;
import com.celerysoft.imagepager.util.ImageLoader;
import com.celerysoft.imagepager.util.ImageUtil;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Simple image pager adapter
 * Created by Celery on 2015-11-18.
 */
public class SimpleImagePagerAdapter extends ImagePagerAdapter {
    private static final String TAG = SimpleImagePagerAdapter.class.getSimpleName();

    private Context mContext;

    private ImageLoader mImageLoader;

    private ArrayList<Image> mImages;
    public ArrayList<Image> getImages() {
        return mImages;
    }
    public void setImages(ArrayList<Image> images) {
        mImages = images;
    }
    public void addImage(Image image) {
        mImages.add(image);
    }
    public void addImage(int index, Image image) {
        mImages.add(index, image);
    }


    public SimpleImagePagerAdapter(Context context) {
        mContext = context;
        mImageLoader = ImageLoader.build(context);
    }

    @Override
    public PhotoView getItem(int position) {
        PhotoView photoView = null;

        if (mImages != null && mImages.size() > position) {
            photoView = new PhotoView(mContext);
            mImageLoader.bindImageView(mImages.get(position).getUrl(), photoView);
        }

        return photoView;
    }

    @Override
    public int getCount() {
        if (mImages != null) {
            return mImages.size();
        }
        return 0;
    }

    @Override
    public boolean removeImage(int imagePosition) {
        boolean succeeded = true;
        try {
            if (mImages != null) {
                mImages.remove(imagePosition);
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

    public static class Image {
        public Image() {}

        int mImageResId = -1;
        String mImagePath = null;
        String mImageUrl = null;

        public void setImageResId(int imageResId) {
            mImageResId = imageResId;
            mImagePath = null;
            mImageUrl = null;
        }

        public void setImagePath(String imagePath) {
            mImageResId = -1;
            mImagePath = imagePath;
            mImageUrl = null;
        }

        public void setImageUrl(String imageUrl) {
            mImageResId = -1;
            mImageUrl = null;
            mImageUrl = imageUrl;
        }

        public String getUrl() {
            if(mImageResId != -1) {
                return Integer.toString(mImageResId);
            }
            if (mImagePath != null) {
                return mImagePath;
            }
            if (mImageUrl != null) {
                return mImageUrl;
            }
            Log.w(TAG, "this image has no url");
            return null;
        }
    }
}
