package com.hclz.client.faxian.bean;

import com.hclz.client.base.util.CommonUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by handsome on 16/7/27.
 */
public class Brand implements Serializable {

    public String name;
    public int fid;
    public List<String> decoration_img;

    public String brand_story;
    public String introduction;
    public List<String> store_img;
    private String cover_img;

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

    public List<String> getDecoration_img() {
        return decoration_img;
    }

    public void setDecoration_img(List<String> decoration_img) {
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

    public List<String> getStore_img() {
        return store_img;
    }

    public void setStore_img(List<String> store_img) {
        this.store_img = store_img;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }
}
