package com.hclz.client.base.bean;

import java.util.HashMap;

public class Coupon {
    private String mid;// 'mid': '',
    private String couponid;// 'couponid': '', //某个人的一张唯一卡券的id
    private String title;// 'title': '1分代金券',
    private String subtitle;// 'subtitle': '1分代金券（无地区使用限制）',
    /**
     * 'desc': [ '在有效期内（1一个月）使用', '订单满3分可以使用', '无地区使用限制', '不能与其它优惠信息同时使用' ],
     */
    private String[] desc;
    private int status;// 卡券状态，1：正常，2：自动过期，3：已经消费 'status': 1/2/3,
    private HashMap<String, String> scope;// 使用范围, 没有该字段，或者为空的话表示没有限制'scope':
    // {'level':'dshopmall',//
    // level对应的对象的id，如下面是济南城代的did
    // 'level_obj_id': '05311001'},
    private String style;// 卡券的UI样式，没有或者不支持的默认为default 'style': 'default/red',
    private long expire_utcms;// 卡券的过期时间（UTC毫秒数）'expire_utcms': 1444717652553,
    // private String expire_ui;//卡券的过期时间，用于ui显示 'expire_ui': '2015-10-15
    // 12:33:33',
    private int coupon_type;// 类型，1:代金券，2:打折卡，3:满减卡'coupon_type': 1,
    // // 1:代金券
    private int coupon_value;// 代金金额，单位为（分），整数 'coupon_value': 1
    // // 3:满减卡
    private int coupon_minpay;// 订单最小支付金额，单位为（分），整数 'coupon_minpay': 3
    private int coupon_cutpay;// 减多少，单位为（分），整数 'coupon_cutpay': 1,

    private String ct;// 创建时间'ct': '2015-15-13 23:11:11'

    private int coupon_maxcutpay;
    private int coupon_rate;

    private String consumed_id;
    private int payment_amount_cut;
    private String ut;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getCouponid() {
        return couponid;
    }

    public void setCouponid(String couponid) {
        this.couponid = couponid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String[] getDesc() {
        return desc;
    }

    public void setDesc(String[] desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, String> getScope() {
        return scope;
    }

    public void setScope(HashMap<String, String> scope) {
        this.scope = scope;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public long getExpire_utcms() {
        return expire_utcms;
    }

    public void setExpire_utcms(long expire_utcms) {
        this.expire_utcms = expire_utcms;
    }

    public int getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(int coupon_type) {
        this.coupon_type = coupon_type;
    }

    public int getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(int coupon_value) {
        this.coupon_value = coupon_value;
    }

    public int getCoupon_minpay() {
        return coupon_minpay;
    }

    public void setCoupon_minpay(int coupon_minpay) {
        this.coupon_minpay = coupon_minpay;
    }

    public int getCoupon_cutpay() {
        return coupon_cutpay;
    }

    public void setCoupon_cutpay(int coupon_cutpay) {
        this.coupon_cutpay = coupon_cutpay;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public int getCoupon_maxcutpay() {
        return coupon_maxcutpay;
    }

    public void setCoupon_maxcutpay(int coupon_maxcutpay) {
        this.coupon_maxcutpay = coupon_maxcutpay;
    }

    public int getCoupon_rate() {
        return coupon_rate;
    }

    public void setCoupon_rate(int coupon_rate) {
        this.coupon_rate = coupon_rate;
    }

    public String getConsumed_id() {
        return consumed_id;
    }

    public void setConsumed_id(String consumed_id) {
        this.consumed_id = consumed_id;
    }

    public int getPayment_amount_cut() {
        return payment_amount_cut;
    }

    public void setPayment_amount_cut(int payment_amount_cut) {
        this.payment_amount_cut = payment_amount_cut;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((couponid == null) ? 0 : couponid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coupon other = (Coupon) obj;
        if (couponid == null) {
            if (other.couponid != null)
                return false;
        } else if (!couponid.equals(other.couponid))
            return false;
        return true;
    }

}
