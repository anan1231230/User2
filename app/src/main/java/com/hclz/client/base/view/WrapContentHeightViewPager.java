package com.hclz.client.base.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class WrapContentHeightViewPager extends ViewPager {

    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        int height = 0;
//        int width = getWidth();
//        //下面遍历所有child的高度
////        for (int i = 0; i < getChildCount(); i++) {
////            View child = getChildAt(i);
////            child.measure(widthMeasureSpec,
////                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
////            int h = child.getMeasuredHeight();
////            if (h > height) //采用最大的view的高度。
////                height = h;
////        }
//
//        height = width / 6 + 1;
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
//                MeasureSpec.EXACTLY);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = 0;
        int width = getWidth();
        height = width * 1 / 2;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
