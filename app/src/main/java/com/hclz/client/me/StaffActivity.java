package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.order.confirmorder.bean.staff.NetStaff;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 员工信息编辑
 */
public class StaffActivity extends BaseActivity implements OnClickListener {
    //百度搜索
    private String city = ProjectConstant.DEFAULT_CITY;

    private EditText staffName;
    private EditText staffPhonenum, staffJob;
    private Button btnOk;

    private NetStaff staff;
    private boolean isDefault;
    String name, phoneNum, job, staffId;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from, NetStaff staff) {
        Intent intent = new Intent(from, StaffActivity.class);
        if (staff != null) {
            intent.putExtra("staff", staff);
            if (staff.getIs_default() == 1) {
                intent.putExtra("isDefault", true);
            }
        }
        from.startActivity(intent);
    }

    public static void startMe(Context from) {
        Intent intent = new Intent(from, StaffActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_staff);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView() {

        staffName = (EditText) findViewById(R.id.staff_name);
        staffPhonenum = (EditText) findViewById(R.id.staff_phonenum);
        staffJob = (EditText) findViewById(R.id.staff_job);
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
        setCommonTitle(R.string.info_staff);
        if (staff != null) {
            staffName.setText(staff.getStaff_name());
            staffPhonenum.setText(staff.getStaff_phone());
            staffJob.setText(staff.getStaff_job());
            staffId=staff.getStaff_id();
            staffPhonenum.setFocusable(false);
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
                 name = staffName.getText().toString();
                 phoneNum = staffPhonenum.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    staffName.requestFocus();
                    staffName.setError(getString(R.string.contact_staff_name_empty));
                    return;
                }
                if(name.length()>5){
                    ToastUtil.showToast(mContext,"姓名长度不能大于5个");
                    return;
                }
                if (TextUtils.isEmpty(phoneNum)) {
                    staffPhonenum.requestFocus();
                    staffPhonenum.setError(getString(R.string.contact_phone_empty));
                    return;
                }

//                staff.receiver_name = name;
//                staff.receiver_phone = phoneNum;
//                staff.is_default = isDefault? 1:0;

                uploadStaff();
                break;
            default:
                break;
        }
    }

    private void uploadStaff() {
        JSONObject contentObj = null;
        try {
            configMap = HclzApplication.getData();
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);

            contentObj.put(ProjectConstant.APP_USER_MID,
                    SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
            job = staffJob.getText().toString();

            JSONObject staffObj = new JSONObject();
            staffObj.put("staff_id", staffId == null ? "" : staffId);//nullpointer
            staffObj.put("staff_name", name);
            staffObj.put("staff_phone", phoneNum);
            staffObj.put("staff_job", job == null ? "" : job);
            contentObj.put("staff_info", staffObj);
            String content = contentObj.toString();
            PostHttpUtil.prepareParams(requestParams, content);
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_UPLOAD_STAFF.getUserMethod(),
                    requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            ToastUtil.showToast(mContext, getString(R.string.success));
                            finish();
                        }

                        @Override
                        public void callBackStaffError() {
                            ToastUtil.showToast(mContext, "员工信息不存在");
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
