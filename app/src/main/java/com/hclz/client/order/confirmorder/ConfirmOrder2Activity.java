package com.hclz.client.order.confirmorder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.R;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.async.SanmiAsyncTask;
import com.hclz.client.base.config.SanmiConfig;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.ui.BaseAppCompatActivity;
import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.base.util.ImageUtility;
import com.hclz.client.base.util.JsonUtility;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.ToastUtil;
import com.hclz.client.base.view.CircleImageView;
import com.hclz.client.base.view.MyListView;
import com.hclz.client.base.view.OutStockDialog;
import com.hclz.client.faxian.products.AddressIns;
import com.hclz.client.me.MyAddressActivity;
import com.hclz.client.me.SelectHehuorenActivity;
import com.hclz.client.order.OrderFragment;
import com.hclz.client.order.SelectPayMethodActivity;
import com.hclz.client.order.adapter.OrderComfirmDetailAdapter;
import com.hclz.client.order.confirmorder.bean.address.NetAddress;
import com.hclz.client.order.confirmorder.bean.discount.NetDiscount;
import com.hclz.client.order.confirmorder.bean.hehuoren.NetHehuoren;
import com.hclz.client.order.confirmorder.bean.products.NetProduct;
import com.hclz.client.shouye.newcart.DiandiCart;
import com.hclz.client.shouye.newcart.DiandiCartItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by handsome on 16/7/27.
 */
public class ConfirmOrder2Activity extends BaseAppCompatActivity implements View.OnClickListener {


    private LinearLayout ll_address;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_address;
    private MyListView lv_products;
    private TextView tv_coupon_name;
    private LinearLayout ll_select_coupon;
    private TextView tv_totalPrice;
    private TextView tv_coupon_cut;
    private TextView tv_yunfei;
    private TextView tv_choosehehuoren;
    private CircleImageView img_logo;
    private TextView txt_name;
    private TextView txt_phone;
    private TextView txt_address;
    private TextView tv_note;
    private TextView txt_juli;
    private TextView txt_xiaoliang;
    private LinearLayout ll_kitchen;
    private TextView tv_actual_pay;
    private TextView tv_zhifufangshi;
    private TextView tv_commit_order;
    private LinearLayout ll_address_empty;

    private String mOrderid;
    private List<NetDiscount> mDiscounts;
    private List<NetProduct> mModifyProducts;
    private NetAddress mAddress;
    private NetHehuoren mHehuoren;
    private ArrayList<NetHehuoren> mHehuorens;
    private Integer mProductsPayment;
    private Integer mYunfei;
    private Integer mYouhui;
    private Integer mTotalPayement;
    private Integer mProductsNum;
    private Integer mWalletPay = 1;

    private OrderComfirmDetailAdapter adapter;

    private int type = 1;

    /**
     * 页面跳转
     *
     * @param from
     */
    public static void startMe(Context from, int type) {
        Intent intent = new Intent(from, ConfirmOrder2Activity.class);
        intent.putExtra(ProjectConstant.ORDER_TYPE, type);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_confirm_order);
    }

    @Override
    protected void initView() {

        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        lv_products = (MyListView) findViewById(R.id.lv_products);
        tv_coupon_name = (TextView) findViewById(R.id.tv_coupon_name);
        ll_select_coupon = (LinearLayout) findViewById(R.id.ll_select_coupon);
        tv_totalPrice = (TextView) findViewById(R.id.tv_totalPrice);
        tv_coupon_cut = (TextView) findViewById(R.id.tv_coupon_cut);
        tv_yunfei = (TextView) findViewById(R.id.tv_yunfei);
        tv_choosehehuoren = (TextView) findViewById(R.id.tv_choosehehuoren);
        img_logo = (CircleImageView) findViewById(R.id.img_logo);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_phone = (TextView) findViewById(R.id.txt_phone);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_juli = (TextView) findViewById(R.id.txt_juli);
        txt_xiaoliang = (TextView) findViewById(R.id.txt_xiaoliang);
        ll_kitchen = (LinearLayout) findViewById(R.id.ll_kitchen);
        tv_actual_pay = (TextView) findViewById(R.id.tv_actual_pay);
        tv_zhifufangshi = (TextView) findViewById(R.id.tv_zhifufangshi);
//        if ("dshop".equals(SharedPreferencesUtil.get(mContext, "user_type"))) {
//            tv_zhifufangshi.setVisibility(View.VISIBLE);
//            tv_zhifufangshi.setSelected(false);
//        } else {
//            tv_zhifufangshi.setVisibility(View.GONE);
//            tv_zhifufangshi.setSelected(false);
//        }
        tv_commit_order = (TextView) findViewById(R.id.tv_commit_order);
        ll_address_empty = (LinearLayout) findViewById(R.id.ll_address_empty);
        tv_note = (TextView) findViewById(R.id.tv_note);
        setCommonTitle("确认订单");
    }

    @Override
    protected void initInstance() {
        configMap = HclzApplication.getData();
        if (mIntent != null) {
            type = mIntent.getIntExtra(ProjectConstant.ORDER_TYPE, 1);//1是好吃懒做,0是海外精品,2是家居
        }
    }

    private void applyOrder() {
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            if (AddressIns.getInstance().getmAddress() != null) {
                contentObj.put("addressid", AddressIns.getInstance().getmAddress().getAddressId());
            }

            List<DiandiCartItem> items = new ArrayList<>();
            //TODO
//            if (type == 2){//家居
//                items.addAll(CartJiaju.getInstance().get());
//            }else{//海外精品好吃懒做
            items.addAll(DiandiCart.getInstance().getArrange());
//            }
            JSONArray jsonArray = new JSONArray();
            for (DiandiCartItem item : items) {
                jsonArray.put(item.toJson());
            }
            contentObj.put("shopping_cart", jsonArray);

            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.ORDER_APPLY2.getOrderMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            if (!"{}".equals(obj.get("inventory_lacks").toString())) {
                                Map<String, Integer> lackMap = JsonUtility.getMapForJson(obj.get("inventory_lacks").toString());
                                if (lackMap != null && lackMap.size() > 0) {
                                    OutStockDialog.getInstance().showDialog(mContext, "缺货提醒", lackMap, OutStockDialog.OUT_STOCK);
                                }
                            }
                            if (!"{}".equals(obj.get("price_changed").toString())) {
                                Map<String, Integer> priceChangeMap = JsonUtility.getMapForJson(obj.get("price_changed").toString());
                                if (priceChangeMap != null && priceChangeMap.size() > 0) {
                                    OutStockDialog.getInstance().showDialog(mContext, "价格变化提醒", priceChangeMap, OutStockDialog.PRICE_CHANGE, type);
                                }
                            }

                            dealWithNetResult(obj);
                        }

                        @Override
                        public void callBackAddressError() {
                            ll_address_empty.setVisibility(View.VISIBLE);
                            ll_address.setVisibility(View.GONE);
                            Intent intent = new Intent(ConfirmOrder2Activity.this,
                                    MyAddressActivity.class);
                            intent.putExtra("from", ConfirmOrder2Activity.class.getName());
                            startActivityForResult(intent, 100);
                            ToastUtil.showLongToast(mContext, "收获地址存在问题,请修改为正确收获地址或选择其它地址");
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    String mNote = "";

    private void dealWithNetResult(JsonObject obj) {
        //获取orderid
        mOrderid = JsonUtility.fromJson(obj.get("orderid"), String.class);

        //获取产品列表
        mModifyProducts = new ArrayList<>();
        List<NetProduct> products = JsonUtility.fromJson(obj.get("products"), new TypeToken<List<NetProduct>>() {
        });
        if (products != null && products.size() > 0) {
            for (NetProduct product : products) {
                mModifyProducts.add(product);
            }
        }
        //TODO
//        if (type == 2){//家居
//            CartJiaju.getInstance().modify(mModifyProducts,mContext);
//        }else{//好吃懒做海外精品
        DiandiCart.getInstance().modify(mModifyProducts, mContext);
//        }
        //获取优惠券列表
        mDiscounts = new ArrayList<NetDiscount>();
        List<NetDiscount> discounts = JsonUtility.fromJson(obj.get("available_discounts"), new TypeToken<List<NetDiscount>>() {
        });
        if (discounts != null && discounts.size() > 0) {
            for (NetDiscount discount : discounts) {
                mDiscounts.add(discount);
            }
        }
        //获取地址
        mAddress = JsonUtility.fromJson(obj.get("select_address"), NetAddress.class);
        //获取合伙人信息
        mHehuoren = JsonUtility.fromJson(obj.get("bind_cshop"), NetHehuoren.class);
        mHehuorens = JsonUtility.fromJson(obj.get("available_cshops"), new TypeToken<ArrayList<NetHehuoren>>() {
        });
        mProductsPayment = JsonUtility.fromJson(obj.get("products_payment"), Integer.class);
        mYunfei = JsonUtility.fromJson(obj.get("yunfei"), Integer.class);
        mYouhui = JsonUtility.fromJson(obj.get("discount_payment"), Integer.class);
        mTotalPayement = JsonUtility.fromJson(obj.get("total_payment"), Integer.class);
        mWalletPay = JsonUtility.fromJson(obj.get("wallet_pay"), Integer.class);
        mProductsNum = products != null ? products.size() : 0;
        mNote = JsonUtility.getAsString(obj.get("note"));

        if (TextUtils.isEmpty(mNote)) {
            tv_note.setVisibility(View.GONE);
        } else {
            tv_note.setVisibility(View.VISIBLE);
            tv_note.setText(mNote);
        }

        showContent();
    }

    private void showContent() {
        showAddress();
        showProducts();
        showYouhui();
        showPrice();
        showHehuoren();
    }

    private void showAddress() {
        //地址
        if (mAddress == null || (TextUtils.isEmpty(mAddress.province) && TextUtils.isEmpty(mAddress.city))) {
            ll_address_empty.setVisibility(View.VISIBLE);
            ll_address.setVisibility(View.GONE);
            mAddress = null;
        } else {
            ll_address.setVisibility(View.VISIBLE);
            ll_address_empty.setVisibility(View.GONE);
            tv_name.setText(mAddress.getName());
            tv_phone.setText(mAddress.getPhone());
            tv_address.setText(mAddress.getAddress());
        }
    }

    private void showProducts() {
        //产品详情
        ArrayList<DiandiCartItem> listDiancan = null;
        //TODO
//        if (type == 2){//家居
//            listDiancan = (ArrayList) CartJiaju.getInstance().get();
//        }else{//好吃懒做海外精品
        listDiancan = (ArrayList) DiandiCart.getInstance().get();
//        }
        adapter = new OrderComfirmDetailAdapter(mContext, listDiancan);
        lv_products.setAdapter(adapter);
    }

    private void showYouhui() {
        //优惠券
        if (mDiscounts != null && mDiscounts.size() > 0) {
            for (NetDiscount discount : mDiscounts) {
                if (discount.isSelected()) {
                    mYouhuiName = discount.getDiscountTitle();
                    break;
                }
            }
        } else {
            mYouhuiName = "无可用优惠券";
        }
        tv_coupon_name.setText(mYouhuiName);
    }

    private void showPrice() {
        //价格
        tv_totalPrice.setText("共" + mProductsNum + "件商品,合计¥" + CommonUtil.getMoney(mProductsPayment));
        tv_coupon_cut.setText(CommonUtil.getMoneyForCommitOrder(mYouhui));
        tv_yunfei.setText(CommonUtil.getMoneyForCommitOrder(mYunfei));
        tv_actual_pay.setText("¥" + CommonUtil.getMoney(mTotalPayement));
    }

    private void showHehuoren() {
        //只有normal显示配送人，剩下都不显示
        if (("cshop".equals(SharedPreferencesUtil.get(mContext, "user_type"))) || ("dshop".equals(SharedPreferencesUtil.get(mContext, "user_type")))
                || ("euser".equals(SharedPreferencesUtil.get(mContext, "user_type")))|| ("buser".equals(SharedPreferencesUtil.get(mContext, "user_type")))) {
            ll_kitchen.setVisibility(View.GONE);
        } else {
            if (mHehuoren != null && mHehuoren.getName() != null) {
                ll_kitchen.setVisibility(View.VISIBLE);
                ImageUtility.getInstance(mContext).showImage(getTextNotNull(mHehuoren.getAlbum()), img_logo, R.mipmap.ic_dianpu);
                txt_name.setText(getTextNotNull(mHehuoren.getName()));
                txt_phone.setText(getTextNotNull(mHehuoren.getPhone()));
                txt_address.setText(getTextNotNull(mHehuoren.getAddress()));

            } else {
                ll_kitchen.setVisibility(View.GONE);
            }
        }
    }

    private String getTextNotNull(String text) {
        return TextUtils.isEmpty(text) ? "" : text;
    }

    @Override
    protected void initData() {
        applyOrder();
//        dealWithTestResult();
    }

    @Override
    protected void initListener() {
        ll_address.setOnClickListener(this);
        ll_address_empty.setOnClickListener(this);
        tv_commit_order.setOnClickListener(this);
        tv_zhifufangshi.setOnClickListener(this);
        ll_select_coupon.setOnClickListener(this);
        ll_kitchen.setOnClickListener(this);
    }

    String mYouhuiName = "";

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_select_coupon:
                if (mDiscounts == null || mDiscounts.size() <= 0) {
                    ToastUtil.showToast(mContext, "无可用优惠");
                } else {
                    final String[] list = new String[mDiscounts.size() + 1];
                    list[0] = "不使用任何优惠券";
                    for (int i = 0; i < mDiscounts.size(); i++) {
                        list[i + 1] = mDiscounts.get(i).getDiscountTitle();
                    }
                    new AlertDialog.Builder(mContext).setTitle("优惠").setItems(list, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                mYouhuiName = "不使用任何优惠券";
                                refresh("");
                            } else {
                                mYouhuiName = mDiscounts.get(which - 1).getDiscountTitle();
                                refresh(mDiscounts.get(which - 1).getDiscountid());
                            }

                        }
                    }).show();
                }
                break;
            case R.id.ll_address_empty:
            case R.id.ll_address:
                intent = new Intent(ConfirmOrder2Activity.this,
                        MyAddressActivity.class);
                intent.putExtra("from", ConfirmOrder2Activity.class.getName());
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_commit_order:
                if (mAddress == null) {
                    ToastUtil.showToast(mContext, getString(R.string.select_address));
                } else if (mHehuoren == null && ("normal").equals(SharedPreferencesUtil.get(mContext, "user_type"))) {
                    new AlertDialog.Builder(mContext).setTitle("当前未选择配送人,请先选择配送人!")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“确认”后的操作
                                    Intent intent = new Intent(ConfirmOrder2Activity.this, SelectHehuorenActivity.class);
                                    intent.putExtra("kitchens", mHehuorens);
                                    startActivityForResult(intent, 400);
                                }
                            }).show();
                } else {
                    placeorder();
                }
                break;
            case R.id.tv_zhifufangshi:
                tv_zhifufangshi.setSelected(!tv_zhifufangshi.isSelected());
                break;
            case R.id.ll_kitchen:
                // 点击“确认”后的操作
                intent = new Intent(ConfirmOrder2Activity.this, SelectHehuorenActivity.class);
                intent.putExtra("kitchens", mHehuorens);
                startActivityForResult(intent, 400);
                break;
            default:
                break;
        }
    }

    private void placeorder() {
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            contentObj.put("orderid", mOrderid);
            contentObj.put("addressid", mAddress.getAddressId());
            for (NetDiscount discount : mDiscounts) {
                if (discount.isSelected()) {
                    contentObj.put("discountid", discount.getDiscountid());
                    break;
                }
            }
            if ("normal".equals(SharedPreferencesUtil.get(mContext, "user_type")) && mHehuoren != null) {
                contentObj.put("cid", mHehuoren.getCid());
            }
            if ("dshop".equals(SharedPreferencesUtil.get(mContext, "user_type"))) {
                if (tv_zhifufangshi.isSelected()) {
                    contentObj.put("is_offline", 1);
                } else {
                    contentObj.put("is_offline", 0);
                }
            }
            requestParams.clear();
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.ORDER_PLACE2.getOrderMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            ToastUtil.showToast(mContext, getString(R.string.order_commit_success));
                            if (mTotalPayement <= 0 || ("dshop".equals(SharedPreferencesUtil.get(mContext, "user_type")) && tv_zhifufangshi.isSelected())) {
                                turnToOrderFragment();
                            } else {
                                String tmp = mOrderid;
                                int tmp1 = mWalletPay;
                                int total = mTotalPayement;
                                String mid = SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID);
                                SelectPayMethodActivity.startMe(mContext, mOrderid, mWalletPay, mTotalPayement, SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
                                finish();
                            }
                            //TODO
//                            if (type == 2){//家居
//                                CartJiaju.getInstance().clear(mContext);
//                                JiajuIns.getInstance().clear();
//                                SanmiConfig.isJiajuNeedRedresh = true;
//                            }else{//海外精品好吃懒做
                            DiandiCart.getInstance().clear(mContext);
//                                ProductIns.getInstance().clear();
                            SanmiConfig.isHaiwaiNeedRefresh = true;
                            SanmiConfig.isMallNeedRefresh = true;
//                            }
                        }

                        @Override
                        public void callBackLackError(Map<String, Integer> lackMap) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("当前以下产品缺货:\n\n");
                            if (lackMap != null && lackMap.size() > 0) {
                                OutStockDialog.getInstance().showDialog(mContext, "缺货提醒", lackMap, OutStockDialog.OUT_STOCK);
                            }
                        }
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

//    /**
//     * 取消购物车缺货产品
//     * @param keySet
//     */
//    private void canncelGoods(Set<String> keySet){
//        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext(); ) {
//            String key = iterator.next();
//            if (Cart.getInstance().contains(key)){
//                Cart.getInstance().remove(key);
//            }
//        }
//        showContent();
//    }

    private void turnToOrderFragment() {
        OrderFragment.startMe(mContext);
        finish();
    }


    private void refresh(String discountId) {
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            contentObj.put("orderid", mOrderid);
            if (!TextUtils.isEmpty(discountId)) {
                contentObj.put("discountid", discountId);
            }
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.ORDER_REFRESH2.getOrderMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            dealWithRefreshResult(obj);
                        }

                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshAddress(String addressId) {
        JSONObject contentObj = null;
        try {
            contentObj = PostHttpUtil.prepareContents(configMap, mContext);
            contentObj.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            contentObj.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(mContext,
                            ProjectConstant.APP_USER_SESSIONID));
            contentObj.put("orderid", mOrderid);
            if (!TextUtils.isEmpty(addressId)) {
                contentObj.put("addressid", mAddress.addressid);
            }
            PostHttpUtil.prepareParams(requestParams, contentObj.toString());
            sanmiAsyncTask.excutePosetRequest(
                    ServerUrlConstant.ORDER_REFRESH2.getOrderMethod(), requestParams,
                    new SanmiAsyncTask.ResultHandler() {
                        @Override
                        public void callBackForServerSuccess(String result) {
                            JsonObject obj = JsonUtility.parse(result);
                            dealWithRefreshResult(obj);
                        }

                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    private void dealWithRefreshResult(JsonObject obj) {
        //获取合伙人信息
        mHehuoren = JsonUtility.fromJson(obj.get("bind_cshop"), NetHehuoren.class);
        mHehuorens = JsonUtility.fromJson(obj.get("available_cshops"), new TypeToken<ArrayList<NetHehuoren>>() {
        });
        mProductsPayment = JsonUtility.fromJson(obj.get("products_payment"), Integer.class);
        mYunfei = JsonUtility.fromJson(obj.get("yunfei"), Integer.class);
        mYouhui = JsonUtility.fromJson(obj.get("discount_payment"), Integer.class);
        mTotalPayement = JsonUtility.fromJson(obj.get("total_payment"), Integer.class);
        showContent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            mAddress = AddressIns.getInstance().getmAddress();
            if (mAddress != null) {
                if (TextUtils.isEmpty(mOrderid)) {
                    applyOrder();
                } else {
                    refreshAddress(mAddress.getAddressId());
                }
                ll_address.setVisibility(View.VISIBLE);
                ll_address_empty.setVisibility(View.GONE);
                tv_name.setText(mAddress.getName());
                tv_phone.setText(mAddress.getPhone());
                tv_address.setText(mAddress.getAddress());
            } else {
                ll_address.setVisibility(View.GONE);
                ll_address_empty.setVisibility(View.VISIBLE);
            }
        }

        if (requestCode == 400 && resultCode == 400) {
            mHehuoren = (NetHehuoren) data.getSerializableExtra("kitchen");
            showHehuoren();
        }
    }

}
