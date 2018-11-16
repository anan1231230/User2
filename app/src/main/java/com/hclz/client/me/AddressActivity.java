package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.bean.Location;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.kitchen.adapter.LocationAdapter;
import com.hclz.client.order.confirmorder.bean.address.NetAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;

public class AddressActivity extends BaseActivity implements OnClickListener,OnAddressSelectedListener {
    //百度搜索
    private String city = ProjectConstant.DEFAULT_CITY;

    private EditText etName;
    private EditText etPhonenum;
    private TextView etAddress;
    private ListView lvAddress;
    private EditText etAddressDetail;
    private ImageView ivMoren;
    private Button btnOk;

    private LocationAdapter mAdapter = null;
    private NetAddress address;
    private boolean isDefault;
    private double longitude = 0;
    private double latitude = 0;
    private String selectAddress;
    private List<NetAddress> addresses;

    BottomDialog mAddressDialog;


    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from, NetAddress address) {
        Intent intent = new Intent(from, AddressActivity.class);
        if (address != null) {
            intent.putExtra("address", address);
            if (address.is_default == 1) {
                intent.putExtra("isDefault", true);
            }
        }
        from.startActivity(intent);
    }

    public static void startMe(Context from) {
        Intent intent = new Intent(from, AddressActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_address);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView() {

        etName = (EditText) findViewById(R.id.et_name);
        etPhonenum = (EditText) findViewById(R.id.et_phonenum);
        etAddress = (TextView) findViewById(R.id.et_address);
        lvAddress = (ListView) findViewById(R.id.lv_address);
        mAdapter = new LocationAdapter(mContext);
        View head = LayoutInflater.from(mContext).inflate(R.layout.address_head, null);
        lvAddress.addHeaderView(head);
        lvAddress.setAdapter(mAdapter);
        etAddressDetail = (EditText) findViewById(R.id.et_address_detail);
        ivMoren = (ImageView) findViewById(R.id.iv_moren);
        btnOk = (Button) findViewById(R.id.btn_ok);
    }

    @Override
    protected void initInstance() {
        mAddressDialog= new BottomDialog(AddressActivity.this);
        mAddressDialog.setOnAddressSelectedListener(AddressActivity.this);
    }

    @Override
    protected void initData() {
        if (mIntent != null) {
            address = (NetAddress) mIntent.getSerializableExtra("address");
            isDefault = mIntent.getBooleanExtra("isDefault", false);
        }
        if (address != null) {
            setCommonTitle(R.string.edit_address);
            etName.setText(address.getName());
            etPhonenum.setText(address.getPhone());
            if (TextUtils.isEmpty(address.province)) {
                etAddress.setText("");
                etAddressDetail.setText("");
            } else {
                etAddress.setText(address.getSanji());
                etAddressDetail.setText(address.getAddr_detail());
            }
            if (isDefault) {
                ivMoren.setBackgroundResource(R.mipmap.btn_slide_pre);
            }
            longitude = 0;
            latitude = 0;
        } else {
            setCommonTitle(R.string.add_address);
        }
    }

    @Override
    protected void initListener() {

        etAddress.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        ivMoren.setOnClickListener(this);
        lvAddress.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                if (position == 0) {
                    lvAddress.setVisibility(View.GONE);
                    return;
                }
                Location location = (Location) mAdapter.getItem(position - 1);
                selectAddress = location.getName();
                etAddress.setText(selectAddress);
                longitude = location.getPosition().getLocation()[0];
                latitude = location.getPosition().getLocation()[1];
                lvAddress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_address:
                if (mAddressDialog != null){
                    etAddress.setText("");
                    mAddressDialog.show();
                }
                break;
            case R.id.iv_moren:
                isDefault = !isDefault;
                if (isDefault) {
                    ivMoren.setBackgroundResource(R.mipmap.btn_slide_pre);
                } else {
                    ivMoren.setBackgroundResource(R.mipmap.btn_slide);
                }
                break;
            case R.id.btn_ok:
                String name = etName.getText().toString();
                String phoneNum = etPhonenum.getText().toString();
                String addressInfo = etAddress.getText().toString();
                String addreddDetail = etAddressDetail.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    etName.requestFocus();
                    etName.setError(getString(R.string.contact_user_empty));
                    return;
                }
                if (TextUtils.isEmpty(phoneNum)) {
                    etPhonenum.requestFocus();
                    etPhonenum.setError(getString(R.string.contact_phone_empty));
                    return;
                }
                if (TextUtils.isEmpty(addressInfo)) {
                    etAddress.requestFocus();
                    etAddress.setError("请选择省市区列表");
                    return;
                }
                if (TextUtils.isEmpty(addreddDetail)) {
                    etAddressDetail.requestFocus();
                    etAddressDetail.setError("请输入详细地址");
                    return;
                }
                if (address == null) {
                    address = new NetAddress();
                }

                if (longitude == 0 || latitude == 0) {
                    longitude = 38.65777;
                    latitude = 104.08296;
                }
                address.receiver_name = name;
                address.receiver_phone = phoneNum;
                address.is_default = isDefault? 1:0;
                String[] sanji = addressInfo.trim().split(" ");
                if (sanji.length >= 1){
                    address.province = sanji[0];
                }
                if (sanji.length >= 2){
                    address.city = sanji[1];
                }
                if (sanji.length >= 3){
                    address.district = sanji[2];
                }
                if (sanji.length >= 4) {
                    address.addr_main = sanji[3];
                } else {
                    address.addr_main = "";
                }
                address.addr_detail = addreddDetail;
//                address.setLocation(new double[]{longitude, latitude});
                uploadAddress();
                break;
            default:
                break;
        }
    }

    private void uploadAddress() {
        JSONObject contentObj = null;
        try {
            configMap = HclzApplication.getData();
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);

            contentObj.put(ProjectConstant.APP_USER_MID,
                    SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
            JSONObject addressObj = new JSONObject();
            addressObj.put("addressid", address.getAddressId() != null ? address.getAddressId() : null);
            addressObj.put("receiver_name", address.receiver_name);
            addressObj.put("receiver_phone", address.receiver_phone);
            addressObj.put("province", address.province == null ?  "" : address.province);
            addressObj.put("city", address.city == null ? "" : address.city);
            addressObj.put("district", address.district == null ? "" : address.district);
            addressObj.put("addr_main", address.addr_main == null ? "" : address.addr_main);
            addressObj.put("addr_detail", address.addr_detail == null ? "" : address.addr_detail);
            addressObj.put("is_default", address.is_default);
            contentObj.put("addr_info", addressObj);
            String content = contentObj.toString();
            PostHttpUtil.prepareParams(requestParams, content);
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_UPLOAD_ADDRESS.getUserMethod(),
                    requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            ToastUtil.showToast(mContext, getString(R.string.success));
                            finish();
                        }

                        @Override
                        public void callBackAddressError() {
                            etAddress.setText("");
                            etAddressDetail.setText("");
                            ToastUtil.showToast(mContext, "地址填写有误,请重新填写");
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String address =
                (province == null ? "" : province.name) + " " +
                        (city == null ? "" : "\n" + city.name) + " " +
                        (county == null ? "" : "\n" + county.name) + " " +
                        (street == null ? "" : "\n" + street.name);

        etAddress.setText(address);
        if (mAddressDialog != null) {
            mAddressDialog.dismiss();
        }

    }
}
