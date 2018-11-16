package com.merben.wangluodianhua.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merben.wangluodianhua.R;
import com.merben.wangluodianhua.activitys.CallActivity;
import com.merben.wangluodianhua.util.ContactBean;
import com.merben.wangluodianhua.util.ToastUtil;

public class DialPadFragment extends Fragment implements View.OnClickListener {
    private TextView one, two, three, four, five, six, seven, eight, nine, zero, plus, haoma ,rightJing;
    private ImageView delete;
    private LinearLayout jing;
    private ContactBean mDialContact;

    public DialPadFragment() {
    }

    public static DialPadFragment newInstance() {
        DialPadFragment fragment = new DialPadFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialpad, container, false);
        one = (TextView) rootView.findViewById(R.id.one);
        two = (TextView) rootView.findViewById(R.id.two);
        three = (TextView) rootView.findViewById(R.id.three);
        four = (TextView) rootView.findViewById(R.id.four);
        five = (TextView) rootView.findViewById(R.id.five);
        six = (TextView) rootView.findViewById(R.id.six);
        seven = (TextView) rootView.findViewById(R.id.seven);
        eight = (TextView) rootView.findViewById(R.id.eight);
        nine = (TextView) rootView.findViewById(R.id.nine);
        zero = (TextView) rootView.findViewById(R.id.zero);
        plus = (TextView) rootView.findViewById(R.id.plus);
        jing = (LinearLayout) rootView.findViewById(R.id.jing);
        delete = (ImageView) rootView.findViewById(R.id.delete);
        haoma = (TextView) rootView.findViewById(R.id.haoma);
        rightJing = (TextView) rootView.findViewById(R.id.right_jing);
        mDialContact = new ContactBean();
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        plus.setOnClickListener(this);
        jing.setOnClickListener(this);
        delete.setOnClickListener(this);
        rightJing.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        StringBuilder sb;
        int i = v.getId();
        if (i == R.id.one) {
            sb = new StringBuilder(haoma.getText().toString());
            sb.append("1");
            mDialContact.setPhoneNum(sb.toString());
            haoma.setText(sb.toString());

        } else if (i == R.id.two) {
            sb = new StringBuilder(haoma.getText().toString());
            sb.append("2");
            mDialContact.setPhoneNum(sb.toString());
            haoma.setText(sb.toString());

        } else if (i == R.id.three) {
            sb = new StringBuilder(haoma.getText().toString());
            sb.append("3");
            mDialContact.setPhoneNum(sb.toString());
            haoma.setText(sb.toString());

        } else if (i == R.id.four) {
            sb = new StringBuilder(haoma.getText().toString());
            sb.append("4");
            mDialContact.setPhoneNum(sb.toString());
            haoma.setText(sb.toString());

        } else if (i == R.id.five) {
            sb = new StringBuilder(haoma.getText().toString());
            sb.append("5");
            mDialContact.setPhoneNum(sb.toString());
            haoma.setText(sb.toString());

        } else if (i == R.id.six) {
            sb = new StringBuilder(haoma.getText().toString());
            sb.append("6");
            mDialContact.setPhoneNum(sb.toString());
            haoma.setText(sb.toString());

        } else if (i == R.id.seven) {
            sb = new StringBuilder(haoma.getText().toString());
            sb.append("7");
            mDialContact.setPhoneNum(sb.toString());
            haoma.setText(sb.toString());

        } else if (i == R.id.eight) {
            sb = new StringBuilder(haoma.getText().toString());
            sb.append("8");
            mDialContact.setPhoneNum(sb.toString());
            haoma.setText(sb.toString());

        } else if (i == R.id.nine) {
            sb = new StringBuilder(haoma.getText().toString());
            sb.append("9");
            mDialContact.setPhoneNum(sb.toString());
            haoma.setText(sb.toString());

        } else if (i == R.id.zero) {
            sb = new StringBuilder(haoma.getText().toString());
            sb.append("0");
            mDialContact.setPhoneNum(sb.toString());
            haoma.setText(sb.toString());

        } else if (i == R.id.plus) {
            sb = new StringBuilder(haoma.getText().toString());
            sb.append("*");
            mDialContact.setPhoneNum(sb.toString());
            haoma.setText(sb.toString());

        }else if (i == R.id.right_jing){
            sb = new StringBuilder(haoma.getText().toString());
            sb.append("#");
            mDialContact.setPhoneNum(sb.toString());
            haoma.setText(sb.toString());
        } else if (i == R.id.jing) {
            if (mDialContact == null || mDialContact.getPhoneNum() == null || mDialContact.getPhoneNum().length() < 11 || mDialContact.getPhoneNum().length() > 14) {
                ToastUtil.showToast(getActivity(), "请输入正确的号码,座机请加区号");
            } else {
                Intent intent = new Intent(getActivity(), CallActivity.class);
                intent.putExtra("contact", mDialContact);
                startActivity(intent);
            }

        } else if (i == R.id.delete) {
            String s = haoma.getText().toString();
            if (!TextUtils.isEmpty(s)) {
                mDialContact.setPhoneNum(s.substring(0, s.length() - 1));
                haoma.setText(s.substring(0, s.length() - 1));
            } else {
                mDialContact.setPhoneNum(s);
                haoma.setText(s);
            }

        } else {
        }

    }
}