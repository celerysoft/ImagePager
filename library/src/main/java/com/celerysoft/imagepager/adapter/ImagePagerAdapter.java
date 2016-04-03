package com.celerysoft.imagepager.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.celerysoft.imagepager.BuildConfig;
import com.celerysoft.imagepager.ImagePager;
import com.celerysoft.imagepager.view.indicator.Indicator;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Base adapter of ImagePager
 * Created by Celery on 2015-11-18.
 */
public abstract class ImagePagerAdapter extends PagerAdapter {

    @SuppressWarnings("unused")
    private final String TAG = "ImagePagerAdapter";
    @SuppressWarnings("unused")
    private final boolean DEBUG = BuildConfig.DEBUG;

    protected boolean mIsRemovedImage = false;
    private ArrayList<PhotoView> mImageViews = new ArrayList<>();
    private PhotoView mCurrentPrimaryItem = null;

    /**
     * {@link Indicator} of {@link ImagePager}, when a {@link ImagePager} call {@link ImagePager#setAdapter},
     * mIndicator is assigned as the {@link Indicator} of the {@link ImagePager}.
     **/
    private Indicator mIndicator;
    public void setIndicator(Indicator indicator) {
        mIndicator = indicator;
    }

    private ImagePager.OnImageClickListener mOnPhotoTapListener;
    public void setOnImageClickListener(ImagePager.OnImageClickListener onImageClickListener) {
        mOnPhotoTapListener = onImageClickListener;
    }

    private ImagePager.OnPageClickListener mOnPageClickListener;
    public void setOnPageClickListenerListener(ImagePager.OnPageClickListener onPageClickListener) {
        mOnPageClickListener = onPageClickListener;
    }

    public ImagePagerAdapter() {
        super();
    }

    public abstract PhotoView getItem(int position);

    /**
     * remove image from adapter
     * @param imagePosition position of image
     * @return true if remove successfully, false if index out of bound etc.
     */
    public abstract boolean removeImage(int imagePosition);

    @Override
    public abstract int getCount();

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mImageViews.size() > position) {
            PhotoView v = mImageViews.get(position);
            if (v != null) {
                container.addView(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return v;
            }
        }

        PhotoView imageView = getItem(position);
        while (mImageViews.size() <= position) {
            mImageViews.add(null);
        }
        imageView.setVisibility(View.VISIBLE);
        mImageViews.set(position, imageView);
        if (mOnPageClickListener != null) {
            imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    mOnPageClickListener.onPageClick();
                }
            });
        }
        if (mOnPhotoTapListener != null) {
            imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    mOnPhotoTapListener.onImageClick();
                }
            });
        }

        container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        PhotoView imageView = (PhotoView) object;
        if (imageView != null) {
            if (mImageViews.size() > 0) {
                int index = mImageViews.indexOf(imageView);
                mImageViews.set(index, null);
            }
        }
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
//        PhotoView imageView = (PhotoView) object;
//        imageView.
//
//        if (imageView != mCurrentPrimaryItem) {
//            if (mCurrentPrimaryItem != null) {
//                mCurrentPrimaryItem.setVisibility(View.INVISIBLE);
//            }
//
//            if (imageView != null) {
//                imageView.setVisibility(View.VISIBLE);
//            }
//        }
//
//        mCurrentPrimaryItem = imageView;
    }



    @Override
    public int getItemPosition(Object object) {
        PhotoView imageView = (PhotoView) object;
        int position = mImageViews.indexOf(imageView);
        if (position == -1) {
            return POSITION_NONE;
        } else {
            return position;
        }
    }

    @Override
    public Parcelable saveState() {
        //TODO
        return super.saveState();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        //TODO
        super.restoreState(state, loader);
    }

    @Override
    public void notifyDataSetChanged() {
        if (mIsRemovedImage) {
            mIndicator.onPageDeleted();
        }
        mIsRemovedImage = false;

        mImageViews = new ArrayList<>();

        super.notifyDataSetChanged();
    }

}
