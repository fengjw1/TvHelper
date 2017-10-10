package com.fengjw.tvhelper.update.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by fengjw on 2017/8/24.
 */

public class NetworkStateReceiver extends BroadcastReceiver {

    private static final String TGA = "NetworkStateReceiver";
    private Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        try {
            ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo activeInfo = manager.getActiveNetworkInfo();
            if (wifiInfo.isConnected() || activeInfo.isConnected()){
                //Toast.makeText(mContext, "get Network!", Toast.LENGTH_SHORT).show();
                //test
                Log.d(TGA, "NetworkStateReceiver");
                Intent stService = new Intent(context, NetworkGetService.class);
                mContext.startService(stService);
                Log.d(TGA, "get Network!");
            }else {
                try {
                    Intent spService = new Intent(context,NetworkStateReceiver.class);
                    mContext.stopService(spService);
                    //Toast.makeText(mContext, "Not Network!", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
