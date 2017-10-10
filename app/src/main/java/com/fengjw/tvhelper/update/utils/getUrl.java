package com.fengjw.tvhelper.update.utils;

import android.os.SystemProperties;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by fengjw on 2017/9/4.
 */

public class getUrl {
    public static String getRemoteUri() {
        String apkPath = "http://192.168.1.14:8800/index.php/apkapi?model=TV918&product=ktc&sdanum=SDA123456789";
        String uri = "http://" + getRemoteHost() + "/index.php/apkapi?model=" + getOtaProductName()
                + "&product=" + getCustomer()
                + "&sdanum=" +  getSDANum_Ini();
        Log.v(TAG, "uri="+uri);
        return uri;
    }

    public static String getRemoteHost() {
        //String remoteHost = SystemProperties.get("ro.product.ota.host");
        String remoteHost = null;
        if(remoteHost == null || remoteHost.length() == 0) {
            remoteHost = "192.168.1.14:8800";
        }
        return remoteHost;
    }

    public static String getOtaProductName() {
        //modify for new ota server, "ro.product.model" change to "ktc.ota.model", zjd20160428
        String productName = SystemProperties.get("ktc.ota.model");
        if(productName.contains(" ")) {
            productName = productName.replaceAll(" ", "");
        }

        return productName;
    }

    public static String getCustomer(){
        String customer = SystemProperties.get("ktc.ota.customer");
        if(customer == null || customer.length() == 0) {
            customer = "ktc";
        }
        if(customer.contains(" ")) {
            customer = customer.replaceAll(" ", "");
        }
        return customer;
    }
    public static String getSDANum_Ini(){
        String sdaNum = getProp("/config/model/Customer_1.ini", "PRODUCT_SDA_NO");
        sdaNum = sdaNum.replaceAll("\"", "");
        sdaNum = sdaNum.replaceAll(";", "");
        sdaNum = sdaNum.replaceAll(" ", "");

        return sdaNum;
    }

    //to parser Customer_1.ini
    public static String getProp(String file, String key)
    {
        String value = "";
        Properties props = new Properties();
        InputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(file));// "/system/build.prop"
            props.load(in);
            value = props.getProperty(key);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(value != null){
            String[] array = value.split(";");
            if (array[0].length() > 0){
                value = array[0];
            }
            value = value.replace("\"", "");
            value = value.trim();
            return value;
        }
        else
            return "";
    }
}
