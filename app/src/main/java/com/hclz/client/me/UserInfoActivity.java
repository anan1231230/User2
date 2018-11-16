package com.hclz.client.me;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.bean.MainAccount;
import com.hclz.client.base.bean.SubAccount;
import com.hclz.client.base.constant.HclzConstant;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.photo.CropConfig;
import com.hclz.client.base.photo.CropInterface;
import com.hclz.client.base.photo.CropUtils;
import com.hclz.client.base.photo.SelectPicControll;
import com.hclz.client.base.photo.SelectPicDialog;
import com.hclz.client.base.ui.BaseActivity;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserInfoActivity extends BaseActivity implements OnClickListener, CropInterface {
    private ImageView ivImage;
    private LinearLayout llChangeNicheng, llCHangeTouxiang;
    private TextView tvNicheng;

    private MainAccount account;
    private String avatar;
    private String niCheng;

    private CropConfig mCropParams;
    private HashMap<String, String> fileParams = new HashMap<>();

    private static final int CAMERA_CODE = 999;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from, MainAccount mainAccount) {
        Intent intent = new Intent(from, UserInfoActivity.class);
        intent.putExtra("mainAccount", mainAccount);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_user_info);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        ivImage = (ImageView) findViewById(R.id.iv_image);
        llChangeNicheng = (LinearLayout) findViewById(R.id.ll_change_nicheng);
        llCHangeTouxiang = (LinearLayout) findViewById(R.id.ll_change_touxiang);
        tvNicheng = (TextView) findViewById(R.id.tv_nicheng);
    }

    @Override
    protected void initInstance() {
    }

    @Override
    protected void initData() {
        setCommonTitle(R.string.user_info);
        mCropParams = new CropConfig();
        if (mIntent != null) {
            account = (MainAccount) mIntent.getSerializableExtra("mainAccount");
            if (account != null) {
                avatar = account.getAvatar();
                ImageUtility.getInstance(mContext).showImage(avatar, ivImage, R.mipmap.ic_touxiang);
                niCheng = account.getNickname();
                tvNicheng.setText(niCheng);
            }
        }
    }

    @Override
    protected void initListener() {
        llChangeNicheng.setOnClickListener(this);
        llCHangeTouxiang.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_change_nicheng:
                ChangeUserInfoActivity.startMe(mContext, account);
                finish();
                break;
            case R.id.ll_change_touxiang:
                SelectPicControll.showPicDialog(mContext, new SelectPicDialog.OnCustomDialogListener() {
                    @Override
                    public void take() {
                        Android_6_Permission(Manifest.permission.CAMERA, CAMERA_CODE);
//                        Intent intent_take = CropUtils.buildCaptureIntent(CropUtils.buildUri());
//                        startActivityForResult(intent_take, CropUtils.REQUEST_CAMERA);
//                        startMeForResult(UserInfoActivity.class,null,intent_take, CropUtils.REQUEST_CAMERA);
                    }

                    @Override
                    public void select() {
                        Intent intent_gallery = CropUtils
                                .buildGetFromGalleryIntent(CropUtils.buildUri());
                        startActivityForResult(intent_gallery, CropUtils.REQUEST_SELECT);
//                        startMeForResult(UserInfoActivity.class,null,intent_gallery, CropUtils.REQUEST_SELECT);
                    }
                }, "上传头像");
                break;
            default:
                break;
        }
    }

    private void startCamera() {
        Intent intent_take = CropUtils.buildCaptureIntent(CropUtils.buildUri());
        startActivityForResult(intent_take, CropUtils.REQUEST_CAMERA);
    }

    public void Android_6_Permission(String permission, int code) {
        //首先判断版本号是否大于等于6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检测权限是否开启
            if (!(mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{permission}, code);
            } else {
                switch (code) {
                    case CAMERA_CODE:
                        startCamera();
                        break;
                    default:
                        break;
                }
            }
        } else {
            switch (code) {
                case CAMERA_CODE:
                    startCamera();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_CODE:
                startCamera();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CropUtils.REQUEST_CAMERA:
            case CropUtils.REQUEST_SELECT:
            case CropUtils.REQUEST_CROP:
                CropUtils.handleResult(this, requestCode, resultCode, data);
                break;
            default:
                break;

        }
    }

    private void changeAvatar(String avatar) {
        requestParams = new HashMap<String, String>();
        JSONObject content;
        try {
            configMap = HclzApplication.getData();
            content = PostHttpUtil.prepareContents(configMap, mContext);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
            JSONObject account = new JSONObject();
            content.put("main_account", account);
            PostHttpUtil.prepareParams(requestParams, content.toString());
            if (avatar != null) {
                fileParams.put("avatar", avatar);
            }
            if (fileParams == null || fileParams.size() == 0) fileParams = null;
            sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_UPDATE.getUserMethod(), requestParams, fileParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            MainAccount mainAccount = JsonUtility.fromJson(obj.get("main_account"), MainAccount.class);
                            List<SubAccount> subAccounts = JsonUtility.fromJson(obj.get("sub_accounts"),
                                    new TypeToken<ArrayList<SubAccount>>() {
                                    });
                            SharedPreferencesUtil.saveUserBasic(mContext, mainAccount, subAccounts);
                            HclzConstant.getInstance().isNeedRefresh = true;
                            //跳转到MeFragment页面
                            UserInfoActivity.this.finish();
                        }

                        @Override
                        protected void callBackForGetDataFailed(String result) {
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CropConfig getCropConfig() {
        return mCropParams;
    }

    @Override
    public void onImageCropped(Uri uri) {
        try {
            changeAvatar(uri.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageCanceled() {

    }

    @Override
    public void onImageFailed(String message) {
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public Fragment getFragment() {
        return null;
    }
}
