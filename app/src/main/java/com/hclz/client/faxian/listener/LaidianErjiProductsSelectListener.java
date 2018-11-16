package com.hclz.client.faxian.listener;

import com.hclz.client.faxian.bean.Type;

public interface LaidianErjiProductsSelectListener {
    public void onClick(Type.Type1Entity.Type2Entity type2);

    public void onClick(Type.Type1Entity.Type2Entity type2, Type.Type1Entity.Type2Entity.Type3Entity type3);

    interface CartChangeListener {
        public void onAddtoCart(int[] startLocation);

        public void onDelfromCart(int[] endLocation);
    }
}