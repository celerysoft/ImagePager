package com.celerysoft.imagepager.view.indicator;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.celerysoft.imagepager.R;
import com.celerysoft.imagepager.util.DensityUtil;

/**
 * Indicator that only display text.
 */
public class TextIndicator extends TextView implements Indicator {
    private final String TAG = TextIndicator.class.getSimpleName();

    private Context mContext;

    private int mImageCount;
    private int mCurrentImagePosition = 0;

    public TextIndicator(Context context) {
        super(context);

        mContext = context;

        setGravity(Gravity.CENTER);
        setTextSize(DensityUtil.sp2px(context, 16));

        setBackgroundResource(R.drawable.textindicator_background);
        try {
            setTextColor(context.getResources().getColor(R.color.text_indicator_text_color, null));
        } catch (NoSuchMethodError e) {
            setTextColor(context.getResources().getColor(R.color.text_indicator_text_color));
        }

    }

    @Override
    public void onPageSelected(int position) {
        String text = (position + 1) + "/" + mImageCount;
        setText(text);
        mCurrentImagePosition = position;
    }

    @Override
    public void onPageDeleted() {
        mImageCount -= 1;
        mCurrentImagePosition = mCurrentImagePosition < mImageCount ?
                mCurrentImagePosition : mImageCount - 1;
        String text;
        if (mImageCount <= 0) {
            setTextSize(DensityUtil.sp2px(mContext, 24));
            text = mContext.getString(R.string.no_images);
            if (mImageCount < 0) {
                Log.e(TAG, "image count less than 0, it could not be happened!");
            }
        } else {
            setTextSize(DensityUtil.sp2px(mContext, 16));
            text = (mCurrentImagePosition + 1) + "/" + mImageCount;
        }
        setText(text);
    }

    @Override
    public void onPageAdapterChanged(int imageCount) {
        mImageCount = imageCount;
        mCurrentImagePosition = 0;

        String text = "1/" + mImageCount;
        setText(text);
    }
}
