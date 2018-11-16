package com.hclz.client.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hclz.client.base.ui.BaseVFragment;

import java.util.ArrayList;

/***
 * @author jia-changyu
 */
public class BaseVPAdapter extends FragmentPagerAdapter {
    private ArrayList<BaseVFragment> fragmentList;

    public BaseVPAdapter(FragmentManager fm,
                         ArrayList<BaseVFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        BaseVFragment fragment = fragmentList.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


}
