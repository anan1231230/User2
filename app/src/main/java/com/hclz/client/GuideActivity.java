/**
 * 类       名:GuideActivity
 * 作       者:杨斌
 * 主要功能:项目的引导页
 * 创建日期：2015-09-22 16:56:00
 * 修  改  者：
 * 修改日期：
 * 修改内容：
 */
package com.hclz.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hclz.client.base.adapter.GuidPagerAdapter;
import com.hclz.client.base.ui.BaseActivity;

import java.util.ArrayList;

public class GuideActivity extends BaseActivity {

    private ViewPager vpGuide;
    private LayoutInflater inflater;
    private ArrayList<View> pagers;
    private View guide1, guide2, guide3;
    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private int previousPage;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from) {
        Intent intent = new Intent(from, GuideActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_guide);
        super.onCreate(savedInstanceState);
//        setStatusBarEffect(com.diandi.client.R.color.guide1);
    }

    @Override
    protected void initView() {
        pagers = new ArrayList<View>();
        inflater = LayoutInflater.from(mContext);

        guide1 = (View) inflater.inflate(R.layout.activity_start_guid1, null);
        guide2 = (View) inflater.inflate(R.layout.activity_start_guid2, null);
        guide3 = (View) inflater.inflate(R.layout.activity_start_guid3, null);
        pagers.add(guide1);
        pagers.add(guide2);
        pagers.add(guide3);

        vpGuide = (ViewPager) findViewById(R.id.vpGuide);
        GuidPagerAdapter adapter = new GuidPagerAdapter(pagers);
        vpGuide.setAdapter(adapter);
        rg = (RadioGroup) findViewById(R.id.rg);
        rb1 = (RadioButton) findViewById(R.id.rbGuid1);
        rb2 = (RadioButton) findViewById(R.id.rbGuid2);
        rb3 = (RadioButton) findViewById(R.id.rbGuid3);
    }

//    private void setStatusBarEffect(int color) {
//        /*add title bar background [START]*/
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
//
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(color);
//        /*add title bar background[END]*/
//    }
//
//    @TargetApi(19)
//    private void setTranslucentStatus(boolean on) {
//        Window win = getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        win.setAttributes(winParams);
//    }

    @Override
    protected void initInstance() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void initListener() {
//		btnNext.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(mContext, MainActivity.class);
//				startActivity(intent);
//				finish();
//			}
//		});
        vpGuide.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (arg0 == pagers.size() - 1 && previousPage == pagers.size() - 1) {
                    MainActivity.startMe(mContext);
                    finish();
                } else {
                    previousPage = arg0;
//                    if (arg0 == 0) {
//                        setStatusBarEffect(R.color.guide1);
//                    } else if (arg0 == 1) {
//                        setStatusBarEffect(R.color.guide2);
//
//                    } else {
//                        setStatusBarEffect(R.color.guide3);
//                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    int page = vpGuide.getCurrentItem();
                    System.out.println("_---------------------------" + page);
                    if (page == 0) {
                        rg.check(rb1.getId());
                    } else if (page == 1) {
                        rg.check(rb2.getId());
                    } else {
                        rg.check(rb3.getId());
                    }
                }
            }
        });
    }

}
