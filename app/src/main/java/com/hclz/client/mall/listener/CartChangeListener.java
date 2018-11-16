package com.hclz.client.mall.listener;

public interface CartChangeListener {
    public void onAddtoCart(int[] startLocation);

    public void onDelfromCart(int[] endLocation);
}
