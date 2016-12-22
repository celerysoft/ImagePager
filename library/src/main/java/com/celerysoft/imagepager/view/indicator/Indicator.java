package com.celerysoft.imagepager.view.indicator;

/**
 * Define indicator of ImagePager must implements' methods.
 */
public interface Indicator {
    void onPageSelected(int position);
    void onPageDeleted();
    void onPageAdapterChanged(int imageCount);
}
