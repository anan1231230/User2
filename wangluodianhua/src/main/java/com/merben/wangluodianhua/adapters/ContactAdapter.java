package com.merben.wangluodianhua.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.merben.wangluodianhua.R;
import com.merben.wangluodianhua.listeners.ContactClickListener;
import com.merben.wangluodianhua.util.ContactBean;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by handsome on 16/1/22.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.StringHolder> implements SectionIndexer {

    LayoutInflater mInflater;
    Activity mContext;
    List<ContactBean> mDatas = new ArrayList<>();
    ContactClickListener mListener;

    public ContactAdapter(Activity context, ContactClickListener contactClickListener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mListener = contactClickListener;
    }

    public void setData(List<ContactBean> datas) {
        if (datas == null || datas.size() <= 0) {
            mDatas = new ArrayList<>();
        } else {
            mDatas = datas;
        }
    }

    @Override
    public StringHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_contact, parent, false);
        return new StringHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StringHolder holder, int position) {
        TextView tvText = ((StringHolder) holder).tvText;
        TextView tvPhone = ((StringHolder) holder).tvPhone;
        TextView tvLetter = ((StringHolder) holder).tvLetter;
        tvText.setText(mDatas.get(position).getDesplayName());
        tvPhone.setText(mDatas.get(position).getPhoneNum());
        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            tvLetter.setVisibility(View.VISIBLE);
            tvLetter.setText(mDatas.get(position).getSortLetters());
        } else {
            tvLetter.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mDatas.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    public int getSectionForPosition(int position) {
        return mDatas.get(position).getSortLetters().charAt(0);
    }

    public class StringHolder extends RecyclerView.ViewHolder {
        private TextView tvText, tvPhone ,tvLetter;
        private LinearLayout llContact;

        public StringHolder(View itemView) {
            super(itemView);
            llContact = (LinearLayout) itemView.findViewById(R.id.ll_contact);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            tvLetter = (TextView) itemView.findViewById(R.id.catalog);
            llContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onContactClick(mDatas.get(getLayoutPosition()));
                }
            });
        }
    }


}
