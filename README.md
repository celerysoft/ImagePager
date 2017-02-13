# Image Pager

## What is it?

Image Pager is an Android widget, Implementation of ViewPager for Android that supports sliding to change picture, zooming picture by various touch gestures.

## Features

 * It extends ViewPager, so it works like a ViewPager

 * Load image from Resource, External Storage or the Internet

 * With memory cache and disk cache supported

## Technical Information

* Required minimum API level: 14(Android 4.0)

* Supports all the screens sizes and density.

## Usage

### Step 1

#### Gradle

```
dependencies {
        compile 'com.celerysoft:imagepager:2.1.1'
}
```

### Step 2

Add the ImagePager to your layout

```
<com.celerysoft.imagepager.ImagePager  
        android:id="@+id/image_pager"  
        android:layout_width="match_parent"  
        android:layout_height="match_parent" />
```

### Step 3

Create an adapter for the ImagePager, you can see it in the [demo](https://github.com/celerysoft/ImagePager/blob/master/demo/src/main/java/com/celerysoft/imagepagerdemo/MainActivity.java).

## Screenshots

![Screenshot 1](https://raw.githubusercontent.com/celerysoft/README/master/ImagePager/sc01.gif "Screenshot 1")

![Screenshot 2](https://raw.githubusercontent.com/celerysoft/README/master/ImagePager/sc02.gif "Screenshot 2")

## Credits

[PhotoView](https://github.com/chrisbanes/PhotoView)

[DiskLruCache](http://developer.android.com/samples/DisplayingBitmaps/src/com.example.android.displayingbitmaps/util/DiskLruCache.html)

## License

[MIT](./LICENSE)
