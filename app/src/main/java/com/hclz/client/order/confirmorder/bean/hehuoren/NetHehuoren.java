package com.hclz.client.order.confirmorder.bean.hehuoren;

import java.io.Serializable;

/**
 * Created by handsome on 16/7/27.
 */
public class NetHehuoren implements Serializable {

    public String cid;
    public String title;
    public String contact;// 联系人
    public String phone;// 联系人手机号
    public double[] location;// [经度数字, 纬度数字]
    public String province;// 省名称，或者直辖市名称，如山东省，北京市
    public String city;// 城市名，如济南市，北京市
    public String city_id; //城市唯一识别id
    public String code;//推荐码
    public String district;// 区名称，如历下区
    public int distance;// 数字，距离，以米为单位
    public double distanceHehuoren;//合伙人距离用户的距离,单位Km
    public boolean test;
    public int status;
    public String did;
    public String address;
    public String[] album_thumbnail;
    public String[][] promotions;

    public String getCid() {
        return cid;
    }

    public String getAlbum() {
        return (album_thumbnail==null||album_thumbnail.length<=0)?"":album_thumbnail[0];
    }

    public String getName() {
        return contact==null?"":contact;
    }

    public String getPhone() {
        return phone==null?"":phone;
    }

    public String getAddress() {
        return address==null?"":address;
    }
}
