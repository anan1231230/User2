package com.hclz.client.base.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.util.ToastUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by hjm on 16/7/12.
 */

public class LocationUtils {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private static LocationUtils locationUtils;
    private android.location.LocationListener netLocationListener, gpsLocationListener;
    private Context mContext;
    private WeakHandler weakHandler = new WeakHandler();
    private static Boolean isFirst;//是否为第一次定位

    private LocationUtils() {
    }

    public static LocationUtils getInstence() {
        if (locationUtils == null) {
            isFirst = true;
            locationUtils = new LocationUtils();
        }
        return locationUtils;
    }

    public void start(Context context, LocationListener locationListener) {
        this.mContext = context;
        if (isFirst){
            init();
            isFirst = false;
        }
        setLocationListener(locationListener);

        weakHandler.post(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            openLocation();
        }
    };

    private void openLocation() {
        if (isOPenLocation(mContext)) {//是否开启定位
            openNetLocation();
            openGPSLocation();
        }
        if (!isOPenLocation(mContext)) {
            ToastUtil.showLongToast(mContext,"您当前尚未开启定位服务,请进入 设置-位置信息-开启定位服务");
        }
    }

    private void init() {
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
    }

    private void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    private void openNetLocation() {
        netLocationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) { //位置发生改变时调用
                locationListener.onResultLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {//provider失效时调用

            }

            @Override
            public void onProviderEnabled(String provider) {//provider启用时调用

            }

            @Override
            public void onProviderDisabled(String provider) {//状态改变时调用

            }
        };
        //开启网络定位
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, netLocationListener);
        } catch (SecurityException e){
            ToastUtil.showLongToast(mContext,"您当前尚未开启定位服务,请进入 设置-位置信息-开启定位服务");
        }
    }

    private void openGPSLocation() {
        gpsLocationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                locationListener.onResultLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        //开启GPS定位
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, gpsLocationListener);
        } catch (SecurityException e){
            ToastUtil.showLongToast(mContext,"您当前尚未开启定位服务,请进入 设置-位置信息-开启定位服务");
        }
    }

    public void stop() {
        if (netLocationListener != null && locationManager != null) {
            try {
                locationManager.removeUpdates(netLocationListener);
            } catch (SecurityException e){
                //do nothing
            }
        }
        if (gpsLocationListener != null && locationManager != null) {
            try {
                locationManager.removeUpdates(gpsLocationListener);
            } catch (SecurityException e){
                //do nothing
            }
        }
    }

    public String getCity(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
//            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
//                sb.append(address.getAddressLine(0)).append("\n");
//                sb.append(address.getLocality()).append("\n");
//                sb.append(address.getCountryName());
                return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static boolean isOPenLocation(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }


}