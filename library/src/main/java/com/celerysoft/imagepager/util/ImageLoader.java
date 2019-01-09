package com.celerysoft.imagepager.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.celerysoft.imagepager.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Celery on 16/3/31.
 * Load bitmap
 */
public class ImageLoader {
    private static final String TAG = "ImageLoader";

    private static final long DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private static final int DISK_CACHE_VALUE_COUNT = 1;
    private static final int DISK_CACHE_INDEX = 0;
    private static final int IO_BUFFER_SIZE = 8 * 1024;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;

    private static final int INTERNET_KEEP_ALIVE = 5 * 1000;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), sThreadFactory);

    private Context mContext;
    private LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;

    private ImageLoader(Context context) {
        mContext = context.getApplicationContext();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };

        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, DISK_CACHE_VALUE_COUNT, DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * build a new instance of ImageLoader
     * @param context Context
     * @return a new instance of ImageLoader
     */
    public static ImageLoader build(Context context) {
        return new ImageLoader(context);
    }

    public void bindImageView(final String uri, final PhotoView photoView) {
        int reqWidth = 2 * mContext.getResources().getDisplayMetrics().widthPixels;
        int reqHeight = 2 * mContext.getResources().getDisplayMetrics().heightPixels;
        bindImageView(uri, photoView, reqWidth, reqHeight);
    }

    public void bindImageView(final String uri, final PhotoView photoView, final int reqWidth, final int reqHeight) {
        photoView.setTag(uri);
        Bitmap bitmap = loadBitmapFromMemoryCache(uri);
        if (bitmap != null) {
            photoView.setImageBitmap(bitmap);
            return;
        }

        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                LoaderResult result = new LoaderResult(photoView, null, null);
                mUiThreadHandler.obtainMessage(MESSAGE_START, result).sendToTarget();

                Bitmap bitmap = loadBitmap(uri, reqWidth, reqHeight);
                if (bitmap != null) {
                    result = new LoaderResult(photoView, uri, bitmap);
                    mUiThreadHandler.obtainMessage(MESSAGE_LOADED, result).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    private int mPlaceholderResInt = R.drawable.placeholder;
    public void setPlaceholder(@DrawableRes int placeholderResInt) {
        mPlaceholderResInt = placeholderResInt;
    }

    public void resetPlaceholder() {
        mPlaceholderResInt = R.drawable.placeholder;
    }

    private final int MESSAGE_START = 0;
    private final int MESSAGE_LOADED = 1;
    private Handler mUiThreadHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            switch (msg.what) {
                case MESSAGE_START:
                    imageView.setImageResource(mPlaceholderResInt);
                    break;
                case MESSAGE_LOADED:
                    imageView.setImageBitmap(result.bitmap);
                    String uri = (String) imageView.getTag();
                    if (uri.equals(result.uri)) {
                        imageView.setImageBitmap(result.bitmap);
                    } else {
                        Log.w(TAG, "set image bitmap, but url has changed, ignored!");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemoryCache (String key) {
        return mMemoryCache.get(key);
    }

    public Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        // Step 1, load Bitmap from memory cache
        Bitmap bitmap = loadBitmapFromMemoryCache(uri);
        if (bitmap != null) {
            Log.d(TAG, "Load Bitmap from memory cache, url: " + uri);
            return bitmap;
        }

        try {
            // Step 2, load Bitmap from disk cache
            bitmap = loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
            if (bitmap != null) {
                Log.d(TAG, "Load Bitmap from disk cache, url: " + uri);
                return bitmap;
            }

            if (uri.charAt(0) == 'h') {//"http://xxxxx"
                // Step 3, load Bitmap from HTTP
                bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight);
                if (bitmap != null) {
                    Log.d(TAG, "Load Bitmap from HTTP, url: " + uri);
                    return bitmap;
                }
            } else if (uri.charAt(0) == '/') {//"/storage/" or "/sdcard/"
                // Step 4, load Bitmap from external storage
                bitmap = loadBitmapFromExternalStorage(uri, reqWidth, reqHeight);
                if (bitmap != null) {
                    Log.d(TAG, "Load Bitmap from external storage, url: " + uri);
                    return bitmap;
                }
            } else {
                // Step 5, load Bitmap from resource
                bitmap = loadBitmapFromResource(uri, reqWidth, reqHeight);
                if (bitmap != null) {
                    Log.d(TAG, "Load Bitmap from resource, url: " + uri);
                    return bitmap;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mDiskLruCache == null) {
            Log.w(TAG, "DiskLruCache did not created.");
            if (uri.charAt(0) == 'h') {//"http://xxxxx"
                bitmap = downloadBitmapFromUrl(uri);
            } else if (uri.charAt(0) == '/') {//"/storage/" or "/sdcard/"
                bitmap = downloadBitmapFromExternalStorage(uri);
            } else {
                bitmap = downloadBitmapFromResource(uri);
            }

        }

        return bitmap;
    }

    private Bitmap loadBitmapFromMemoryCache(String url) {
        final String key = hashKeyFromUrl(url);
        return getBitmapFromMemoryCache(key);
    }

    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("Could not access network from UI Thread.");
        }
        if (mDiskLruCache == null) {
            return null;
        }

        String key = hashKeyFromUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(url, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }
        return loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    public boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(INTERNET_KEEP_ALIVE);
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "download bitmap failed. " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            Util.close(in);
            Util.close(out);
        }

        return false;
    }

    private Bitmap loadBitmapFromExternalStorage(String url, int reqWidth, int reqHeight) throws IOException {
        if (mDiskLruCache == null) {
            return null;
        }

        String key = hashKeyFromUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadExternalStorageUrlToStream(url, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }

        return loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    private boolean downloadExternalStorageUrlToStream(String urlString, OutputStream outputStream) {
        FileInputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new FileInputStream(urlString);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int bufferSize = IO_BUFFER_SIZE;
            byte[] buffer = new byte[bufferSize];
            int length;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "download bitmap from external storage failed. " + e.getMessage());
        } finally {
            Util.close(in);
            Util.close(out);
        }

        return false;
    }

    private Bitmap loadBitmapFromResource(String resId, int reqWidth, int reqHeight) throws IOException {
        if (mDiskLruCache == null) {
            return null;
        }

        String key = hashKeyFromUrl(resId);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            if (downloadResourceToStream(resId, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }

        return loadBitmapFromDiskCache(resId, reqWidth, reqHeight);
    }

    private boolean downloadResourceToStream(String resIdString, OutputStream outputStream) {
        InputStream in = null;
        BufferedOutputStream out = null;

        int resId = Integer.valueOf(resIdString);
        try {
            in = mContext.getResources().openRawResource(resId);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "download bitmap from external storage failed. " + e.getMessage());
        } finally {
            Util.close(in);
            Util.close(out);
        }

        return false;
    }

    private Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "load bitmap from UI Thread, it is not recommended!");
        }
        if (mDiskLruCache == null) {
            return null;
        }

        Bitmap bitmap = null;
        String key = hashKeyFromUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if (snapshot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = ImageUtil.getBitmap(fileDescriptor, reqWidth, reqHeight);
            if (bitmap != null) {
                addBitmapToMemoryCache(key, bitmap);
            }
        }

        return bitmap;
    }

    private Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(INTERNET_KEEP_ALIVE);
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "download bitmap failed. " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            Util.close(in);
        }

        return bitmap;
    }

    private Bitmap downloadBitmapFromExternalStorage(String url) {
        return ImageUtil.getBitmap(mContext, url);
    }

    private Bitmap downloadBitmapFromResource(String resId) {
        int id = Integer.valueOf(resId);
        return ImageUtil.getBitmap(mContext, id);
    }

    private String hashKeyFromUrl(String url) {
        String cacheKey;

        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            cacheKey = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }

        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < bytes.length; ++i) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(hex);
        }

        return stringBuilder.toString();
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        boolean isExternalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        final String cachePath;
        if (isExternalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return  path.getUsableSpace();
        }
        final StatFs statFs = new StatFs((path.getPath()));
        return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
    }

    public static class LoaderResult {
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }
}
