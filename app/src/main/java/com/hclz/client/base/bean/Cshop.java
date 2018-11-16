package com.hclz.client.base.bean;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by handsome on 2016/12/16.
 */

public class Cshop implements Serializable {
//            'cid' : string,
//            'did' : string,
//            'code': string,
//            'title': string,
//            'contact': string,
//            'location': [longitude, latitude]
//            'province': string,
//            'city': string,
//            'address': string,
//            'album_thumbnail': string
    public String cid;
    public String did;
    public String code;
    public String title;
    public String contact;
    public ArrayList<Double> location;
    public String province;
    public String city;
    public String address;
    public ArrayList<String> album_thumbnail;

}
