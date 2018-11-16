package com.hclz.client.faxian.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hclz.client.base.util.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by lt on 2015/12/14.
 */
public class TabFragmentAdapter extends PagerAdapter {

    private List<View> mViewList;
    private Context mContext;

    public TabFragmentAdapter(List<View> mViewList, Context context) {
        this.mViewList = mViewList;
        this.mContext = context;
    }

    public void setData(List<View> mViewList) {
        this.mViewList = mViewList;
    }

    @Override
    public int getCount() {
        return mViewList == null ? 0 : mViewList.size();//页卡数
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;//官方推荐写法
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));//添加页卡
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));//删除页卡
    }

    @Override
    public CharSequence getPageTitle(int position) {
//            return position == 0 ? "来点" : position == 1 ? "点餐":"家居";
        return position == 0 ? "特色" : position == 1 ? "点餐" : "";
    }
}
