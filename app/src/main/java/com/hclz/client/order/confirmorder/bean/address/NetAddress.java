package com.hclz.client.order.confirmorder.bean.address;

import com.hclz.client.base.util.CommonUtil;

import java.io.Serializable;

/**
 * Created by handsome on 16/7/27.
 */
public class NetAddress implements Serializable {

    public String addressid;

    public String province;
    public String city;
    public String district;
    public String addr_main;
    public String addr_detail;

    public int is_default;
    public String receiver_name;
    public String receiver_phone;

    public String getAddressId() {
        return addressid == null ? "" : addressid;
    }

    public String getName() {
        return receiver_name == null ? "" : receiver_name;
    }

    public String getPhone() {
        return receiver_phone == null ? "" : receiver_phone;
    }

    public String getAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(province == null ? "" : CommonUtil.replaceBlank(province)).append(" ");
        sb.append(city == null ? "" : CommonUtil.replaceBlank(city)).append(" ");
        sb.append(district == null ? "" : CommonUtil.replaceBlank(district)).append(" ");
        sb.append(addr_main == null ? "" : CommonUtil.replaceBlank(addr_main)).append(" ");
        sb.append(addr_detail == null ? "" : CommonUtil.replaceBlank(addr_detail));

        return sb.toString();

    }

    public String getSanji() {
        StringBuilder sb = new StringBuilder();
        sb.append(province == null ? "" : CommonUtil.replaceBlank(province)).append(" ");
        sb.append(city == null ? "" : CommonUtil.replaceBlank(city)).append(" ");
        sb.append(district == null ? "" : CommonUtil.replaceBlank(district)).append(" ");
        sb.append(addr_main == null ? "" : CommonUtil.replaceBlank(addr_main));
        return sb.toString();
    }

    public String getAddr_detail() {
        StringBuilder sb = new StringBuilder();
        sb.append(addr_detail == null ? "" : CommonUtil.replaceBlank(addr_detail)).append(" ");
        return sb.toString();
    }
}
