package com.hclz.client.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.MainActivity;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask.ResultHandler;
import com.hclz.client.base.bean.MainAccount;
import com.hclz.client.base.bean.SubAccount;
import com.hclz.client.base.constant.HclzConstant;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.photo.SelectPicDialog;
import com.hclz.client.base.ui.BaseFragment;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.view.SelectDialog;
import com.hclz.client.base.view.WaitingDialogControll;
import com.hclz.client.forcshop.jiedanguanli.JiedanGuanliActivity;
import com.hclz.client.forcshop.kucunguanli.KucunGuanliActivity;
import com.hclz.client.forcshop.qrcode.MyQrCodeActivity;
import com.hclz.client.login.LoginActivity;
import com.hclz.client.me.view.PhoneOrMessageDialog;
import com.hclz.client.order.WebViewActivity;
import com.hyphenate.easeui.EaseConstant;
import com.merben.wangluodianhua.activitys.NetPhoneActivity;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MeFragment extends BaseFragment implements OnClickListener {
    private View viWholeView;
    private TextView tvCommHeadTitle;
    private ImageView ivCommHeadRight;
    private LinearLayout llQianbao, llUserInfo, llMycoupon, llMyJifen, llMyAddress, llJiedanguanli, llKucunguanli,llMyQrCode,llCshopOP,ll_my_zuji,ll_contact_kefu,ll_my_wangluodianhua;//llMyShoucang
    private LinearLayout llWxAuth,ll_my_staff;
    private ImageView ivUser;
    private TextView tvName, tvPhone;
    private TextView tvCouponsCount, tvJifenAmount;
    private TextView tvBindWx;

    private LinearLayout llNoUser;

    private MainAccount mainAccount;
    private ArrayList<SubAccount> subAccounts;
    private int amount;
    private String couponsCount;
    private boolean isBingWx = false;

    private String headImageUrl;
    private SwipeRefreshLayout mSwr;

    private SelectDialog mDialog;

    public static void startMe(Context from){
        Intent intent = new Intent(from, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("fragment", "MeFragment");
        from.startActivity(intent);
    }

    public static void startMeFromSelfActivity(Context from){
        ((MainActivity)from).rdoMe.setChecked(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viWholeView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_me, container, false);
        return viWholeView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && HclzConstant.getInstance().isNeedRefresh) {// 当前fragment显示时
            showLoadingDialog();
            getUser();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (HclzConstant.getInstance().isNeedRefresh&&((MainActivity) mContext).getCurrentVisibleFragment() == MainActivity.CURRENT_ME) {
            HclzConstant.getInstance().isNeedRefresh = false;
            showLoadingDialog();
            getUser();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopSwr();
    }

    private void dissmissDialog() {
        handler.sendEmptyMessageDelayed(8888, 0);
    }

    private void showLoadingDialog() {
        WaitingDialogControll.showLoadingDialog(mContext);
        handler.sendEmptyMessageDelayed(8888, 20000);
    }
    @Override
    protected void initView() {
        viWholeView.findViewById(R.id.iv_comm_head_left).setVisibility(View.GONE);
        tvCommHeadTitle = (TextView) viWholeView.findViewById(R.id.tv_comm_head_title);
        ivCommHeadRight = (ImageView) viWholeView.findViewById(R.id.iv_comm_head_right);
        ivCommHeadRight.setVisibility(View.VISIBLE);

        llUserInfo = (LinearLayout) viWholeView.findViewById(R.id.ll_user_info);

        ivUser = (ImageView) viWholeView.findViewById(R.id.iv_user);
        tvName = (TextView) viWholeView.findViewById(R.id.tv_name);
        tvPhone = (TextView) viWholeView.findViewById(R.id.tv_phone);
        llQianbao = (LinearLayout) viWholeView.findViewById(R.id.ll_my_qianbao);
        llMycoupon = (LinearLayout) viWholeView.findViewById(R.id.ll_mycoupon);
        llMyJifen = (LinearLayout) viWholeView.findViewById(R.id.ll_my_jifen);
        llCshopOP = (LinearLayout)viWholeView.findViewById(R.id.ll_cshop_op);
        llJiedanguanli = (LinearLayout) viWholeView.findViewById(R.id.ll_my_jiedanxitong);
        llKucunguanli = (LinearLayout) viWholeView.findViewById(R.id.ll_my_kucunguanli);
        llMyQrCode = (LinearLayout) viWholeView.findViewById(R.id.ll_my_qrcode);
        llMyAddress = (LinearLayout) viWholeView.findViewById(R.id.ll_my_address);
        ll_contact_kefu = (LinearLayout) viWholeView.findViewById(R.id.ll_contact_kefu);
        ll_my_wangluodianhua = (LinearLayout) viWholeView.findViewById(R.id.ll_my_wangluodianhua);

        llWxAuth = (LinearLayout) viWholeView.findViewById(R.id.ll_wx_auth);
        ll_my_staff=(LinearLayout)viWholeView.findViewById(R.id.ll_my_staff);
        tvCouponsCount = (TextView) viWholeView.findViewById(R.id.tv_coupons_count);
        tvJifenAmount = (TextView) viWholeView.findViewById(R.id.tv_jifen_amount);

        tvBindWx = (TextView) viWholeView.findViewById(R.id.tv_bind_wx);

        ll_my_zuji = (LinearLayout) viWholeView.findViewById(R.id.ll_my_zuji);

        llNoUser = (LinearLayout) viWholeView.findViewById(R.id.ll_no_user);

        mSwr = (SwipeRefreshLayout) viWholeView.findViewById(R.id.me_swr);
        mSwr.setColorSchemeResources(R.color.main, R.color.yellow, R.color.main,
                R.color.yellow);
    }

    @Override
    protected void initInstance() {

        mDialog = new SelectDialog(mContext, new SelectDialog.SelectListener() {
            @Override
            public void message() {
                KefuListActivity.startMe(mContext);
            }

            @Override
            public void phone() {
                ToastUtil.showToast(mContext,"打电话！！！");
            }
        });
    }

    @Override
    protected void initData() {
        configMap = HclzApplication.getData();
    }

    @Override
    protected void setViewData() {
        tvCommHeadTitle.setText(getActivity().getResources().getString(R.string.me));
    }

    @Override
    protected void setListener() {
        ivCommHeadRight.setOnClickListener(this);
        llQianbao.setOnClickListener(this);
        llUserInfo.setOnClickListener(this);
        llMycoupon.setOnClickListener(this);
        llMyJifen.setOnClickListener(this);
        llKucunguanli.setOnClickListener(this);
        llJiedanguanli.setOnClickListener(this);
        llMyQrCode.setOnClickListener(this);
//		llMyShoucang.setOnClickListener(this);
        llMyAddress.setOnClickListener(this);
        llWxAuth.setOnClickListener(this);
        llNoUser.setOnClickListener(this);
        ll_my_zuji.setOnClickListener(this);
        ll_contact_kefu.setOnClickListener(this);
        ll_my_wangluodianhua.setOnClickListener(this);
        ll_my_staff.setOnClickListener(this);
        mSwr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    /**
     * 关闭刷新
     */
    private void stopSwr(){
        if (mSwr != null && mSwr.isRefreshing()){
            mSwr.setRefreshing(false);
        }
    }

    /**
     * 刷新需要执行的方法
     */
    private void refresh(){
        initData();
        getUser();
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(SharedPreferencesUtil
                .get(mContext, ProjectConstant.APP_USER_MID))) {
            LoginActivity.startMe(mContext);
        } else {
            Intent intent;
            switch (v.getId()) {
                case R.id.ll_no_user:
                    LoginActivity.startMe(mContext);
                    break;
                case R.id.iv_comm_head_right:
                    SettingActivity.startMe(mContext);
                    break;
                case R.id.ll_user_info:
                    UserInfoActivity.startMe(mContext,mainAccount);
                    break;
                case R.id.ll_my_qianbao:
                    QianbaoActivity.startMe(mContext);
                    break;
                case R.id.ll_my_qrcode:
                    MyQrCodeActivity.startMe(mContext);
                    break;
                case R.id.ll_my_kucunguanli:
                    KucunGuanliActivity.startMe(mContext);
                    break;
                case R.id.ll_my_wangluodianhua:
                    NetPhoneActivity.startMe(mContext);
                    break;
                case R.id.ll_my_jiedanxitong:
                    JiedanGuanliActivity.startMe(mContext);
                    break;
                case R.id.ll_my_staff:
                    MyStaffActivity.startMe(mContext);
                    break;
                case R.id.ll_mycoupon:
                    HclzConstant.getInstance().isNeedRefresh = true;
                    StringBuilder sb = new StringBuilder();
                    sb.append(ServerUrlConstant.COUPON_MY.getWebViewMethod());
                    sb.append("?");
                    sb.append(PostHttpUtil.getBasicUrl(mContext, configMap, true));
                    WebViewActivity.startMe(mContext,"我的奖券",sb.toString(),true);
                    break;
                case R.id.ll_my_jifen:
//                    startActivity(new Intent(getActivity(), MyJiFenActivity.class));
                    MyJiFenActivity.startMe(mContext);
                    break;
//			case R.id.ll_my_shoucang:
//				startActivity(new Intent(getActivity(), MyShouCangActivity.class));
//				break;
                case R.id.ll_my_address:
//                    startActivity(new Intent(getActivity(), MyAddressActivity.class));
                    MyAddressActivity.startMe(mContext);
                    break;
                case R.id.ll_wx_auth:
                    if (!isBingWx) {
                        HclzConstant.getInstance().isNeedRefresh = true;
                        getWXAuth();
                    }
                    break;
                case R.id.ll_my_zuji:
                    ZujiActivity.startMe(mContext);
                    break;
                case R.id.ll_contact_kefu:
                    mDialog.show();
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 方法说明：获取用户信息
     */
    private void getUser() {
        if (!TextUtils.isEmpty(SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID))) {
            JSONObject content = null;
            try {
//                content.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
//                content.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
                content = PostHttpUtil.prepareContents(configMap,mContext);
                content.put(ProjectConstant.APP_USER_MID,
                        SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
                content.put(ProjectConstant.APP_USER_SESSIONID,
                        SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
                content.put("with_assets", true);
                PostHttpUtil.prepareParams(requestParams, content.toString());
                sanmiAsyncTask.setIsShowDialog(false);
                sanmiAsyncTask.excutePosetRequest(ServerUrlConstant.USER_SELT.getUserMethod(),
                        requestParams,
                        new ResultHandler() {
                            @Override
                            public void callBackForServerSuccess(String result) {
                                JsonObject obj = JsonUtility.parse(result);
                                mainAccount = JsonUtility.fromJson(obj.get("main_account"), MainAccount.class);
                                subAccounts = JsonUtility.fromJson(obj.get("sub_accounts"),
                                        new TypeToken<ArrayList<SubAccount>>() {
                                        });
                                //增加user_type判定,判断是 normal(普通用户) 还是 cshop(合伙人) 还是 dshop(城代)buser(商户端)euser（员工）
                                String user_type = JsonUtility.fromJson(obj.get("user_type"), String.class);
                                SharedPreferencesUtil.save(mContext, "user_type", user_type);

                                if ("dshop".equals(user_type)) {
                                    tvCommHeadTitle.setText(getActivity().getResources().getString(R.string.me_chengdai));
                                }
                                SharedPreferencesUtil.save(mContext,ProjectConstant.APP_USER_INTRODUCER,obj.get("introducer").toString());

                                SharedPreferencesUtil.saveUserBasic(mContext, mainAccount, subAccounts);
                                if (obj.get("assets") != null) {
                                    JsonObject assets = obj.get("assets").getAsJsonObject();
                                    if (assets != null) {
                                        JsonObject obj1 = assets.get("basic").getAsJsonObject();
                                        JsonObject obj2 = null;
                                        if (obj1 != null && obj1.get("points") != null) {
                                            obj2 = obj1.get("points").getAsJsonObject();
                                        }
                                        if (obj2 != null) {
                                            if (obj2.get("amount") != null && !(obj2.get("amount") instanceof JsonNull)) {
                                                amount = obj2.get("amount").getAsInt();
                                            }
                                        }
                                        couponsCount = assets.get("coupons_count").getAsString();
                                    }
                                }
                                showUser();
                            }

                            @Override
                            public boolean callBackSessionError() {
                                getUser();
                                return false;
                            }
                        });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            showNoUser();
        }
    }

    /**
     * 用户未登录状态
     */
    private void showNoUser() {
        headImageUrl = "";

        llNoUser.setVisibility(View.VISIBLE);
        llUserInfo.setVisibility(View.GONE);

        //除了cshop都不显示
        llCshopOP.setVisibility(View.GONE);
        //未登录状态标题改回"我的"
        tvCommHeadTitle.setText(getActivity().getResources().getString(R.string.me));

        llCshopOP.setVisibility(View.GONE);

        tvCouponsCount.setText("");
        tvJifenAmount.setText("");

        tvBindWx.setTextColor(getResources().getColor(R.color.black));
        tvBindWx.setText(getString(R.string.no_bangding));
        dissmissDialog();
        stopSwr();
    }

    /**
     * 用户登录状态
     */
    private void showUser() {

        if (("cshop".equals(SharedPreferencesUtil.get(mContext,"user_type")))){//cshop身份需要显示操作面板
            llCshopOP.setVisibility(View.VISIBLE);
        } else {
            llCshopOP.setVisibility(View.GONE);
        }
        if("buser".equals(SharedPreferencesUtil.get(mContext,"user_type"))){
            ll_my_staff.setVisibility(View.GONE);//暂时不上线员工功能，隐藏
        }
        llNoUser.setVisibility(View.GONE);
        llUserInfo.setVisibility(View.VISIBLE);
        String imageUrl = mainAccount.getAvatar();
        if (mApplication.getWxUserInfo() != null && TextUtils.isEmpty(imageUrl)) {
            imageUrl = mApplication.getWxUserInfo().getHeadimgurl();
        }
        if (TextUtils.isEmpty(headImageUrl) || !headImageUrl.equals(imageUrl)) {
            headImageUrl = imageUrl;
            ImageUtility.getInstance(mContext).showImage(imageUrl, ivUser,R.mipmap.ic_touxiang);
        }
        tvName.setText(mainAccount.getNickname());
        for (SubAccount account : subAccounts) {
            if (account.getType().equals("phone")) {
                tvPhone.setText(getString(R.string.my_phone, account.getSid()));
            }
            if (account.getType().equals("weichat")) {
                isBingWx = true;
            }
        }
        if (isBingWx) {
            tvBindWx.setTextColor(getResources().getColor(R.color.red));
            tvBindWx.setText(getString(R.string.alread_bangding));
        } else {
            tvBindWx.setTextColor(getResources().getColor(R.color.black));
            tvBindWx.setText(getString(R.string.no_bangding));
        }
        tvCouponsCount.setText(getString(R.string.my_coupon_num, couponsCount));
        tvJifenAmount.setText(getString(R.string.jifen_num, amount));
        dissmissDialog();
        stopSwr();
    }

    private void getWXAuth() {
        SendAuth.Req req = new SendAuth.Req();
        req.openId = "wx35b28d704912110f";
        req.scope = "snsapi_userinfo";
        req.state = "hclz_wx_auth";
        api.sendReq(req);
    }
}
