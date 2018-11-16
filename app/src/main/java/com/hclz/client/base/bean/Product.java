package com.hclz.client.base.bean;

import java.io.Serializable;

public class Product implements Serializable {

    private static final long serialVersionUID = -8562543322042831421L;
    private String[] about;
    private String[] album;// 相册，至少两张，第一张是半成品，后面几张都是成品
    private String[] album_thumbnail;// 头像缩略图，用于列表展示，必须是两个，第一个是半成品包装，第二个是成品
    private String[] do1;// 制作方法, 0~5张
    private String[] info;// 产品质量说明等
    private int inventory;// 库存，数字类型
    private int[] min_purchase_amount;// 最低采购量，第一个是普通用户商城下单最小量，第二个是社区店采购最小量，以后再扩展城市代理等
    private String name;
    private String pid;// 商品id，字符串类型
    private int praise;
    private String[] price;// [成本价, 城代采购价, 厨房采购价, 半成品价, 堂食价](单位：分)，数字类型。
    private String[][] promotions;//活动
    private int sort_id;
    private int status;// 0:下架, 1:正常，数字类型
    private String[] story;// 产品故事, 0~5张
    private String[] type1;
    private String ui_style;
    private String[] types;// 一个菜品可以属于多个类别，至少有一个
    private String[] tags;
    private String name_append;
    private int price_delta;
    //购物时使用
    private int count;

    public String getName_append() {
        return name_append;
    }

    public void setName_append(String name_append) {
        this.name_append = name_append;
    }

    public String[] getAbout() {
        return about;
    }

    public void setAbout(String[] about) {
        this.about = about;
    }

    public String[] getAlbum() {
        return album;
    }

    public void setAlbum(String[] album) {
        this.album = album;
    }

    public String[] getAlbum_thumbnail() {
        return album_thumbnail;
    }

    public void setAlbum_thumbnail(String[] album_thumbnail) {
        this.album_thumbnail = album_thumbnail;
    }

    public String[] getDo1() {
        return do1;
    }

    public void setDo1(String[] do1) {
        this.do1 = do1;
    }

    public String[] getInfo() {
        return info;
    }

    public void setInfo(String[] info) {
        this.info = info;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int[] getMin_purchase_amount() {
        return min_purchase_amount;
    }

    public void setMin_purchase_amount(int[] min_purchase_amount) {
        this.min_purchase_amount = min_purchase_amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int[] getPrice() {
        int tmp[] = new int[price.length];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = Integer.parseInt(price[i].replaceAll("\\D+", "").replaceAll("\r", "").replaceAll("\n", "").trim());
        }
        return tmp;
    }

    public void setPrice(String[] price) {
        this.price = price;
    }

    public String[][] getPromotions() {
        return promotions;
    }

    public void setPromotions(String[][] promotions) {
        this.promotions = promotions;
    }

    public int getSort_id() {
        return sort_id;
    }

    public void setSort_id(int sort_id) {
        this.sort_id = sort_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String[] getStory() {
        return story;
    }

    public void setStory(String[] story) {
        this.story = story;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String[] getType1() {
        return type1;
    }

    public void setType1(String[] type1) {
        this.type1 = type1;
    }

    public String getUi_style() {
        return ui_style;
    }

    public void setUi_style(String ui_style) {
        this.ui_style = ui_style;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pid == null) ? 0 : pid.hashCode());
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
        Product other = (Product) obj;
        if (pid == null) {
            if (other.pid != null)
                return false;
        } else if (!pid.equals(other.pid))
            return false;
        return true;
    }

}
