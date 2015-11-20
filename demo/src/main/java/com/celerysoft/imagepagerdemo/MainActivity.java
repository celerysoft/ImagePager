package com.celerysoft.imagepagerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.celerysoft.imagepager.ImagePager;
import com.celerysoft.imagepager.adapter.SimpleImagePagerAdapter;

/**
 * Created by Administrator on 2015-11-18.
 */
public class MainActivity extends Activity {
    private ImagePager mImagePager;
    private View mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mActionBar = findViewById(R.id.mian_action_bar);
        mImagePager = (ImagePager) findViewById(R.id.main_image_pager);

        mImagePager.setOnPageClickListener(new ImagePager.OnPageClickListener() {
            @Override
            public void onPageClick() {
                toggleActionBarVisibility();
            }
        });

        SimpleImagePagerAdapter adapter = new SimpleImagePagerAdapter(this);
        int[] imageResIds = new int[3];
        imageResIds[0] = R.drawable._00001;
        imageResIds[1] = R.drawable._00002;
        imageResIds[2] = R.drawable._00003;
        adapter.setImageResIds(imageResIds);
        String[] imagePaths = new String[3];
        imagePaths[0] = "/storage/emulated/0/Pictures/GIS/GIS_20151116_162430478918924.jpg";
        imagePaths[1] = "/storage/emulated/0/Pictures/GIS/GIS_20151116_165549478918924.jpg";
        imagePaths[2] = "/storage/emulated/0/Pictures/GIS/GIS_20151117_155447478918924.jpg";
        adapter.setImagePaths(imagePaths);

        mImagePager.setAdapter(adapter);
    }

    private void toggleActionBarVisibility() {
        if (mActionBar.getVisibility() == View.INVISIBLE) {
            mActionBar.setVisibility(View.VISIBLE);
        } else {
            mActionBar.setVisibility(View.INVISIBLE);
        }
    }
}
