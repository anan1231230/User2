package com.hclz.client.base.util;

/**
 * Created by Administrator on 2015/12/24.
 */
public class PageInfo {
    public String pageId;
    public String pageName;
    public String type;
    public String url;
    public Class pageClass;
    public Class parentClass;

    public PageInfo(String pageId, String type, Class<?> cls) {
        this.pageId = pageId;
        this.type = type;
        this.pageClass = cls;
    }

    public PageInfo(String pageId, String type, Class<?> cls, Class<?> parentCls) {
        this.pageId = pageId;
        this.type = type;
        this.pageClass = cls;
        this.parentClass = parentCls;
    }

    public PageInfo(String type, String url, String pageName) {
        this.type = type;
        this.url = url;
        this.pageName = pageName;
    }
}
