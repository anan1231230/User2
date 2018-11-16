/**
 * 类       名:DisplayImageUtil
 * 作       者:
 * 主要功能:图片工具类
 * 创建日期：2015-09-11 10:33:00
 * 修  改  者：
 * 修改日期：
 * 修改内容：
 */

package com.hclz.client.base.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class DisplayImageUtil {

    /**
     * ImageLoader
     */
    private final ImageLoader imageLoader = ImageLoader.getInstance();
    /**
     * 默认显示设置
     */
    private final DisplayImageOptions defaultOptions;
    /**
     * 载入监听器
     */
    private final ImageLoadingListener loadingListener = new SimpleImageLoadingListener() {

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {

            // 图片淡入效果
            FadeInBitmapDisplayer.animate((ImageView) view, 500);
        }
    };

    /**
     * 构造函数
     *
     * @param context Context
     */
    public DisplayImageUtil(Context context) {

        defaultOptions = new DisplayImageOptions.Builder()
//				.showStubImage(R.drawable.ic_launcher)
//				.showImageForEmptyUri(R.drawable.ic_launcher)
//				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
                .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(0))
                .build();
        imageLoader.init(new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions).build());
    }

    /**
     * 创建ImageView。
     *
     * @param context
     * @return ImageView
     */
    public static ImageView createImageView(Context context) {

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        return imageView;
    }

    /**
     * 压缩显示图片
     *
     * @param srcPath 图片路径
     * @return 图片
     */
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
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * 显示图片。
     *
     * @param url       图片路径
     * @param imageView ImageView
     */
    public void displayImage(String url, ImageView imageView) {

        imageLoader.displayImage(url, imageView, loadingListener);
    }

    /**
     * 显示图片。
     *
     * @param url       图片路径
     * @param imageView ImageView
     * @param defImgId  默认图片ID
     */
    public void displayImage(String url, ImageView imageView, int defImgId) {

        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .cloneFrom(defaultOptions).showStubImage(defImgId)
                .showImageForEmptyUri(defImgId).showImageOnFail(defImgId);
        imageLoader.displayImage(url, imageView, builder.build(),
                loadingListener);
    }

}
