package com.celerysoft.imagepagerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.celerysoft.imagepager.ImagePager;
import com.celerysoft.imagepager.adapter.ImagePagerAdapter;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2015-11-18.
 */
public class MainActivity extends Activity {
    private ImagePager mImagePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mImagePager = (ImagePager) findViewById(R.id.main_image_pager);

        ImagePagerAdapter adapter = new ImagePagerAdapter();
        ArrayList<PhotoView> photoViews = new ArrayList<>();
        PhotoView v1 = new PhotoView(this);
        v1.setImageResource(R.drawable._00001);
        PhotoView v2 = new PhotoView(this);
        v2.setImageResource(R.drawable._00002);
        PhotoView v3 = new PhotoView(this);
        v3.setImageResource(R.drawable._00003);
        photoViews.add(v1);
        photoViews.add(v2);
        photoViews.add(v3);
        adapter.setImageViews(photoViews);

        mImagePager.setAdapter(adapter);
        //mImagePager.setAdapter(new SamplePagerAdapter());
    }

    static class SamplePagerAdapter extends PagerAdapter {

        private static final int[] sDrawables = { R.drawable._00001, R.drawable._00002, R.drawable._00003 };

        @Override
        public int getCount() {
            return sDrawables.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setImageResource(sDrawables[position]);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
