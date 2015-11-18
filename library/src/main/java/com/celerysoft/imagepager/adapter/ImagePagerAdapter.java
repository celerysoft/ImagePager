package com.celerysoft.imagepager.adapter;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Celery on 2015-11-18.
 */
public class ImagePagerAdapter extends ImagePagerAdapterBase {

    ArrayList<PhotoView> mPhotoViews;
    public void setImageViews(ArrayList<PhotoView> ImageViews) {
        mPhotoViews = ImageViews;
    }

    @Override
    public PhotoView getItem(int position) {
        PhotoView photoView = null;
        if (mPhotoViews != null && mPhotoViews.size() > position) {
            photoView = mPhotoViews.get(position);
        }
        return photoView;
    }

    @Override
    public int getCount() {
        if (mPhotoViews != null) {
            return mPhotoViews.size();
        }
        return 0;
    }
}
