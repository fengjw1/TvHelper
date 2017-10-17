package com.fengjw.tvhelper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.settingslib.applications.ApplicationsState;
import com.fengjw.tvhelper.stop.StopRunActivity;
import com.fengjw.tvhelper.stop.StopRunningActivity;
import com.fengjw.tvhelper.stop.utils.AppsInfo;
import com.fengjw.tvhelper.stop.utils.StopAppInfo;
import com.fengjw.tvhelper.update.DownloadAllActivity;

import org.evilbinary.tv.widget.BorderView;

import java.util.ArrayList;
import java.util.List;

import static com.fengjw.tvhelper.stop.StopRunningActivity.TGA;


public class MainActivity extends Activity implements View.OnClickListener {

    private RelativeLayout mLayout;

    /*
    这里是预加载一次
     */
    private AppsInfo mAppsInfo;
    private List<StopAppInfo> mAppInfoList;
    private List<ApplicationsState.AppEntry> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        try {
            /*
            如果这里不执行一次，那么后面的activity无法直接获取完整的列表，具体不清楚原因。
             */
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        BorderView border = new BorderView(this);
        border.setBackgroundResource(R.drawable.border_highlight);
        mLayout  = (RelativeLayout) findViewById(R.id.main);
        border.attachTo(mLayout);

        for (int i = 0; i < mLayout.getChildCount(); i ++){
            mLayout.getChildAt(i).setOnClickListener(this);
        }

    }


    private void init(){
        try {
            mAppsInfo = new AppsInfo(this);
            mAppInfoList = new ArrayList<>();
            Log.d(TGA, "Enter mAppInfo");
            mAppsInfo.init();
            mList = mAppsInfo.rebuildRunning();

            Log.d(TGA, "mList Size is " + mList.size());
            for (ApplicationsState.AppEntry appEntry : mList){
                try {
                    StopAppInfo appInfo = new StopAppInfo(this, appEntry);
                    mAppInfoList.add(appInfo);
                }catch (Exception e){
                    Log.d(TGA, e.getMessage());
                    e.printStackTrace();
                }
            }
            for (StopAppInfo appInfo : mAppInfoList){
                Log.d(TGA, "appInfo: name =" + appInfo.getName()
                        + " CacheSize =" + appInfo.getCacheSize()
                        + " DateSize =" + appInfo.getDataSize()
                        + " Size =" + appInfo.getSize()
                        + " Version =" + appInfo.getVersion());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if (view == mLayout.getChildAt(0)){
            intent.setClass(this, DownloadAllActivity.class);
            startActivity(intent);
        }else if (view == mLayout.getChildAt(1)){
            intent.setClass(this, StopRunActivity.class);
            startActivity(intent);
        }
    }
}
