package com.hclz.client.forcshop.jiedanguanli;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.ui.BaseVFragment;
import com.hclz.client.base.view.BadgeView;
import com.hclz.client.forcshop.jiedanguanli.bean.BadgeBean;
import com.hclz.client.forcshop.jiedanguanli.fragment.ChengdaiDongxiangFragment;
import com.hclz.client.forcshop.jiedanguanli.fragment.JieHuoGuanliFragment;
import com.hclz.client.forcshop.jiedanguanli.fragment.JiedanGuanliFragment;
import com.hclz.client.forcshop.jiedanguanli.fragment.SongHuoGuanliFragment;

import java.util.ArrayList;

/**
 * Created by handsome on 16/7/7.
 */
public class JiedanGuanliActivity extends BaseAppCompatActivity {

    TabLayout mTablayout;
    ViewPager mViewPager;
    ArrayList<BaseVFragment> mFragments;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TextView badgeOneTv, badgeTwoTv, badgeThreeTv, badgeFourTv, mOrderHistory;
    private BadgeView badgeOne, badgeTwo, badgeThree, badgeFour;

    /**
     * 页面跳转,不需要传参数过来
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, JiedanGuanliActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static JiedanGuanliFragment newInstance() {
        JiedanGuanliFragment fragment = new JiedanGuanliFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_jiedanguanli);
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.container);
        mTablayout = (TabLayout) findViewById(R.id.tabs);
        badgeOneTv = (TextView) findViewById(R.id.bage_one);
        badgeTwoTv = (TextView) findViewById(R.id.bage_two);
        badgeThreeTv = (TextView) findViewById(R.id.bage_three);
        badgeFourTv = (TextView) findViewById(R.id.bage_four);
        mOrderHistory = (TextView) findViewById(R.id.jiedanguanli_order_history);
        setCommonTitle("接单管理");
    }

    @Override
    protected void initInstance() {
        mFragments = new ArrayList<>();
        mFragments.add(ChengdaiDongxiangFragment.newInstance());
        mFragments.add(JieHuoGuanliFragment.newInstance());
        mFragments.add(JiedanGuanliFragment.newInstance());
        mFragments.add(SongHuoGuanliFragment.newInstance());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTablayout.setupWithViewPager(mViewPager);

        badgeOne = new BadgeView(mContext,badgeOneTv);
        badgeTwo = new BadgeView(mContext,badgeTwoTv);
        badgeThree = new BadgeView(mContext,badgeThreeTv);
        badgeFour = new BadgeView(mContext,badgeFourTv);
        setBadge();
    }

    public void refresh() {
        if (mFragments != null) {
            for (BaseVFragment fragment : mFragments) {
                fragment.refresh();
            }
        }
    }

    public void setBadge(){
        String badgeOneNum = "",badgeTwoNum = "",badgeThreeNum = "",badgeFourNum = "";
        badgeOneNum = TextUtils.isEmpty(BadgeBean.getInstence().badges[0]) ? "0" : BadgeBean.getInstence().badges[0];
        badgeTwoNum = TextUtils.isEmpty(BadgeBean.getInstence().badges[1]) ? "0" : BadgeBean.getInstence().badges[1];
        badgeThreeNum = TextUtils.isEmpty(BadgeBean.getInstence().badges[2]) ? "0" : BadgeBean.getInstence().badges[2];
        badgeFourNum = TextUtils.isEmpty(BadgeBean.getInstence().badges[3]) ? "0" : BadgeBean.getInstence().badges[3];
        if (badgeOne != null) {
            badgeOne.setText(badgeOneNum);
            if ("0".equals(badgeOneNum) && badgeOne.isShown()) {
                badgeOne.hide();
            } else if (!"0".equals(badgeOneNum) && !badgeOne.isShown()) {
                badgeOne.show();
            }
        }
        if (badgeTwo != null) {
            badgeTwo.setText(badgeTwoNum);
            if ("0".equals(badgeTwoNum) && badgeTwo.isShown()) {
                badgeTwo.hide();
            } else if (!"0".equals(badgeTwoNum) && !badgeTwo.isShown()) {
                badgeTwo.show();
            }
        }
        if (badgeThree != null) {
            badgeThree.setText(badgeThreeNum);
            if ("0".equals(badgeThreeNum) && badgeThree.isShown()) {
                badgeThree.hide();
            } else if (!"0".equals(badgeThreeNum) && !badgeThree.isShown()) {
                badgeThree.show();
            }
        }
        if (badgeFour != null) {
            badgeFour.setText(badgeFourNum);
            if ("0".equals(badgeFourNum) && badgeFour.isShown()) {
                badgeFour.hide();
            } else if (!"0".equals(badgeFourNum) && !badgeFour.isShown()) {
                badgeFour.show();
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JiedanHistoryActivity.startMe(mContext);
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<BaseVFragment> fragmets;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<BaseVFragment> fragments) {
            super(fm);
            this.fragmets = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmets.get(position);
        }

        @Override
        public int getCount() {
            return fragmets.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "缺货订单";
                case 1:
                    return "城代订单";
                case 2:
                    return "自己订单";
                case 3:
                    return "未送订单";
            }
            return null;
        }
    }

}
