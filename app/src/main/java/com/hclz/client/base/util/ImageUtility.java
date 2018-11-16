package com.hclz.client.base.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.hclz.client.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


/**
 * 图片工具类
 *
 * @author shanjainfei
 */
public class ImageUtility {

    private static ImageUtility mImageUtility;
    //    //===============默认显示设置
//    private final DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//            .showImageForEmptyUri(R.mipmap.the_picture)
//            .showImageOnFail(R.mipmap.the_picture)
//            .cacheInMemory(true)
//            .displayer(new RoundedBitmapDisplayer(0)).build();
    private Context mContext;
//    //===============ImageLoader

    private ImageUtility(Context context) {
        init(context);
    }

    public static ImageUtility getInstance(Context context) {
        if (mImageUtility == null) {
            mImageUtility = new ImageUtility(context);
        }
        return mImageUtility;
    }

    //===============创建ImageView Start=================================================
    public static ImageView createImageView(Context context) {

        ImageView imageView = new ImageView(context);

        imageView.setScaleType(ScaleType.CENTER_CROP);
        return imageView;
    }

    //===============压缩图片,用来显示 Start=================================================
    public static Bitmap CompressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置采样率
        newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    //===============构造函数,初始化 Start=================================================
    public void init(Context context) {
        mContext = context;
    }

    //===============显示图片 Start=================================================
    public void showImage(String url, ImageView imageView) {
        Glide
                .with(mContext.getApplicationContext())
                .load(url)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

    //===============显示图片 包含默认 Start=================================================
    public void showImage(String url, ImageView imageView, int defImgId) {
        Glide
                .with(mContext.getApplicationContext())
                .load(url)
                .centerCrop()
                .placeholder(defImgId)
                .crossFade()
                .into(imageView);
    }

    public void clearCache() {
        // 必须在UI线程中调用
        Glide.get(mContext).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 必须在后台线程中调用，建议同时clearMemory()
                Glide.get(mContext).clearDiskCache();
            }
        }).start();
    }
}
