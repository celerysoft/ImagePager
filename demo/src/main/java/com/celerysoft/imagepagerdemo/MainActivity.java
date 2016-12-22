package com.celerysoft.imagepagerdemo;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.celerysoft.imagepager.ImagePager;
import com.celerysoft.imagepager.adapter.SimpleImagePagerAdapter;
import com.celerysoft.imagepager.animation.DepthPageTransformer;
import com.celerysoft.imagepager.view.indicator.DotIndicator;
import com.celerysoft.imagepager.view.indicator.TextIndicator;

import java.util.ArrayList;

/**
 * ImagePager usage demo.
 */
public class MainActivity extends AppCompatActivity {
    private ImagePager mImagePager;
    private SimpleImagePagerAdapter mAdapter;

    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initActivity();
    }

    private void initActivity() {
        bindView();
        bindListener();
        initData();
        initView();
    }

    private void bindView() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mImagePager = (ImagePager) findViewById(R.id.main_image_pager);
    }

    private void bindListener() {
        mImagePager.setOnPageClickListener(new ImagePager.OnPageClickListener() {
            @Override
            public void onPageClick() {
                toggleActionBarVisibility();
            }
        });
    }

    private void initData() {
        resetAdapter();
    }

    private void initView() {
        mImagePager.setPageTransformer(true, new DepthPageTransformer());

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("ImagePager Demo");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete:
                deleteCurrentImage();
                return true;
            case R.id.action_restore:
                resetAdapter();
                return true;

            case R.id.action_dot_indicator:
                setDotIndicator();
                return true;
            case R.id.action_text_indicator:
                setTextIndicator();
                return true;

            case R.id.action_indicator_position_left:
                setIndicatorPosition(ImagePager.LEFT);
                return true;
            case R.id.action_indicator_position_top:
                setIndicatorPosition(ImagePager.TOP);
                return true;
            case R.id.action_indicator_position_right:
                setIndicatorPosition(ImagePager.RIGHT);
                return true;
            case R.id.action_indicator_position_bottom:
                setIndicatorPosition(ImagePager.BOTTOM);
                return true;
            case R.id.action_indicator_position_top_left:
                setIndicatorPosition(ImagePager.TOP_LEFT);
                return true;
            case R.id.action_indicator_position_top_right:
                setIndicatorPosition(ImagePager.TOP_RIGHT);
                return true;
            case R.id.action_indicator_position_bottom_left:
                setIndicatorPosition(ImagePager.BOTTOM_LEFT);
                return true;
            case R.id.action_indicator_position_bottom_right:
                setIndicatorPosition(ImagePager.BOTTOM_RIGHT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void toggleActionBarVisibility() {
        if (mAppBarLayout.getVisibility() == View.GONE) {
            mAppBarLayout.setVisibility(View.VISIBLE);
        } else {
            mAppBarLayout.setVisibility(View.GONE);
        }
    }

    private void resetAdapter() {
        // set the transformer for pages
        mImagePager.setPageTransformer(true, new DepthPageTransformer());
        //mImagePager.setPageTransformer(true, new ZoomOutPageTransformer());

        mAdapter = new SimpleImagePagerAdapter(this);

        ArrayList<SimpleImagePagerAdapter.Image> images = new ArrayList<>();

        SimpleImagePagerAdapter.Image image1 = new SimpleImagePagerAdapter.Image();
        image1.setImageResId(R.drawable._00001);
        images.add(image1);

        SimpleImagePagerAdapter.Image image2 = new SimpleImagePagerAdapter.Image();
        image2.setImageResId(R.drawable._00002);
        images.add(image2);

        SimpleImagePagerAdapter.Image image3 = new SimpleImagePagerAdapter.Image();
        image3.setImageResId(R.drawable._00003);
        images.add(image3);

        SimpleImagePagerAdapter.Image image4 = new SimpleImagePagerAdapter.Image();
        image4.setImageUrl("http://7xpapo.com1.z0.glb.clouddn.com/blog/assets/postImages/2016102601.png");
        images.add(image4);

        SimpleImagePagerAdapter.Image image5 = new SimpleImagePagerAdapter.Image();
        image5.setImageUrl("http://7xpapo.com1.z0.glb.clouddn.com/blog/assets/postImages/2016011509.png");
        images.add(image5);

        SimpleImagePagerAdapter.Image image6 = new SimpleImagePagerAdapter.Image();
        image6.setImageUrl("http://7xpapo.com1.z0.glb.clouddn.com/blog/assets/postImages/2016010502.png");
        images.add(image6);

        SimpleImagePagerAdapter.Image image7 = new SimpleImagePagerAdapter.Image();
        image7.setImageUrl("http://7xpapo.com1.z0.glb.clouddn.com/blog/assets/postImages/2016010401.png");
        images.add(image7);

        // set your own local image paths
//        SimpleImagePagerAdapter.Image image8 = new SimpleImagePagerAdapter.Image();
//        image8.setImagePath("/sdcard/DCIM/Camera/beku4Q9gGV2.jpg");
//        images.add(image8);
//
//        SimpleImagePagerAdapter.Image image9 = new SimpleImagePagerAdapter.Image();
//        image9.setImagePath("/sdcard/DCIM/Camera/IMG_20120907_194939.jpg");
//        images.add(image9);
//
//        SimpleImagePagerAdapter.Image image10 = new SimpleImagePagerAdapter.Image();
//        image10.setImagePath("/sdcard/DCIM/Camera/IMG_20120910_093748.jpg");
//        images.add(image10);

        mAdapter.setImages(images);

        mImagePager.setAdapter(mAdapter);

        setDotIndicator();

        // set up the indicator margin
        mImagePager.setIndicatorMargin(16);
    }

    private void deleteCurrentImage() {
        mAdapter.removeImage(mImagePager.getCurrentImagePosition());
    }

    private void setTextIndicator() {
        TextIndicator indicator = new TextIndicator(this);
        mImagePager.setIndicator(indicator);
    }

    private void setDotIndicator() {
        DotIndicator indicator = new DotIndicator(this);
        indicator.setSelectedImageResource(R.drawable.ic_selected);
        indicator.setUnselectedImageResource(R.drawable.ic_unselected);
        mImagePager.setIndicator(indicator);
    }

    private void setIndicatorPosition(int position) {
        mImagePager.setIndicatorPosition(position);
    }
}
