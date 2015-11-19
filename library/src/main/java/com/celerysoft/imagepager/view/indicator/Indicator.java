package com.celerysoft.imagepager.view.indicator;

/**
 * Define indicator of ImagePager must implements' methods.
 */
public interface Indicator {
    public void onPageSelected(int position);
    public void setImageCount(int imageCount);
}
