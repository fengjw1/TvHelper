package com.fengjw.tvhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.settingslib.applications.ApplicationsState;
import com.fengjw.tvhelper.stop.StopRunningActivity;
import com.fengjw.tvhelper.stop.utils.AppsInfo;
import com.fengjw.tvhelper.stop.utils.StopAppInfo;
import com.fengjw.tvhelper.update.DownloadAllActivity;

import java.util.ArrayList;
import java.util.List;

import static com.fengjw.tvhelper.stop.StopRunningActivity.TGA;


public class MainActivity extends AppCompatActivity {

    private Button btn_update;
    private Button btn_stoprunning;

    /*
    这里是预加载一次
     */
    private AppsInfo mAppsInfo;
    private List<StopAppInfo> mAppInfoList;
    private List<ApplicationsState.AppEntry> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            /*
            如果这里不执行一次，那么后面的activity无法直接获取完整的列表，具体不清楚原因。
             */
            init();
        }catch (Exception e){
            e.printStackTrace();
        }

        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DownloadAllActivity.class);
                startActivity(intent);
            }
        });

        btn_stoprunning = (Button) findViewById(R.id.btn_stoprunning);
        btn_stoprunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StopRunningActivity.class);
                startActivity(intent);
            }
        });
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
}
