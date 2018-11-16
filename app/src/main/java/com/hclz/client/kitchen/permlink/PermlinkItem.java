package com.hclz.client.kitchen.permlink;

public class PermlinkItem {

    public String icon;
    public String hint;
    public String tid;
    public String webview_url;
    public boolean enabled;

    public PermlinkItem() {
        //默认true
        enabled = true;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("icon:" + icon);
        sb.append(",hint:" + hint);
        sb.append(",webview_url:" + webview_url);
        sb.append(",enabled:" + enabled);
        return sb.toString();
    }

}
