package com.hclz.client.base.bean;

import java.util.List;

public class Type {

    private String tid;
    private String name;
    private List<Product> products;
    //购物使用
    private int count;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 获取Item内容
     *
     * @param pPosition
     * @return
     */
    public Object getItem(int pPosition) {
        // Category排在第一位
        if (pPosition == 0) {
            return name;
        } else {
            return products.get(pPosition - 1);
        }
    }

    /**
     * 当前类别Item总数。Type也需要占用一个Item
     *
     * @return
     */
    public int getItemCount() {
        return products.size() + 1;
    }
}
