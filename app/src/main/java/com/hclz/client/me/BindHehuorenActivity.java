package com.hclz.client.me;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.bean.Kitchen;
import com.hclz.client.base.bean.Position;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.location.LocationListener;
import com.hclz.client.base.location.LocationUtils;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.view.WaitingDialogControll;
import com.hclz.client.me.adapter.BindHehuorenAdapter;
import com.hclz.client.order.confirmorder.ConfirmOrder2Activity;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by handsome on 16/6/21.
 */
public class BindHehuorenActivity extends BaseActivity {

    ImageView iv_back, iv_btntuijianma;
    EditText et_search;
    TextView tv_yibangding, tv_empty;
    RelativeLayout rl_bangding_panel;
    RecyclerView rv_hehuoren;
    LinearLayoutManager mLayoutManager;
    BindHehuorenAdapter mAdapter;
    BindHehuorenAdapter.HehuorenSelectListener mListener;
    SwipeRefreshLayout mSwipe;

    //menu
    private DrawerLayout dlDrawlayout;
    private TextView tv_erweimasaomiao,tv_shurutuijianma;


    Kitchen mKitchenYibangding;
    int bindState = 0;
    ArrayList<Kitchen> mKitchens;
    private boolean isFromCreate = false;
    private double longitude;//定位的当前用户的经度
    private double latitude;//纬度
    private Position mPosition;

    private static final int REQUEST_CODE = 100;
    private static final int CAMERA_CODE = 99;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from){
        Intent intent = new Intent(from, BindHehuorenActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_bind_hehuoren);
        isFromCreate = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFromCreate) {
            initData();
        } else {
            isFromCreate = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationUtils.getInstence().stop();
    }

    @Override
    protected void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_search = (EditText) findViewById(R.id.et_search);
        iv_btntuijianma = (ImageView) findViewById(R.id.iv_btntuijianma);
        tv_yibangding = (TextView) findViewById(R.id.tv_yibangding);
        rl_bangding_panel = (RelativeLayout) findViewById(R.id.rl_bangding_panel);
        rv_hehuoren = (RecyclerView) findViewById(R.id.rv_hehuoren);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        mSwipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipe.setColorSchemeResources(R.color.blue, R.color.yellow, R.color.cyan,
                R.color.red);
        dlDrawlayout = (DrawerLayout) findViewById(R.id.dl_drawlayout);
        tv_erweimasaomiao = (TextView) findViewById(R.id.tv_erweimasaomiao);
        tv_shurutuijianma = (TextView) findViewById(R.id.tv_shuruerweima);

    }

    @Override
    protected void initInstance() {
        mLayoutManager = new LinearLayoutManager(mContext);
        mListener = new BindHehuorenAdapter.HehuorenSelectListener() {
            @Override
            public void onHehuorenSelected(final Kitchen item) {
                new AlertDialog.Builder(mContext).setTitle("您确定要换绑定的合伙人吗?")
                        .setPositiveButton("绑定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bindHehuoren(item);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DO NOTHING
                    }
                }).show();
            }
        };
        mAdapter = new BindHehuorenAdapter(mContext, mListener);
        rv_hehuoren.setLayoutManager(mLayoutManager);
        rv_hehuoren.setAdapter(mAdapter);

    }

    String mFrom;
    @Override
    protected void initData() {
        configMap = HclzApplication.getData();
        mFrom = getIntent().getStringExtra("from");
        getYibangding();
    }

    private void getYibangding() {
        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            configMap = HclzApplication.getData();
//            content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            content = PostHttpUtil.prepareContents(configMap,mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.CSHOP_USER_BIND_QUERY.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            bindState = JsonUtility.getAsInt(obj.get("bind_state"));
                            mKitchenYibangding = JsonUtility.fromJson(obj.get("cshop"),
                                    Kitchen.class);
                            showBangding();
                            getHehuoren();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void bindHehuoren(Kitchen item) {
        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            configMap = HclzApplication.getData();
//            content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            content = PostHttpUtil.prepareContents(configMap,mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            content.put("code", item.getCode());
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.CSHOP_USER_BIND.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            mKitchenYibangding = JsonUtility.fromJson(obj.get("cshop"),
                                    Kitchen.class);
                            bindState = 1;
                            ToastUtil.showToast(mContext, "绑定成功!");
                            showBangding();
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void getHehuoren() {
        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            configMap = HclzApplication.getData();
//            content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//            content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
            content = PostHttpUtil.prepareContents(configMap,mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            JSONObject positionObj = new JSONObject();
            JSONArray locationArray = new JSONArray();
            if (!TextUtils.isEmpty(SharedPreferencesUtil.get(
                    mContext, ProjectConstant.APP_START_LONGITUDE))){
                longitude = Double.parseDouble(SharedPreferencesUtil.get(
                        mContext, ProjectConstant.APP_START_LONGITUDE));
                latitude = Double.parseDouble(SharedPreferencesUtil.get(
                        mContext, ProjectConstant.APP_START_LATITUDE));
                locationArray.put(longitude).put(latitude);
                positionObj.put("location", locationArray);
            }
            positionObj.put("province", SharedPreferencesUtil.get(mContext,
                    ProjectConstant.APP_START_PROVINCE));
            positionObj.put("city", SharedPreferencesUtil.get(mContext,
                    ProjectConstant.APP_START_CITY));
            positionObj.put("district", SharedPreferencesUtil.get(mContext,
                    ProjectConstant.APP_START_DISTRICT));
            content.put("position", positionObj);
            PostHttpUtil.prepareParams(requestParams, content.toString());
            sanmiAsyncTask.setIsShowDialog(true);
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.CSHOP_ALL.getShopMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            mKitchens = JsonUtility.fromJson(obj.get("shops"),
                                    new TypeToken<ArrayList<Kitchen>>() {
                                    });
                            showContent();
                            if (!TextUtils.isEmpty(SharedPreferencesUtil.get(
                                        mContext, ProjectConstant.APP_START_LONGITUDE))) {
                                longitude = Double.parseDouble(SharedPreferencesUtil.get(
                                        mContext, ProjectConstant.APP_START_LONGITUDE));
                                latitude = Double.parseDouble(SharedPreferencesUtil.get(
                                        mContext, ProjectConstant.APP_START_LATITUDE));
                            }else{
                                Android_6_Permission(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_CODE);
                            }
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showBangding() {
        if (mKitchenYibangding == null || mKitchenYibangding.getPhone() == null) {
            tv_yibangding.setVisibility(View.GONE);
        } else {
            tv_yibangding.setVisibility(View.VISIBLE);
            tv_yibangding.setText("已绑定 " + (mKitchenYibangding.getContact() == null ? "" : mKitchenYibangding.getContact()) + (mKitchenYibangding.getTitle() == null ? "" : ("(" + mKitchenYibangding.getTitle() + ")")));
            if ("confirmorder".equals(mFrom)){
                Intent intent = new Intent(BindHehuorenActivity.this, ConfirmOrder2Activity.class);
                intent.putExtra("kitchen", mKitchenYibangding);
                setResult(400, intent);
                finish();
            }
        }
    }

    private void startLocation(){
        LocationUtils.getInstence().start(mContext, new LocationListener() {
            @Override
            public void onResultLocation(Location location) {
                setLocation(location);
            }
        });
    }

    private void setLocation(Location location){
        if (location == null) {
            WaitingDialogControll.dismissLoadingDialog();
            ToastUtil
                    .showToast(mContext, getString(R.string.location_fail));
            return;
        }
        mPosition = new Position();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
//        mPosition.setCity(location.getCity());
        mPosition.setCity(LocationUtils.getInstence().getCity(location));
//            mPosition.setProvince(location.getProvince());
//            mPosition.setDistrict(location.getDistrict());
        mPosition.setLocation(new double[]{longitude, latitude});
//            SharedPreferencesUtil.save(mContext,
//                    ProjectConstant.APP_START_PROVINCE, location.getProvince());
//            SharedPreferencesUtil.save(mContext,
//                    ProjectConstant.APP_START_CITY, location.getCity());
        SharedPreferencesUtil.save(mContext,
                    ProjectConstant.APP_START_CITY, LocationUtils.getInstence().getCity(location));
//            SharedPreferencesUtil.save(mContext,
//                    ProjectConstant.APP_START_DISTRICT, location.getDistrict());
        SharedPreferencesUtil.save(mContext,
                ProjectConstant.APP_START_LATITUDE,
                String.valueOf(location.getLatitude()));
        SharedPreferencesUtil.save(mContext,
                ProjectConstant.APP_START_LONGITUDE,
                String.valueOf(location.getLongitude()));
        showContent();
    }

    private void showContent() {
        if (mKitchens == null || mKitchens.size() <= 0) {
            tv_empty.setVisibility(View.VISIBLE);
        } else {
            tv_empty.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(SharedPreferencesUtil.get(
                mContext, ProjectConstant.APP_START_LONGITUDE))){
            sort();
        }
        mAdapter.setData(mKitchens);
        mAdapter.notifyDataSetChanged();
        mSwipe.setRefreshing(false);
    }

    /**
     * 计算距离并按距离进行排序
     */
    private void sort() {
        for (int i = 0; i < mKitchens.size(); i++) {
            Kitchen kitchen = mKitchens.get(i);
            if(kitchen.getPositions().size() > 0 ){
//                double distence = CommonUtil.getDistance(latitude, longitude, kitchen.getLocation()[1], kitchen.getLocation()[0]);
                for (int j = 0; j < kitchen.getPositions().size(); j++) {
                    double distence = CommonUtil.getDistance(latitude,longitude,kitchen.getPositions().get(j).getLatitude(),kitchen.getPositions().get(j).getLongitude());
                    if (j == 0){
                        kitchen.setDistanceHehuoren(distence);
                        kitchen.setAddress(kitchen.getPositions().get(j).getAddress());
                    } else {
                        if (distence < kitchen.getDistanceHehuoren()){
                            kitchen.setDistanceHehuoren(distence);
                            kitchen.setAddress(kitchen.getPositions().get(j
                            ).getAddress());
                        }
                    }
                }
            }
        }
        for (int i = 0; i < mKitchens.size() - 1; i++) {
            for (int j = 0; j < mKitchens.size() - i - 1; j++) {
                Kitchen kitchen = null;
                if (mKitchens.get(j).getDistanceHehuoren() >= mKitchens.get(j + 1).getDistanceHehuoren()) {
                    kitchen = mKitchens.get(j);
                    mKitchens.set(j, mKitchens.get(j + 1));
                    mKitchens.set(j + 1, kitchen);
                }
            }
        }
    }

    @Override
    protected void initListener() {
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();
                return true;
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_btntuijianma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlDrawlayout.openDrawer(GravityCompat.END);
            }
        });

        tv_shurutuijianma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlDrawlayout.closeDrawer(GravityCompat.END);
                //跳转到绑定页面
                TuijianmaActivity.startMe(mContext);
            }
        });

        tv_erweimasaomiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlDrawlayout.closeDrawer(GravityCompat.END);
                Android_6_Permission(Manifest.permission.CAMERA,CAMERA_CODE);
            }
        });

    }

    private void startErWeiMa(){
        //打开扫描界面扫描条形码或二维码
        Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(openCameraIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if (!TextUtils.isEmpty(scanResult)) {
                //跳转到绑定页面
                Intent intent = new Intent(BindHehuorenActivity.this, TuijianmaActivity.class);
                intent.putExtra("erweima_result", scanResult);
                startActivity(intent);
                TuijianmaActivity.startMe(mContext,scanResult);
            } else {//不符合要求
                ToastUtil.showToast(mContext, "二维码扫描出错");
            }
        }
    }

    private void search() {
        String keyword = et_search.getText().toString().trim();
        if (TextUtils.isEmpty(keyword)) {
            mAdapter.setData(mKitchens);
        } else {
            ArrayList<Kitchen> tmp = new ArrayList<>();
            if (!mKitchens.isEmpty()) {
                for (Kitchen kitchen : mKitchens) {
                    if (kitchen.getContact().contains(keyword)) {
                        tmp.add(kitchen);
                    } else if (kitchen.getPhone().contains(keyword)) {
                        tmp.add(kitchen);
                    } else if (kitchen.getTitle().contains(keyword)) {
                        tmp.add(kitchen);
                    }
                }
            }
            mAdapter.setData(tmp);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void Android_6_Permission(String permission,int code) {
        //首先判断版本号是否大于等于6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检测权限是否开启
            if (!(mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{permission},code);
            }else{
                switch (code){
                    case REQUEST_CODE:
                        startLocation();
                        break;
                    case CAMERA_CODE:
                        startErWeiMa();
                        break;
                    default:
                        break;
                }
            }
        }else{
            switch (code){
                case REQUEST_CODE:
                    startLocation();
                    break;
                case CAMERA_CODE:
                    startErWeiMa();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE:
                startLocation();
                break;
            case CAMERA_CODE:
                startErWeiMa();
                break;
            default:
                break;
        }
    }
}
