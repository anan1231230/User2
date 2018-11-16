package com.hclz.client.forcshop.jiedanguanli.bean;

import com.hclz.client.base.util.CommonUtil;
import com.hclz.client.forcshop.jiedandetail.adapter.ProductInOrderAdapter;
import com.hclz.client.forcshop.jiedanguanli.adapter.JiedanGuanliAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by handsome on 16/7/9.
 */
public class JiedanguanliBean implements JiedanGuanliAdapter.JiedanItem{


    /**
     * address : 山东村山东省济南市天桥区
     * addressid : 39964ef83d9611e685dd00163e00717f
     * detail : 荆棘坎坷
     * location : ["117.006946","36.743885"]
     * name : ba
     * phone : 15662622763
     */

    public AddressEntity address;
    /**
     * address : {"address":"山东村山东省济南市天桥区","addressid":"39964ef83d9611e685dd00163e00717f","detail":"荆棘坎坷","location":["117.006946","36.743885"],"name":"ba","phone":"15662622763"}
     * cid : 05360003
     * cshop : {}
     * ct : 2016-07-04 16:04:00
     * delivery_type : 3
     * did : 05361001
     * eat_type : 2
     * extra : {}
     * inventory_source : dshopmall
     * mid : 21001356
     * orderid : 1607041603570001
     * past_seconds : 425231
     * payment_amount : 1500
     * payment_type : null
     * products : [{"album_thumbnail":[null],"count":1,"name":"羊肚丝","name_append":null,"pid":"40090022","price":[1165,1350,1500,1980],"price_delta":0}]
     * status : 49
     * status_detail : [{"desc":"提交订单成功","operator":"21001356","timestamp":"2016-07-04 16:04:00"},{"desc":"订单过期,系统自动取消!","operator":"后台服务","timestamp":"2016-07-04 16:21:34"},{"desc":"城代 接货","operator":"18340079622","timestamp":"2016-07-04 16:52:37"},{"desc":"城代 发货","operator":"18340079622","timestamp":"2016-07-04 16:52:40"},{"desc":"城代 送达合伙人","operator":"18340079622","timestamp":"2016-07-04 16:52:42"}]
     * transactionid : null
     * user_status : 0
     * ut : 2016-07-04 16:43:24
     * yunfei_amount : 0
     */

    public String cid;
    public String ct;
    public int delivery_type;
    public String did;
    public int eat_type;
    public ExtraEntity extra;
    public String inventory_source;
    public String mid;
    public String orderid;
    public int past_seconds;
    public int payment_amount;
    public Object payment_type;
    public int status;
    public Object transactionid;
    public int user_status;
    public String ut;
    public int yunfei_amount;
    /**
     * album_thumbnail : [null]
     * count : 1
     * name : 羊肚丝
     * name_append : null
     * pid : 40090022
     * price : [1165,1350,1500,1980]
     * price_delta : 0
     */

    public List<ProductsEntity> products;
    /**
     * desc : 提交订单成功
     * operator : 21001356
     * timestamp : 2016-07-04 16:04:00
     */

    public List<StatusDetailEntity> status_detail;
    public static class AddressEntity implements Serializable{
        public String address;
        public String addressid;
        public String detail;
        public String name;
        public String phone;
    }

    public static class ExtraEntity implements Serializable{
    }

    public static class ProductsEntity implements ProductInOrderAdapter.ProductInOrderItem {
        public int count;
        public String name;
        public String name_append;
        public String pid;
        public int price_delta;
        public List<String> album_thumbnail;
        public List<Integer> price;

        @Override
        public String getName() {
            if (name == null){
                if (name_append == null){
                    return "";
                } else {
                    return name_append;
                }
            } else {
                if (name_append == null){
                    return name;
                } else {
                    return name + name_append;
                }
            }
        }

        @Override
        public int getNum() {
            return count;
        }
    }

    public static class StatusDetailEntity implements Serializable {
        public String desc;
        public String operator;
        public String timestamp;
    }
    @Override
    public String getOrderId() {
        return orderid;
    }

    @Override
    public String getAddress() {
        return address.address;
    }

    @Override
    public String getPhone() {
        return address.phone;
    }

    @Override
    public String getYidengdai() {
        return CommonUtil.formatDuring(past_seconds);
    }

    @Override
    public int getPrice() {
        return payment_amount;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getCt() {
        return ct;
    }

}
