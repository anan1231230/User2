package com.hclz.client.base.bean;

public class OrderExtra {

    private String[] couponids;
    private int payment_amount_cut;

    public String[] getCouponids() {
        return couponids;
    }

    public void setCouponids(String[] couponids) {
        this.couponids = couponids;
    }

    public int getPayment_amount_cut() {
        return payment_amount_cut;
    }

    public void setPayment_amount_cut(int payment_amount_cut) {
        this.payment_amount_cut = payment_amount_cut;
    }

}
