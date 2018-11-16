package com.hclz.client.base.cart;

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

/**
 * Created by hjm on 16/7/29.
 */

public class Cart {
    private static Cart mCartInfo = null;
    public Integer mTotalPrice;
    public Integer mTotalDeltaPrice;
    public Integer mTotalNum;
    public int mEatType = 1;//TODO
    public int mSendType = 1;//TODO
    public String mAddressid;
    Map<String, CartItem> mCart;


    public Cart() {
        mCart = new HashMap<String, CartItem>();
        mTotalPrice = 0;
        mTotalDeltaPrice = 0;
        mTotalNum = 0;
    }

    public static Cart getInstance() {
        if (mCartInfo == null) {
            mCartInfo = new Cart();
        }
        return mCartInfo;
    }

    public boolean isEmpty() {
        return mCart.isEmpty();
    }

    public void clear(Context context) {
        mCart.clear();
        mTotalPrice = 0;
        mTotalDeltaPrice = 0;
        mTotalNum = 0;

        String userPhone = SharedPreferencesUtil.get(context, ProjectConstant.APP_USER_PHONE);
        if (!TextUtils.isEmpty(userPhone)) {
            SharedPreferencesUtil.save(context, userPhone, "");
        }
    }

    public int length() {
        return mCart.size();
    }

    public void put(CartItem cartItem, Context context) {
        CartItem tmp = mCart.get(cartItem.pid);

        mTotalNum += cartItem.num;
        mTotalPrice += cartItem.sumprice;
        mTotalDeltaPrice += cartItem.sumdeltaprice;

        //first time added to cart
        if (tmp == null) {
            mCart.put(cartItem.pid, cartItem);
        } else {
            Integer sum = tmp.num + cartItem.num;
            //if the final number is 0
            if (sum <= 0) {
                mCart.remove(tmp.pid);
            } else {
                tmp.num = sum;
            }
        }

        saveCart(context);
    }

    public List<CartItem> get() {
        List<CartItem> cartList = new ArrayList<CartItem>();
        for (String pid : mCart.keySet()) {
            cartList.add(mCart.get(pid));
        }
        return cartList;
    }

    public CartItem get(String pid) {

        return mCart.get(pid);

    }

    public Boolean contains(String pid) {
        return mCart.containsKey(pid);
    }

    public int getCountInType(String tid) {
        int count = 0;
        for (String pid : mCart.keySet()) {
            String tidName = mCart.get(pid).tid;
            if (TextUtils.isEmpty(tidName)) {
                continue;
            }
            if ((mCart.get(pid).tid).equals(tid)) {
                count += mCart.get(pid).num;
            }
        }
        return count;
    }

    public void remove(String key, Context context) {
        if (mCart.get(key) != null) {
            mCart.remove(key);
        }

        saveCart(context);
    }

    public void modify(List<NetProduct> mModifyProducts, Context context) {
        mTotalNum = 0;
        Map<String, CartItem> cart = new HashMap<>();
        for (NetProduct product : mModifyProducts) {
            if (mCart.containsKey(product.getPid())) {
                CartItem item = mCart.get(product.getPid());
                item.price = product.getPrice();
                item.num = product.getNum();
                mTotalNum += item.num;
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
            String cartJson = new Gson().toJson(Cart.getInstance());
            SharedPreferencesUtil.save(context, userPhone, TextUtils.isEmpty(cartJson) ? "" : cartJson);
        }
    }

    public void loadCart(String json, Context context) {
        if (!TextUtils.isEmpty(json)) {
            Cart cart = new Gson().fromJson(json, Cart.class);
            if (cart != null && cart.length() > 0) {
                for (CartItem cartItem : cart.get()) {
                    Cart.getInstance().put(cartItem, context);
                }
            }
        }
    }
}
