package com.hclz.client.shouye.newcart;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.order.confirmorder.bean.products.NetProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hjm on 16/9/26.
 */

public class DiandiCart {

    private static DiandiCart diandiCart = new DiandiCart();
    public ConcurrentHashMap<String, DiandiCartItem> mCart = new ConcurrentHashMap<>();

    private DiandiCart() {
    }

    public static DiandiCart getInstance() {
        return diandiCart;
    }

    public boolean isEmpty() {
        return mCart.isEmpty();
    }

    public void clear(Context context) {
        mCart.clear();
        String userPhone = SharedPreferencesUtil.get(context, ProjectConstant.APP_USER_PHONE);
        if (!TextUtils.isEmpty(userPhone)) {
            SharedPreferencesUtil.save(context, userPhone, "");
        }
    }

    public int length() {
        return mCart.size();
    }

    public void put(DiandiCartItem cartItem, Context context) {
        if (mCart.containsKey(cartItem.pid)) {
            mCart.remove(cartItem.pid);
        }
        if (cartItem.num > 0){
            mCart.put(cartItem.pid, cartItem);
        }
        saveCart(context);
    }

    public List<DiandiCartItem> get() {
        List<DiandiCartItem> cartList = new ArrayList<>();
        for (String pid : mCart.keySet()) {
            cartList.add(mCart.get(pid));
        }
        return cartList;
    }

    public List<DiandiCartItem> getArrange() {
        List<DiandiCartItem> cartList = new ArrayList<>();
        for (String pid : mCart.keySet()) {
            if (mCart.get(pid).num > 0){
                cartList.add(mCart.get(pid));
            }
        }
        return cartList;
    }

    public DiandiCartItem get(String pid) {
        return mCart.get(pid);
    }

    public Boolean contains(String pid) {
        return mCart.containsKey(pid);
    }

    public void remove(String key, Context context) {
        if (mCart.get(key) != null) {
            mCart.remove(key);
        }
        saveCart(context);
    }

    public void modify(List<NetProduct> mModifyProducts, Context context) {
        ConcurrentHashMap<String, DiandiCartItem> cart = new ConcurrentHashMap<>();
        for (NetProduct product : mModifyProducts) {
            if (mCart.containsKey(product.getPid())) {
                DiandiCartItem item = mCart.get(product.getPid());
                item.price = product.getPrice();
                item.num = product.getNum();
                cart.put(item.pid, item);
            }
        }
        mCart.clear();
        mCart = cart;

        saveCart(context);
    }

    private void saveCart(Context context) {
        String userPhone = SharedPreferencesUtil.get(context, ProjectConstant.APP_USER_PHONE);
        if (!TextUtils.isEmpty(userPhone)) {
            String cartJson = new Gson().toJson(DiandiCart.getInstance());
            SharedPreferencesUtil.save(context, userPhone, TextUtils.isEmpty(cartJson) ? "" : cartJson);
        }
    }

    public void loadCart(String json, Context context) {
        if (!TextUtils.isEmpty(json)) {
            DiandiCart cart = new Gson().fromJson(json, DiandiCart.class);
            if (cart != null && cart.length() > 0) {
                for (DiandiCartItem cartItem : cart.get()) {
                    DiandiCart.getInstance().put(cartItem, context);
                }
            }
        }
    }

    public Integer getTotlePrice() {
        Integer totlePrice = 0;
        for (DiandiCartItem cartItem : mCart.values()) {
            totlePrice += cartItem.price * cartItem.num;
        }
        return totlePrice;
    }

    public Integer getTotleNum() {
        Integer totleNum = 0;
        for (DiandiCartItem cartItem : mCart.values()) {
            totleNum += cartItem.num;
        }
        return totleNum;
    }
}
