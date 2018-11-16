package com.hclz.client.order.confirmorder.bean.discount;

/**
 * Created by handsome on 16/7/27.
 */
public class NetDiscount {

    public String desc;
    public int discount_delta;
    public String discountid;
    public String title;
    public int used;

    public String getDiscountid() {
        return discountid==null?"":discountid;
    }

    public String getDiscountTitle() {
        return title==null?"":title;
    }

    public String getDiscountDesc() {
        return desc==null?"":desc;
    }

    public int getDiscountDelta() {
        return discount_delta;
    }

    public boolean isSelected(){
        return used==0?false:true;
    }
}
