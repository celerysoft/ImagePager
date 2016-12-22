package com.celerysoft.imagepager;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.celerysoft.imagepager.adapter.ImagePagerAdapter;
import com.celerysoft.imagepager.util.DensityUtil;
import com.celerysoft.imagepager.view.Pager;
import com.celerysoft.imagepager.view.indicator.Indicator;
import com.celerysoft.imagepager.view.indicator.TextIndicator;

/**
 * ImagePager, display images, contain a indicator and a pager.
 * Created by Celery on 2015-11-19.
 */
public class ImagePager extends ViewGroup {
    // const
    private static final String TAG = "ImagePager";
    public static final int LEFT = 0x0;
    public static final int TOP = 0x1;
    public static final int RIGHT = 0x2;
    public static final int BOTTOM = 0x3;
    public static final int TOP_LEFT = 0x4;
    public static final int TOP_RIGHT = 0x5;
    public static final int BOTTOM_LEFT = 0x6;
    public static final int BOTTOM_RIGHT = 0x7;

    private static int CHILD_COUNT = 1;

    // field
    private Pager mPager;
    private Indicator mIndicator;
    private int mIndicatorPosition = BOTTOM;
    private float mIndicatorMargin = 16;

    // propertry
    private ImagePagerAdapter mAdapter;
    public ImagePagerAdapter getAdapter() {
        return mAdapter;
    }
    public void setAdapter(ImagePagerAdapter adapter) {
        mAdapter = adapter;
        ImagePagerAdapter imagePagerAdapter = (ImagePagerAdapter) adapter;
        if (mOnPageClickListener != null) {
            imagePagerAdapter.setOnPageClickListenerListener(mOnPageClickListener);
        }
        if (mOnImageClickListener != null) {
            imagePagerAdapter.setOnImageClickListener(mOnImageClickListener);
        }
        mPager.setAdapter(adapter);

        // TODO
        if (mIndicator != null) {
            mIndicator.onPageAdapterChanged(adapter.getCount());
            mAdapter.setIndicator(mIndicator);
        }
    }

    private OnImageChangeListener mOnImageChangeListener;
    public OnImageChangeListener getOnImageChangeListener() {
        return mOnImageChangeListener;
    }
    public void setOnImageChangeListener(OnImageChangeListener onImageChangeListener) {
        mOnImageChangeListener = onImageChangeListener;
    }

    private OnImageClickListener mOnImageClickListener;
    public OnImageClickListener getOnImageClickListener() {
        return mOnImageClickListener;
    }
    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        mOnImageClickListener = onImageClickListener;
        if (mAdapter != null) {
            mAdapter.setOnImageClickListener(onImageClickListener);
        }
    }

    private OnPageClickListener mOnPageClickListener;
    public OnPageClickListener getOnPageClickListener() {
        return mOnPageClickListener;
    }
    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        mOnPageClickListener = onPageClickListener;
        if (mAdapter != null) {
            mAdapter.setOnPageClickListenerListener(onPageClickListener);
        }
    }

    public ImagePager(Context context) {
        super(context);
        initImagePager();
    }

    public ImagePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initImagePager();
    }

    private void initImagePager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setBackgroundColor(getResources().getColor(R.color.image_pager_background, null));
        } else {
            setBackgroundColor(getResources().getColor(R.color.image_pager_background));
        }

        mPager = new Pager(getContext());
        addView(mPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mOnImageChangeListener != null) {
                    mOnImageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mOnImageChangeListener != null) {
                    mOnImageChangeListener.onPageSelected(position);
                }

                if (mIndicator != null) {
                    mIndicator.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mOnImageChangeListener != null) {
                    mOnImageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
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
            Log.e(TAG, "wtf, child count must be " + CHILD_COUNT + "!");
        }

        int childWidth = 0;
        int childHeight = 0;

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
            Log.e(TAG, "wtf, child count must be " + CHILD_COUNT + "!");
        }

        for (int i = 0; i < childCount; ++i) {
            View childView = getChildAt(i);

            int left = l;
            int top = t;
            int right = r;
            int bottom = b;

            if (childView instanceof Indicator) {
                int[] positions = deriveIndicatorPosition(l, t, r, b, childView);
                left = positions[0];
                top = positions[1];
                right = positions[2];
                bottom = positions[3];
            } else if (childView instanceof Pager) {
                // no op
            } else {
                Log.e(TAG, "wtfffff!");
            }

            childView.layout(left, top, right, bottom);
        }
    }

    private int[] deriveIndicatorPosition(int left, int top, int right, int bottom, View indicatorView) {
        int[] positions = new int[4];

        int horizontalMargin;
        int verticalMargin;
        switch (mIndicatorPosition) {
            case LEFT:
                verticalMargin = (getMeasuredHeight() - indicatorView.getMeasuredHeight()) / 2;
                left = DensityUtil.dp2px(getContext(), mIndicatorMargin);
                right = left + indicatorView.getMeasuredWidth();
                top += verticalMargin;
                bottom -= verticalMargin;
                break;
            case TOP:
                horizontalMargin = (getMeasuredWidth() - indicatorView.getMeasuredWidth()) / 2;
                left += horizontalMargin;
                right -= horizontalMargin;
                top = DensityUtil.dp2px(getContext(), mIndicatorMargin);
                bottom = top + indicatorView.getMeasuredHeight();
                break;
            case RIGHT:
                verticalMargin = (getMeasuredHeight() - indicatorView.getMeasuredHeight()) / 2;
                right = getMeasuredWidth() - DensityUtil.dp2px(getContext(), mIndicatorMargin);
                left = right - indicatorView.getMeasuredWidth();
                top += verticalMargin;
                bottom -= verticalMargin;
                break;
            case TOP_LEFT:
                left = DensityUtil.dp2px(getContext(), mIndicatorMargin);
                right = left + indicatorView.getMeasuredWidth();
                top = DensityUtil.dp2px(getContext(), mIndicatorMargin);
                bottom = top + indicatorView.getMeasuredHeight();
                break;
            case TOP_RIGHT:
                right = getMeasuredWidth() - DensityUtil.dp2px(getContext(), mIndicatorMargin);
                left = right - indicatorView.getMeasuredWidth();
                top = DensityUtil.dp2px(getContext(), mIndicatorMargin);
                bottom = top + indicatorView.getMeasuredHeight();
                break;
            case BOTTOM_LEFT:
                left = DensityUtil.dp2px(getContext(), mIndicatorMargin);
                right = left + indicatorView.getMeasuredWidth();
                bottom = getMeasuredHeight() - indicatorView.getMeasuredHeight();
                top = bottom - indicatorView.getMeasuredHeight();
                break;
            case BOTTOM_RIGHT:
                right = getMeasuredWidth() - DensityUtil.dp2px(getContext(), mIndicatorMargin);
                left = right - indicatorView.getMeasuredWidth();
                bottom = getMeasuredHeight() - indicatorView.getMeasuredHeight();
                top = bottom - indicatorView.getMeasuredHeight();
                break;
            case BOTTOM:
            default:
                horizontalMargin = (getMeasuredWidth() - indicatorView.getMeasuredWidth()) / 2;
                left += horizontalMargin;
                right -= horizontalMargin;
                bottom = getMeasuredHeight() - DensityUtil.dp2px(getContext(), mIndicatorMargin);
                top = bottom - indicatorView.getMeasuredHeight();
                break;
        }

        positions[0] = left;
        positions[1] = top;
        positions[2] = right;
        positions[3] = bottom;

        return positions;
    }

    public void setIndicator(Indicator indicator) {
        if (mIndicator != null) {
            removeView((View) mIndicator);
        }

        CHILD_COUNT = 2;
        mIndicator = indicator;
        if (mAdapter != null) {
            mIndicator.onPageAdapterChanged(mAdapter.getCount());
            mIndicator.onPageSelected(getCurrentImagePosition());
            mAdapter.setIndicator(mIndicator);
        }
        addView((View) mIndicator, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * set up the margin of indicator, default is 16 dp.
     * @param marginInDp margin in dp value.
     */
    public void setIndicatorMargin(float marginInDp) {
        mIndicatorMargin = marginInDp;
    }

    /**
     * set up the position of indicator
     * @param position pick one from {@link #LEFT}, {@link #TOP}, {@link #RIGHT}, {@link #BOTTOM},
     * {@link #TOP_LEFT}, {@link #TOP_RIGHT}, {@link #BOTTOM_LEFT}, {@link #BOTTOM_RIGHT}
     */
    public void setIndicatorPosition(int position) {
        mIndicatorPosition = position;
        requestLayout();
    }

    public int getCurrentImagePosition() {
        return mPager.getCurrentItem();
    }

    public void moveToImage(int imagePosition) {
        mPager.setCurrentItem(imagePosition);
    }

    public void setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        mPager.setPageTransformer(reverseDrawingOrder, transformer);
    }

    public void setOffscreenPageLimit(int limit) {
        mPager.setOffscreenPageLimit(limit);
    }

    public interface OnImageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageSelected(int position);
        void onPageScrollStateChanged(int state);
    }

    public interface OnImageClickListener {
        void onImageClick();
    }

    public interface OnPageClickListener {
        void onPageClick();
    }
}
