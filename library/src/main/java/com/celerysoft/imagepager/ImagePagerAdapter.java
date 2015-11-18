package com.celerysoft.imagepager;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Celery on 2015-11-18.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private final String TAG = this.getClass().getSimpleName();
    private final boolean DEBUG = BuildConfig.DEBUG;

    private ArrayList<PhotoView> mImageViews;
    private PhotoView mCurrentPrimaryItem = null;


    public ImagePagerAdapter() {

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mImageViews.size() > position) {
            ImageView v = mImageViews.get(position);
            if (v != null) {
                return v;
            }
        }
        //TODO
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mImageViews.remove(object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        PhotoView photoView = (PhotoView) object;
        if (photoView != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setVisibility(View.INVISIBLE);
            }

            if (photoView != null) {
                photoView.setVisibility(View.VISIBLE);
            }

            mCurrentPrimaryItem = photoView;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return mImageViews.indexOf(object);
    }

    @Override
    public int getCount() {
        return mImageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
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
}
