package com.celerysoft.imagepagerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.celerysoft.imagepager.ImagePager;
import com.celerysoft.imagepager.adapter.SimpleImagePagerAdapter;
import com.celerysoft.imagepager.animation.DepthPageTransformer;
import com.celerysoft.imagepager.animation.ZoomOutPageTransformer;

import java.util.ArrayList;

/**
 * ImagePager usage demo.
 */
public class MainActivity extends Activity {
    private ImagePager mImagePager;
    private SimpleImagePagerAdapter mAdapter;

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
        image4.setImageUrl("http://7xpapo.com1.z0.glb.clouddn.com/2015123001.png");
        images.add(image4);

        SimpleImagePagerAdapter.Image image5 = new SimpleImagePagerAdapter.Image();
        image5.setImageUrl("http://7xpapo.com1.z0.glb.clouddn.com/2016011803.png");
        images.add(image5);

        SimpleImagePagerAdapter.Image image6 = new SimpleImagePagerAdapter.Image();
        image6.setImageUrl("http://7xpapo.com1.z0.glb.clouddn.com/2016011509.png");
        images.add(image6);

        SimpleImagePagerAdapter.Image image7 = new SimpleImagePagerAdapter.Image();
        image7.setImageUrl("http://7xpapo.com1.z0.glb.clouddn.com/2016010503.png");
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
    }

    private void deleteCurrentImage() {
        mAdapter.removeImage(mImagePager.getCurrentImagePosition());
        mAdapter.notifyDataSetChanged();
    }
}
