package com.hclz.client.base.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable{
    public AddressBean address;
    public String cid;
    public String ct;
    public String did;
    public int freight_amount;
    public String mid;
    public String order_type;
    public String orderid;
    public int past_seconds;
    public int payment_amount;
    public String payment_type;
    public String service_phone;
    public int status;
    public int wallet_pay;
    public String waretype;
    public ArrayList<OperationBean> operation;
    public ArrayList<ProductsBean> products;
    public ArrayList<StatusDetailBean> status_detail;
    public static class AddressBean implements Serializable{
        public String addr_detail;
        public String addr_main;
        public String city;
        public String district;
        public String province;
        public String receiver;
        public String receiver_phone;
    }

    public static class OperationBean implements Serializable {
        public String display;
        public String op;
    }

    public static class ProductsBean implements Serializable {
        public String album_thumbnail;
        public int deal_count;
        public int deal_price;
        public String name;
        public String name_append;
        public String pid;
        public int total;
    }

    public static class StatusDetailBean implements Serializable {
        public String desc;
        public String operator;
        public String timestamp;
    }
}
