/**
 * 类       名: GuidPagerAdapter
 * 作       者:杨斌
 * 主要功能:项目的引导页的Adapter
 * 创建日期：2015-09-22 17:05:00
 * 修  改  者：
 * 修改日期：
 * 修改内容：
 */

package com.hclz.client.base.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class GuidPagerAdapter extends PagerAdapter {

    private ArrayList<View> pagers;

    public GuidPagerAdapter(ArrayList<View> list) {
        this.pagers = list;
    }

    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(pagers.get(arg1));
    }

    @Override
    public void finishUpdate(ViewGroup arg0) {

    }

    @Override
    public int getCount() {
        return pagers.size();
    }

    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {
        ((ViewPager) arg0).addView(pagers.get(arg1), 0);
        return pagers.get(arg1);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(ViewGroup arg0) {

    }

}
