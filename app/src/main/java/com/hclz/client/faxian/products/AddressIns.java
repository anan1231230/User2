package com.hclz.client.faxian.products;

import com.hclz.client.kitchen.bean.User;
import com.hclz.client.order.confirmorder.bean.address.NetAddress;

/**
 * Created by handsome on 2016/10/26.
 */

public class AddressIns {

    private static AddressIns addressIns;
    NetAddress mAddress;


    private AddressIns() {
    }

    public static AddressIns getInstance() {
        if (addressIns == null) {
            addressIns = new AddressIns();
        }
        return addressIns;
    }

    public void setAddress(NetAddress address){
        mAddress = address;
    }

    public NetAddress getmAddress(){
        return mAddress;
    }

}
