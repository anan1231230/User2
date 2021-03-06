package com.hclz.client.faxian.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by handsome on 16/6/17.
 */
public class HeightWidth310v720layout extends LinearLayout {
    public HeightWidth310v720layout(Context context) {
        super(context);
    }

    public HeightWidth310v720layout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeightWidth310v720layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        int width = getWidth();
        height = width * 310 / 720 + 1;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
