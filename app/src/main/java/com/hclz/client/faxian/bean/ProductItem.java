package com.hclz.client.faxian.bean;

public class ProductItem {
    //ID
    public String pid;

    //标题
    public String title;

    //显示类型
    public int viewType;

    //描述
    public String description;

    //数量
    public int count;

    public ProductItem(String title, int viewType) {
        this.pid = title;
        this.title = title;
        this.viewType = viewType;
        this.count = 0;
        this.description = "暂时先这么解释吧";
    }
}
