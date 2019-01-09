package com.celerysoft.imagepager.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.widget.ImageView;

import com.celerysoft.imagepager.util.ImageLoader;

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

    private ImageView.ScaleType mScaleType;

    public ImageView.ScaleType getScaleType() {
        return mScaleType;
    }

    /**
     * <p> Set a ScaleType for the ImageView in ImagePager.
     * <p> Please note that call this method before {@link com.celerysoft.imagepager.ImagePager#setAdapter(ImagePagerAdapter)}
    * @param scaleType
    */
    public void setScaleType(ImageView.ScaleType scaleType) {
        mScaleType = scaleType;
    }

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

    public SimpleImagePagerAdapter(Context context, ImageView.ScaleType scaleType) {
        this(context);

        mScaleType = scaleType;
    }

    @Override
    public PhotoView getItem(int position) {
        PhotoView photoView = null;

        if (mImages != null && mImages.size() > position) {
            photoView = new PhotoView(mContext);

            if (mScaleType != null) {
                photoView.setScaleType(mScaleType);
            }

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
        } catch (Exception e) {
            Log.w(TAG, "remove image failed, image position: " + imagePosition
                    + ", image count: " + getCount());
            Log.w(TAG, "detail: " + e.getMessage());
            succeeded = false;
        } finally {
            if (succeeded) {
                mIsRemovedImage = true;
                notifyDataSetChanged();
            }
        }

        return succeeded;
    }

    @Override
    public void setPlaceholder(@DrawableRes int placeholderResInt) {
        mImageLoader.setPlaceholder(placeholderResInt);
    }

    @Override
    public void resetPlaceholder() {
        mImageLoader.resetPlaceholder();
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
