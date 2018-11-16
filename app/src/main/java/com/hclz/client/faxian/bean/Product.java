package com.hclz.client.faxian.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by handsome on 16/2/16.
 */
public class Product {

    /**
     * album_thumbnail : ["http://hclzdata.oss-cn-hangzhou.aliyuncs.com/dandi/images/products/hcdn.png"]
     * ct : 2016-02-02 15:40:14
     * inventory : 8
     * name : 华少的奶
     * name_append :
     * pid : 101000003
     * price : 2
     * price_delta : 1
     * shopid : 05310005
     * status : 1
     * type1 : ["laidian"]
     * type2 : ["lingshi","shuiqianlaidian","dianyingbanlv"]
     * type3 : []
     * ui_style : default
     * ut : 2016-02-02 15:45:31
     */

    private List<Product2sEntity> product2s;

    public List<Product2sEntity> getProduct2s() {
        return product2s;
    }

    public void setProduct2s(List<Product2sEntity> product2s) {
        this.product2s = product2s;
    }

    public static class Product2sEntity implements Serializable {

        /**
         * album : ["http://hclzimages.oss-cn-hangzhou.aliyuncs.com/%E5%B7%A7%E5%8F%AF%E7%BE%8E/%E5%B7%A7%E5%8F%AF%E7%BE%8E%E9%98%BF%E5%8B%92%E4%BC%AF%E6%96%AF%E4%BB%80%E9%94%A6%E8%BF%B7%E4%BD%A0%E5%B7%A7%E5%85%8B%E5%8A%9B/1.jpg","http://hclzimages.oss-cn-hangzhou.aliyuncs.com/%E5%B7%A7%E5%8F%AF%E7%BE%8E/%E5%B7%A7%E5%8F%AF%E7%BE%8E%E9%98%BF%E5%8B%92%E4%BC%AF%E6%96%AF%E4%BB%80%E9%94%A6%E8%BF%B7%E4%BD%A0%E5%B7%A7%E5%85%8B%E5%8A%9B/2.jpg","http://hclzimages.oss-cn-hangzhou.aliyuncs.com/%E5%B7%A7%E5%8F%AF%E7%BE%8E/%E5%B7%A7%E5%8F%AF%E7%BE%8E%E9%98%BF%E5%8B%92%E4%BC%AF%E6%96%AF%E4%BB%80%E9%94%A6%E8%BF%B7%E4%BD%A0%E5%B7%A7%E5%85%8B%E5%8A%9B/3.jpg","http://hclzimages.oss-cn-hangzhou.aliyuncs.com/%E5%B7%A7%E5%8F%AF%E7%BE%8E/%E5%B7%A7%E5%8F%AF%E7%BE%8E%E9%98%BF%E5%8B%92%E4%BC%AF%E6%96%AF%E4%BB%80%E9%94%A6%E8%BF%B7%E4%BD%A0%E5%B7%A7%E5%85%8B%E5%8A%9B/4.jpg","http://hclzimages.oss-cn-hangzhou.aliyuncs.com/%E5%B7%A7%E5%8F%AF%E7%BE%8E/%E5%B7%A7%E5%8F%AF%E7%BE%8E%E9%98%BF%E5%8B%92%E4%BC%AF%E6%96%AF%E4%BB%80%E9%94%A6%E8%BF%B7%E4%BD%A0%E5%B7%A7%E5%85%8B%E5%8A%9B/5.jpg","http://hclzimages.oss-cn-hangzhou.aliyuncs.com/%E5%B7%A7%E5%8F%AF%E7%BE%8E/%E5%B7%A7%E5%8F%AF%E7%BE%8E%E9%98%BF%E5%8B%92%E4%BC%AF%E6%96%AF%E4%BB%80%E9%94%A6%E8%BF%B7%E4%BD%A0%E5%B7%A7%E5%85%8B%E5%8A%9B/6.jpg","http://hclzimages.oss-cn-hangzhou.aliyuncs.com/%E5%B7%A7%E5%8F%AF%E7%BE%8E/%E5%B7%A7%E5%8F%AF%E7%BE%8E%E9%98%BF%E5%8B%92%E4%BC%AF%E6%96%AF%E4%BB%80%E9%94%A6%E8%BF%B7%E4%BD%A0%E5%B7%A7%E5%85%8B%E5%8A%9B/7.jpg"]
         * album_thumbnail : ["http://hclzimages.oss-cn-hangzhou.aliyuncs.com/%E5%B7%A7%E5%8F%AF%E7%BE%8E/%E5%B7%A7%E5%8F%AF%E7%BE%8E%E9%98%BF%E5%8B%92%E4%BC%AF%E6%96%AF%E4%BB%80%E9%94%A6%E8%BF%B7%E4%BD%A0%E5%B7%A7%E5%85%8B%E5%8A%9B/suoluetu.jpg","http://hclzimages.oss-cn-hangzhou.aliyuncs.com/%E5%B7%A7%E5%8F%AF%E7%BE%8E/%E5%B7%A7%E5%8F%AF%E7%BE%8E%E9%98%BF%E5%8B%92%E4%BC%AF%E6%96%AF%E4%BB%80%E9%94%A6%E8%BF%B7%E4%BD%A0%E5%B7%A7%E5%85%8B%E5%8A%9B/suoluetu.jpg"]
         * dshop_inventory : 30
         * has_stock : 1
         * inventory : 9000000
         * inventory_count : [0,30,9000000]
         * min_plus_amount : [1,1,1]
         * min_purchase_amount : [1,1,1]
         * minimal_package : 1
         * name : 巧可美阿勒伯斯什锦迷你巧克力
         * name_append : 200g
         * pid : 7610209065491
         * praise : 0
         * price : [2500,3100,3800,5800,5800]
         * price_delta : 0
         * status : 1
         * tags : ["欧美","食品","休闲食品","巧克力"]
         * type1 : ["laidian"]
         * types : ["1"]
         */

        private int dshop_inventory;
        private int has_stock;
        private int minimal_package;
        private int praise;
        private List<Integer> inventory_count;
        private List<Integer> min_plus_amount;
        private List<Integer> min_purchase_amount;
        private Integer price_market;
        private List<String> types;
        private int inventory;
        private String name;
        private String name_append;
        private String pid;
        private int price_delta;
        private int status;
        private List<Integer> price;
        private List<String> album_thumbnail;
        private List<String> album;
        private List<String> type1;
        private List<String> tags;
        private String currentType;
        private String currentTypeName;


        public int getDshop_inventory() {
            return dshop_inventory;
        }

        public void setDshop_inventory(int dshop_inventory) {
            this.dshop_inventory = dshop_inventory;
        }

        public int getHas_stock() {
            return has_stock;
        }

        public void setHas_stock(int has_stock) {
            this.has_stock = has_stock;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        public int getInventory(int priceNum) {
            int inventory = 0;
            if (priceNum == 1) {//城代
                return inventory_count.get(2);
            } else if (priceNum == 2) {//合伙人
                return inventory_count.get(1);
            } else {
                return inventory_count.get(0) > inventory_count.get(1) ? inventory_count.get(0) : inventory_count.get(1);
            }
        }

        public int getMinimal_package() {
            return minimal_package;
        }

        public void setMinimal_package(int minimal_package) {
            this.minimal_package = minimal_package;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_append() {
            return name_append;
        }

        public void setName_append(String name_append) {
            this.name_append = name_append;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public int getPraise() {
            return praise;
        }

        public void setPraise(int praise) {
            this.praise = praise;
        }

        public int getPrice_delta() {
            return price_delta;
        }

        public void setPrice_delta(int price_delta) {
            this.price_delta = price_delta;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<String> getAlbum() {
            return album;
        }

        public void setAlbum(List<String> album) {
            this.album = album;
        }

        public List<String> getAlbum_thumbnail() {
            return album_thumbnail;
        }

        public void setAlbum_thumbnail(List<String> album_thumbnail) {
            this.album_thumbnail = album_thumbnail;
        }

        public List<Integer> getInventory_count() {
            return inventory_count;
        }

        public void setInventory_count(List<Integer> inventory_count) {
            this.inventory_count = inventory_count;
        }

        public List<Integer> getMin_plus_amount() {
            return min_plus_amount;
        }

        public void setMin_plus_amount(List<Integer> min_plus_amount) {
            this.min_plus_amount = min_plus_amount;
        }

        public List<Integer> getMin_purchase_amount() {
            return min_purchase_amount;
        }

        public void setMin_purchase_amount(List<Integer> min_purchase_amount) {
            this.min_purchase_amount = min_purchase_amount;
        }

        public List<Integer> getPrice() {
            return price;
        }

        public void setPrice(List<Integer> price) {
            this.price = price;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public List<String> getType1() {
            return type1;
        }

        public void setType1(List<String> type1) {
            this.type1 = type1;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getCurrentType() {
            return currentType;
        }

        public void setCurrentType(String currentType) {
            this.currentType = currentType;
        }

        public String getCurrentTypeName() {
            return currentTypeName;
        }

        public void setCurrentTypeName(String currentTypeName) {
            this.currentTypeName = currentTypeName;
        }

        public Integer getPrice_market() {
            return price_market;
        }

        public void setPrice_market(Integer price_market) {
            this.price_market = price_market;
        }


    }
}
