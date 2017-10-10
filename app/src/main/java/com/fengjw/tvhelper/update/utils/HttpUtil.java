package com.fengjw.tvhelper.update.utils;

import android.util.Log;

import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by fengjw on 2017/8/7.
 */

public class HttpUtil {

    public static String TGA = "HttpUtil";

    public static void sendOKHttpResquest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .build();
//.readTimeout(30, TimeUnit.SECONDS)
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
        Log.d(TGA, "sendOKHttpResquest");
    }

    /*
    这里主要是为了Https实现证书验证
     */

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    //
    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }
}
