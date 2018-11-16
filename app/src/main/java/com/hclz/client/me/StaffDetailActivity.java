package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.order.confirmorder.bean.address.NetAddress;
import com.hclz.client.order.confirmorder.bean.staff.NetStaff;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 员工信息编辑
 */
public class StaffDetailActivity extends BaseActivity implements OnClickListener{
    //百度搜索
    private String city = ProjectConstant.DEFAULT_CITY;

    private TextView staffName,staffJob;
    private TextView staffPhonenum;
    private Button btnOk;

    private NetStaff staff;
    private boolean isDefault;




    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from, NetStaff staff) {
        Intent intent = new Intent(from, StaffDetailActivity.class);
        if (staff != null) {
            intent.putExtra("staff", staff);
            if (staff.getIs_default() == 1) {
                intent.putExtra("isDefault", true);
            }
        }
        from.startActivity(intent);
    }

    public static void startMe(Context from) {
        Intent intent = new Intent(from, StaffDetailActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_staff_detail);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView() {

        staffName = (TextView) findViewById(R.id.staff_name);
        staffPhonenum = (TextView) findViewById(R.id.staff_phonenum);
        staffJob=(TextView)findViewById(R.id.staff_job);
        btnOk = (Button) findViewById(R.id.btn_ok);
    }

    @Override
    protected void initInstance() {

    }

    @Override
    protected void initData() {
        if (mIntent != null) {
            staff = (NetStaff) mIntent.getSerializableExtra("staff");
            isDefault = mIntent.getBooleanExtra("isDefault", false);
        }
        if (staff != null) {
            setCommonTitle(R.string.info_staff);
            staffName.setText(staff.getStaff_name());
            staffPhonenum.setText(staff.getStaff_phone());
            staffJob.setText(staff.getStaff_job());

        } else {
            setCommonTitle(R.string.info_staff);
        }
    }

    @Override
    protected void initListener() {
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                finish();
               StaffActivity.startMe(mContext,staff);
                break;
            default:
                break;
        }
    }



}
