package com.hclz.client.order.confirmorder.bean.staff;

import com.hclz.client.base.util.CommonUtil;

import java.io.Serializable;

/**
 * Created by handsome on 16/7/27.
 */
public class NetStaff implements Serializable {

    private String staff_id;

    private String staff_job;
    private String staff_name;
    private String staff_phone;
    private int is_default;

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getStaff_job() {
        return staff_job;
    }

    public void setStaff_job(String staff_job) {
        this.staff_job = staff_job;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getStaff_phone() {
        return staff_phone;
    }

    public void setStaff_phone(String staff_phone) {
        this.staff_phone = staff_phone;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }
}
