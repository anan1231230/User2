package com.hclz.client.base.bean;

import com.hclz.client.kitchen.adapter.KitchenSearchAdapter;

import java.io.Serializable;
import java.util.List;

public class Kitchen implements Serializable {
    public int viewType = KitchenSearchAdapter.VIEW_TYPE_NORMAL;
    private String cid;
    private String title;
    private String contact;// 联系人
    private String phone;// 联系人手机号
    private double[] location;// [经度数字, 纬度数字]
    private String province;// 省名称，或者直辖市名称，如山东省，北京市
    private String city;// 城市名，如济南市，北京市
    private String city_id; //城市唯一识别id
    private String code;//推荐码
    private String district;// 区名称，如历下区
    private int distance;// 数字，距离，以米为单位

    private List<PositionsBean> positions;

    public List<PositionsBean> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionsBean> positions) {
        this.positions = positions;
    }

    public static class PositionsBean {
        private String address;
        private double latitude;
        private double longitude;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public double getDistanceHehuoren() {
        return distanceHehuoren;
    }

    public void setDistanceHehuoren(double distanceHehuoren) {
        this.distanceHehuoren = distanceHehuoren;
    }

    private double distanceHehuoren;//合伙人距离用户的距离,单位Km
    private boolean test;
    private int status;
    private String did;
    private String address;
    private String[] album_thumbnail;
    private String[][] promotions;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    // 'promotions': [
    // ['http://hclzdatadev.oss-cn-hangzhou.aliyuncs.com/images/promotion_tui_green.png',
    // '推荐厨房，优惠更多哦'],
    // ['http://hclzdatadev.oss-cn-hangzhou.aliyuncs.com/images/promotion_cu_cheng.png',
    // '中秋大促庆团圆']
    // ]

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String[] getAlbum_thumbnail() {
        return album_thumbnail;
    }

    public void setAlbum_thumbnail(String[] album_thumbnail) {
        this.album_thumbnail = album_thumbnail;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String[][] getPromotions() {
        return promotions;
    }

    public void setPromotions(String[][] promotions) {
        this.promotions = promotions;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

}
