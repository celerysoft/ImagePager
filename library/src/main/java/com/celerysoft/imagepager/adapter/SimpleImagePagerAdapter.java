package com.celerysoft.imagepager.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

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

    private ArrayList<Bitmap> mImageBitmaps;

    private ArrayList<Image> mImages;
    public ArrayList<Image> getImages() {
        return mImages;
    }
    public void setImages(ArrayList<Image> images) {
        removeAllCollection();
        mImages = images;

        createImageBitmaps(images.size());
    }
    public void addImage(Image image) {
        mImages.add(image);

        mImageBitmaps.add(null);
    }
    public void addImage(int index, Image image) {
        mImages.add(index, image);

        mImageBitmaps.add(index, null);
    }


    @Deprecated
    private ArrayList<Integer> mImageResIds;
    /**
     *
     * @param imageResIds ArrayList of image resource ids
     * @deprecated use {@link #setImages(ArrayList)} to instead of.
     */
    @Deprecated
    public void setImageResIds(ArrayList<Integer> imageResIds) {
        removeAllCollection();
        mImageResIds = imageResIds;

        createImageBitmaps(imageResIds.size());
    }

    @Deprecated
    private ArrayList<String> mImagePaths;
    /**
     *
     * @param imagePaths ArrayList of image file paths
     * @deprecated use {@link #setImages(ArrayList)} to instead of.
     */
    @Deprecated
    public void setImagePaths(ArrayList<String> imagePaths) {
        removeAllCollection();
        mImagePaths = imagePaths;

        createImageBitmaps(imagePaths.size());
    }

    @Deprecated
    private ArrayList<String> mImageUrls;
    /**
     *
     * @param imageUrls ArrayList of internet image file urls
     * @deprecated use {@link #setImages(ArrayList)} to instead of.
     */
    @Deprecated
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
            Bitmap bitmap;
            if (mImageBitmaps.get(position) != null) {
                bitmap = mImageBitmaps.get(position);
            } else {
                // TODO open a new thread to handle this
                bitmap = ImageUtil.getBitmap(mContext, mImageResIds.get(position));
                mImageBitmaps.set(position, bitmap);
            }
            photoView.setImageBitmap(bitmap);
        } else if (mImagePaths != null && mImagePaths.size() > position) {
            Bitmap bitmap;
            if (mImageBitmaps.get(position) != null) {
                bitmap = mImageBitmaps.get(position);
            } else {
                // TODO open a new thread to handle this
                bitmap = ImageUtil.getBitmap(mContext, mImagePaths.get(position));
                mImageBitmaps.set(position, bitmap);
            }
            photoView.setImageBitmap(bitmap);
        } else if (mImageUrls != null && mImageUrls.size() > position) {
            // TODO handle image from internet
        } else if (mImages != null && mImages.size() > position) {
            Bitmap bitmap;
            if (mImageBitmaps.get(position) != null) {
                bitmap = mImageBitmaps.get(position);
            } else {
                // TODO open a new thread to handle this
                bitmap = mImages.get(position).getBitmap();
                mImageBitmaps.set(position, bitmap);
            }
            photoView.setImageBitmap(bitmap);
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
        } else if (mImages != null) {
            return mImages.size();
        }
        return 0;
    }

    @Override
    public boolean removeImage(int imagePosition) {
        boolean succeeded = true;
        try {
            if (mImageResIds != null) {
                mImageResIds.remove(imagePosition);
                mImageBitmaps.remove(imagePosition);
            } else if (mImagePaths != null) {
                mImagePaths.remove(imagePosition);
                mImageBitmaps.remove(imagePosition);
            } else if (mImageUrls != null) {
                mImageUrls.remove(imagePosition);
            } else if (mImages != null) {
                mImages.remove(imagePosition);
                mImageBitmaps.remove(imagePosition);
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

    private void createImageBitmaps(int imageCount) {
        mImageBitmaps = new ArrayList<>();
        for (int i = 0; i < imageCount; ++i) {
            mImageBitmaps.add(null);
        }
    }

    public static class Image {
        private Context mContext;

        public Image(Context context) {
            mContext = context;
        }

        int mImageResId = -1;
        String mImagePath = null;
        String mImageUrl = null;

        public int getImageResId() {
            return mImageResId;
        }

        public void setImageResId(int imageResId) {
            mImageResId = imageResId;
            mImagePath = null;
            mImageUrl = null;
        }

        public String getImagePath() {
            return mImagePath;
        }

        public void setImagePath(String imagePath) {
            mImageResId = -1;
            mImagePath = imagePath;
            mImageUrl = null;
        }

        public String getImageUrl() {
            return mImageUrl;
        }

        public void setImageUrl(String imageUrl) {
            mImageResId = -1;
            mImageUrl = null;
            mImageUrl = imageUrl;
        }

        public Bitmap getBitmap() {
            Bitmap bitmap = null;

            if (mImageResId != -1) {
                bitmap = ImageUtil.getBitmap(mContext, mImageResId);
            } else if (mImagePath != null) {
                bitmap = ImageUtil.getBitmap(mContext, mImagePath);
            } else if (mImageUrl != null) {
                // TODO handle image from internet
            }

            return bitmap;
        }
    }
}
