package com.celerysoft.imagepager.view.indicator;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.celerysoft.imagepager.R;
import com.celerysoft.imagepager.util.DensityUtil;

/**
 * Indicator that only display text.
 */
public class TextIndicator extends TextView implements Indicator {

    private int mImageCount;
    private int oldPosition = 0;

    public TextIndicator(Context context) {
        super(context);
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
        setText((position+1) + "/" + mImageCount);
        oldPosition = position;
    }

    @Override
    public void setImageCount(int imageCount) {
        mImageCount = imageCount;
        setText((oldPosition+1) + "/" + mImageCount);
    }
}
