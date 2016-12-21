package com.celerysoft.imagepager.view.indicator;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.celerysoft.imagepager.R;
import com.celerysoft.imagepager.util.DensityUtil;

/**
 * Created by admin on 16/12/21.
 * Indicator with dots.
 */

public class DotIndicator extends LinearLayout implements Indicator {
    private static final String TAG = "DotIndicator";

    private int mImageCount;
    private int mCurrentImagePosition;

    private int mSelectedImageResourceId = R.drawable.ic_selected;
    private int mUnselectedImageResourceId = R.drawable.ic_unselected;

    public DotIndicator(Context context) {
        super(context);

        setOrientation(HORIZONTAL);
        setBackgroundResource(R.drawable.indicator_background);
    }

    public void setSelectedImageResource(int resId) {
        mSelectedImageResourceId = resId;
    }

    public void setUnselectedImageResource(int resId) {
        mUnselectedImageResourceId = resId;
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentImagePosition = position;
        updateUi(position);
    }

    @Override
    public void onPageDeleted() {
        removeViewAt(mImageCount - 1);
        mImageCount -= 1;
        mCurrentImagePosition = mCurrentImagePosition < mImageCount ? mCurrentImagePosition : mImageCount - 1;
        if (mImageCount <= 0) {
            TextView textView = new TextView(getContext());
            textView.setTextSize(20);
            String text = getContext().getString(R.string.no_images);
            textView.setText(text);
            addView(textView);
            if (mImageCount < 0) {
                Log.e(TAG, "image count less than 0, it could not be happened!");
            }
        } else {
            updateUi(mCurrentImagePosition);
        }
    }

    @Override
    public void onPageAdapterChanged(int imageCount) {
        mImageCount = imageCount;
        mCurrentImagePosition = 0;

        removeAllViews();
        for (int i = 0; i < mImageCount; ++i) {
            View view = new View(getContext());
            if (i == 0) {
                view.setBackgroundResource(mSelectedImageResourceId);
            } else {
                view.setBackgroundResource(mUnselectedImageResourceId);
            }
            addView(view);
            view.getLayoutParams().width = DensityUtil.dp2px(getContext(), 12);
            view.getLayoutParams().height = DensityUtil.dp2px(getContext(), 12);
            view.requestLayout();
        }

//        updateUi(0);
    }

    private void updateUi(int selectedImagePosition) {
        int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View v = getChildAt(i);
            if (i == selectedImagePosition) {
                if (mSelectedImageResourceId != -1) {
                    v.setBackgroundResource(mSelectedImageResourceId);
                }
            } else {
                if (mUnselectedImageResourceId != -1) {
                    v.setBackgroundResource(mUnselectedImageResourceId);
                }
            }
        }
    }
}
