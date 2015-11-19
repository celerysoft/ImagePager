package com.celerysoft.imagepagerdemo;

import android.app.Activity;
import android.os.Bundle;

import com.celerysoft.imagepager.view.Pager;
import com.celerysoft.imagepager.adapter.ImagePagerAdapter;

/**
 * Created by Administrator on 2015-11-18.
 */
public class MainActivity extends Activity {
    private Pager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mPager = (Pager) findViewById(R.id.main_image_pager);

        ImagePagerAdapter adapter = new ImagePagerAdapter(this);
        int[] imageResIds = new int[3];
        imageResIds[0] = R.drawable._00001;
        imageResIds[1] = R.drawable._00002;
        imageResIds[2] = R.drawable._00003;
        adapter.setImageResIds(imageResIds);

        mPager.setAdapter(adapter);
    }
}
