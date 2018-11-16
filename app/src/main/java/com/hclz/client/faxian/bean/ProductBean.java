package com.hclz.client.faxian.bean;

import com.hclz.client.order.confirmorder.bean.address.NetAddress;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hjm on 16/9/24.
 */

public class ProductBean implements Serializable {
    public String city;
    public String block_name;
    public List<ProductsBean> products;
    public NetAddress address;


    public static class ProductsBean implements Serializable, Comparable<ProductBean.ProductsBean> {
        public String album_thumbnail;
        public int inventory;
        public int is_bookable;
        public int is_direct_selling;
        public int minimal_plus;
        public int minimal_quantity;
        public int leadtime;
        public String name;
        public String name_append;
        public LimitPriceBean limit_price;
        public VirtualGoodsBean virtual_goods;
        public List<String> videos;
        public int has_stock;
        public NormalDetailBean normal_detail;
        public CshopDetailBean cshop_detail;
        public DshopDetailBean dshop_detail;
        public String pid;
        public int price;
        public int price_market;
        public int status;
        public String type1;
        public List<String> albums;
        public List<List<String>> properties;
        public List<String> tags;


        @Override
        public int compareTo(ProductBean.ProductsBean bean) {
            if (price < bean.price) //这里比较的是什么 sort方法实现的就是按照此比较的东西从小到大排列
                return -1;
            if (price > bean.price)
                return 1;
            return 0;
        }

        public static class NormalDetailBean implements Serializable {
            public int price_market;
            public int price;

        }

        public static class CshopDetailBean implements Serializable {
            public int selling_price;
            public int selling_profit;

        }

        public static class LimitPriceBean implements  Serializable {
            public long current_time;
            public long start_time;
            public long end_time;


            public long count_down;
            public long end_time_local;
            public boolean isStarted;


        }


        public static class DshopDetailBean implements Serializable {
            public int selling_price;
            public int selling_profit;
            public int cshop_selling_price;
            public int cshop_selling_profit;


        }

        public static class VirtualGoodsBean implements Serializable {
            public List<ProductBean.ProductsBean> real_goods;

        }

    }
}
