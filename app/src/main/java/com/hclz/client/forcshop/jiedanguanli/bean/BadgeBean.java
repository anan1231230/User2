package com.hclz.client.forcshop.jiedanguanli.bean;

/**
 * Created by hjm on 16/7/29.
 */

public class BadgeBean {

    private static BadgeBean badgeBean = new BadgeBean();

    private BadgeBean() {
    }

    public String[] results = new String[]{"", "", "", ""};
    public String[] badges = new String[]{"0", "0", "0", "0"};

    public static BadgeBean getInstence() {
        return badgeBean;
    }
}
