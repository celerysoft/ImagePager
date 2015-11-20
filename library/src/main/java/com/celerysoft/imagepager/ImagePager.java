package com.celerysoft.imagepager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.celerysoft.imagepager.adapter.ImagePagerAdapter;
import com.celerysoft.imagepager.view.ActionBar;
import com.celerysoft.imagepager.view.Pager;
import com.celerysoft.imagepager.view.indicator.Indicator;
import com.celerysoft.imagepager.view.indicator.TextIndicator;

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
    private Pager mPager;
    private Indicator mIndicator;
    private ActionBar mActionBar;

    // propertry
    private ImagePagerAdapter mAdapter;
    public ImagePagerAdapter getAdapter() {
        return mAdapter;
    }
    public void setAdapter(ImagePagerAdapter adapter) {
        mAdapter = adapter;
        ImagePagerAdapter imagePagerAdapter = (ImagePagerAdapter) adapter;
        imagePagerAdapter.setOnPageClickListenerListener(new OnImageClickListener() {
            @Override
            public void onImageClick() {
                toggleActionBarVisibility();
            }
        });
        if (mOnImageClickListener != null) {
            imagePagerAdapter.setOnImageClickListener(mOnImageClickListener);
        }
        mPager.setAdapter(adapter);
        mIndicator.setImageCount(adapter.getCount());
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
        mContext = getContext();

        try {
            setBackgroundColor(getResources().getColor(R.color.image_pager_background, null));
        } catch (NoSuchMethodError e) {
            setBackgroundColor(getResources().getColor(R.color.image_pager_background));
        }

        mPager = new Pager(mContext);
        addView(mPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mIndicator = new TextIndicator(mContext);
        addView((View)mIndicator, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mActionBar = new ActionBar(mContext);
        addView(mActionBar, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        hideActionBar();


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
                mIndicator.onPageSelected(position);
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

            int left = l;
            int top = t;
            int right = r;
            int bottom = b;

            if (childView instanceof ActionBar) {
                // do nothing
            } else if (childView instanceof Indicator) {
                int horizontalMargin = (getMeasuredWidth() - childView.getMeasuredWidth()) / 2;
                left += horizontalMargin;
                right -= horizontalMargin;
                top = getMeasuredHeight() - (int) (childView.getMeasuredHeight() * 2.5);
                bottom = top + childView.getMeasuredHeight();
            } else if (childView instanceof Pager) {
                // do nothing
            } else {
                Log.e(TAG, "wtfffff!");
                childView.layout(l, t, r, b);
                continue;
            }

            childView.layout(left, top, right, bottom);
        }
    }

    private void toggleActionBarVisibility() {
        if (mActionBar.getVisibility() == INVISIBLE) {
            showActionBar();
        } else {
            hideActionBar();
        }
    }

    private void showActionBar() {
        mActionBar.setVisibility(VISIBLE);
        Log.d(TAG, "show actionbar");
    }

    private void hideActionBar() {
        mActionBar.setVisibility(INVISIBLE);
        Log.d(TAG, "hide actionbar");
    }

    public interface OnImageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageSelected(int position);
        void onPageScrollStateChanged(int state);
    }

    public interface OnImageClickListener {
        void onImageClick();
    }
}
