package com.hclz.client.faxian.bean;

import com.hclz.client.order.confirmorder.bean.address.NetAddress;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hjm on 16/9/24.
 */

public class BrandBean{

    private List<BrandBeanList> BrandBeanList;

    public List<BrandBean.BrandBeanList> getBrandBeanList() {
        return BrandBeanList;
    }

    public void setBrandBeanList(List<BrandBean.BrandBeanList> brandBeanList) {
        BrandBeanList = brandBeanList;
    }

    public static class BrandBeanList{
        public String name;
        public int fid;
        public String decoration_img;

        public String brand_story;
        public String introduction;
        public String store_img;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
        }

        public String getDecoration_img() {
            return decoration_img;
        }

        public void setDecoration_img(String decoration_img) {
            this.decoration_img = decoration_img;
        }

        public String getBrand_story() {
            return brand_story;
        }

        public void setBrand_story(String brand_story) {
            this.brand_story = brand_story;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getStore_img() {
            return store_img;
        }

        public void setStore_img(String store_img) {
            this.store_img = store_img;
        }
    }
}
