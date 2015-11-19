package com.celerysoft.imagepager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.celerysoft.imagepager.view.ActionBar;
import com.celerysoft.imagepager.view.Indicators;
import com.celerysoft.imagepager.view.Pager;

/**
 * ImagePager, display images, contain a actionbar, a indicator and a pager.
 * Created by Celery on 2015-11-19.
 */
public class ImagePager extends ViewGroup {
    // const
    private final String TAG = this.getClass().getSimpleName();
    private final int CHILD_COUNT = 3;

    // field
    private Context mContext;


    public ImagePager(Context context) {
        super(context);
        initImagePager();
    }

    public ImagePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initImagePager();
    }

    private void initImagePager() {
        mContext = getContext();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int measureWidth;
        int measureHeight;

        int childCount = getChildCount();
        if (childCount != CHILD_COUNT) {
            Log.e(TAG, "wtf, child count must be 3!");
        }

        int childWidth = 0;
        int childHeight = 0;
        //MarginLayoutParams params = null;

        for (int i = 0; i < childCount; ++i) {
            View childView = getChildAt(0);

            if (childView instanceof Pager) {
                childWidth = childView.getMeasuredWidth();
                childHeight = childView.getMeasuredHeight();
            }
        }

        measureWidth = childWidth;
        measureHeight = childHeight;

        // MeasureSpec.AT_MOST means WRAP_CONTENT
        measureWidth = widthMode == MeasureSpec.AT_MOST ? measureWidth : widthSize;
        measureHeight = heightMode == MeasureSpec.AT_MOST ? measureHeight : heightSize;
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount != CHILD_COUNT) {
            Log.e(TAG, "wtf, child count must be 3!");
        }

        for (int i = 0; i < childCount; ++i) {
            View childView = getChildAt(i);

            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;

            if (childView instanceof ActionBar) {
                // do nothing
            } else if (childView instanceof Indicators) {
                int horizontalMargin = (getMeasuredWidth() - childView.getMeasuredWidth()) / 2;
                left = horizontalMargin;
                right = horizontalMargin;
                bottom = dp2px(16);
            } else if (childView instanceof Pager) {
                // do nothing
            } else {
                Log.e(TAG, "wtfffff!");
            }

            childView.layout(left, top, right, bottom);
        }
    }

    private int dp2px(float dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, mContext.getResources().getDisplayMetrics());
    }
}
