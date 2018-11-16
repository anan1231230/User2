package com.hclz.client.base.bean;

import java.io.Serializable;

public class Position implements Serializable {
    // 定位信息，由百度sdk提供
    // 服务器搜索逻辑：
    // 先按照location搜索周围5km，没有的话按照district搜索，没有的话按照city搜索，再没有的话返回空，提示用户从地图查找接口查找。
    private double[] location;// [经度数字, 纬度数字],
    private String province;// 省名称，或者直辖市名称，如山东省，北京市
    private String city;// 城市名，如济南市，北京市
    private String district;// 区名称，如历下区
    private String name;//地区名字

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
