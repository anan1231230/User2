package com.hclz.client.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hclz.client.R;

public abstract class SanmiAdapter extends BaseAdapter {
    public int VIEWTYPE_EMPTY = 1;
    protected Context mContext;
    private String emptyString = "没有获取到合适的数据";
    private TextView emptyTextView;

    public SanmiAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取列表为空时的显示View(调用此方法(不重写getItemViewType时)需重写isEmpty()方法)
     *
     * @return a view 传递getView方法中的ViewGroup参数即可
     */
    public View getEmptyView(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.empty_common_item, null);
        emptyTextView = (TextView) view.findViewById(R.id.textview);
        emptyTextView.setText(emptyString);
        int width = parent.getWidth();
        int height = parent.getHeight();
        LayoutParams params = new LayoutParams(width, height);
        view.setLayoutParams(params);
        return view;
    }

    /**
     * 设置空列表提示语
     *
     * @param emptyString
     */
    public void setEmptyString(String emptyString) {
        if (emptyTextView != null)
            emptyTextView.setText(emptyString);
        this.emptyString = emptyString;
    }

    /**
     * 设置空列表提示语
     *
     * @param emptyStrID
     */
    public void setEmptyString(int emptyStrID) {
        emptyString = mContext.getResources().getString(emptyStrID);
        setEmptyString(emptyString);
    }

}
