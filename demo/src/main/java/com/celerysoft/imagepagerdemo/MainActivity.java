package com.celerysoft.imagepagerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.celerysoft.imagepager.ImagePager;
import com.celerysoft.imagepager.adapter.ImagePagerAdapter;
import com.celerysoft.imagepager.adapter.SimpleImagePagerAdapter;
import com.celerysoft.imagepager.animation.DepthPageTransformer;
import com.celerysoft.imagepager.animation.ZoomOutPageTransformer;
import com.celerysoft.imagepager.view.indicator.Indicator;

import java.util.ArrayList;

/**
 * ImagePager usage demo.
 */
public class MainActivity extends Activity {
    private ImagePager mImagePager;
    private ImagePagerAdapter mAdapter;

    private View mActionBar;
    private Button mBtnBack;
    private Button mBtnDelete;
    private Button mBtnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mActionBar = findViewById(R.id.main_action_bar);
        mBtnBack = (Button) findViewById(R.id.main_btn_back);
        mBtnDelete = (Button) findViewById(R.id.main_btn_delete);
        mBtnReset = (Button) findViewById(R.id.main_btn_reset);
        mImagePager = (ImagePager) findViewById(R.id.main_image_pager);

        mImagePager.setOnPageClickListener(new ImagePager.OnPageClickListener() {
            @Override
            public void onPageClick() {
                toggleActionBarVisibility();
            }
        });
        mImagePager.setPageTransformer(true, new DepthPageTransformer());

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCurrentImage();
            }
        });
        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAdapter();
            }
        });

        resetAdapter();
    }

    private void toggleActionBarVisibility() {
        if (mActionBar.getVisibility() == View.INVISIBLE) {
            mActionBar.setVisibility(View.VISIBLE);
        } else {
            mActionBar.setVisibility(View.INVISIBLE);
        }
    }

    private int adapterType = 0;
    private boolean isYourDemo = true;
    private void resetAdapter() {
        mAdapter = new SimpleImagePagerAdapter(this);

        int type = 0;
        if (!isYourDemo) {
            type = 1;
        } else {
            if (adapterType % 2 == 0) {
                type = 1;
                mImagePager.setPageTransformer(true, new DepthPageTransformer());
            } else {
                type = 0;
                mImagePager.setPageTransformer(true, new ZoomOutPageTransformer());
            }
        }

        if (type == 1) {
            ArrayList<SimpleImagePagerAdapter.Image> images = new ArrayList<>();

            SimpleImagePagerAdapter.Image image1 = new SimpleImagePagerAdapter.Image(this);
            image1.setImageResId(R.drawable._00001);
            images.add(image1);

            SimpleImagePagerAdapter.Image image2 = new SimpleImagePagerAdapter.Image(this);
            image2.setImageResId(R.drawable._00002);
            images.add(image2);

            SimpleImagePagerAdapter.Image image3 = new SimpleImagePagerAdapter.Image(this);
            image3.setImageResId(R.drawable._00003);
            images.add(image3);

            ((SimpleImagePagerAdapter) mAdapter).setImages(images);
        } else if (type == 0) {
            // set your own local image paths
//            ArrayList<String> imagePaths = new ArrayList<>();
//            imagePaths.add("/storage/emulated/0/Pictures/GIS/20151123_104716.jpg");
//            imagePaths.add("/storage/emulated/0/Pictures/GIS/20151123_104729.jpg");
//            imagePaths.add("/storage/emulated/0/Pictures/GIS/20151123_105347.jpg");
//            imagePaths.add("/storage/emulated/0/Pictures/GIS/20151128_104537.jpg");
//            imagePaths.add("/storage/emulated/0/Pictures/GIS/20151209_153401.jpg");
//            imagePaths.add("/storage/emulated/0/Pictures/GIS/20151223_103259.jpg");
//            imagePaths.add("/storage/emulated/0/Pictures/GIS/20151211_105652.jpg");
            ArrayList<SimpleImagePagerAdapter.Image> images = new ArrayList<>();

            SimpleImagePagerAdapter.Image image1 = new SimpleImagePagerAdapter.Image(this);
            image1.setImagePath("/storage/emulated/0/Pictures/GIS/20151123_104716.jpg");
            images.add(image1);

            SimpleImagePagerAdapter.Image image2 = new SimpleImagePagerAdapter.Image(this);
            image2.setImagePath("/storage/emulated/0/Pictures/GIS/20151123_104729.jpg");
            images.add(image2);

            SimpleImagePagerAdapter.Image image3 = new SimpleImagePagerAdapter.Image(this);
            image3.setImagePath("/storage/emulated/0/Pictures/GIS/20151123_105347.jpg");
            images.add(image3);

            SimpleImagePagerAdapter.Image image4 = new SimpleImagePagerAdapter.Image(this);
            image4.setImagePath("/storage/emulated/0/Pictures/GIS/20151128_104537.jpg");
            images.add(image4);

            SimpleImagePagerAdapter.Image image5 = new SimpleImagePagerAdapter.Image(this);
            image5.setImagePath("/storage/emulated/0/Pictures/GIS/20151209_153401.jpg");
            images.add(image5);

            SimpleImagePagerAdapter.Image image6 = new SimpleImagePagerAdapter.Image(this);
            image6.setImagePath("/storage/emulated/0/Pictures/GIS/20151223_103259.jpg");
            images.add(image6);

            SimpleImagePagerAdapter.Image image7 = new SimpleImagePagerAdapter.Image(this);
            image7.setImagePath("/storage/emulated/0/Pictures/GIS/20151211_105652.jpg");
            images.add(image7);

            ((SimpleImagePagerAdapter) mAdapter).setImages(images);
        }

        adapterType++;

        mImagePager.setAdapter(mAdapter);
    }

    private void deleteCurrentImage() {
        mAdapter.removeImage(mImagePager.getCurrentImagePosition());
        mAdapter.notifyDataSetChanged();
    }
}
