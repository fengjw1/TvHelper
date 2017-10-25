package com.fengjw.tvhelper.stop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.settingslib.applications.ApplicationsState;
import com.fengjw.tvhelper.R;
import com.fengjw.tvhelper.stop.utils.AppsInfo;
import com.fengjw.tvhelper.stop.utils.DomXml;
import com.fengjw.tvhelper.stop.utils.Filter;
import com.fengjw.tvhelper.stop.utils.ForceStopManager;
import com.fengjw.tvhelper.stop.utils.StopAppInfo;

import java.util.ArrayList;
import java.util.List;

import static com.fengjw.tvhelper.stop.StopRunActivity.TGA;

public class AppManagementActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AppManagementActivity";

    private ImageView mAppImage;
    private TextView m1AppTv;
    private TextView mNameApp;
    private TextView mVersionApp;
    private LinearLayout mAppLin;
    private TextView mOpenApp;
    private TextView mStopAppTv;
    private StopAppInfo mAppInfo;

    private DomXml mXml;
    private List<Filter> mFilters;
    private AppsInfo mAppsInfo;
    private List<ApplicationsState.AppEntry> mList;
    private List<StopAppInfo> mAppInfoList;
    private String packageName;
    private int position;

    private static final int STOP_RUN = 1;
    private static final int CONTINUE_RUN = 2;

    private ForceStopManager mForceStopManager;
    private ApplicationsState mApplicationsState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_app);

        packageName = getIntent().getStringExtra("name");
        //position = getIntent().getIntExtra("position", 0);

        Log.d(TAG, "packageName : " + packageName);

        initView();
        init();
        bindView();

    }

    private void init(){
        try {
            mXml = new DomXml(this);
            mFilters = mXml.XMLResolve();
            Log.d(TAG, "xml size : " + mFilters.size());
            for (int i = 0; i < mFilters.size(); i ++){
                Log.d(TGA, mFilters.get(i).getName());
            }
            mAppsInfo = new AppsInfo(this);
            mAppInfoList = new ArrayList<>();
            Log.d(TAG, "Enter mAppInfo");
            mAppsInfo.init();
            mList = mAppsInfo.rebuildRunning();
            for (ApplicationsState.AppEntry appEntry : mList){
                try {
                    StopAppInfo appInfo = new StopAppInfo(this, appEntry);
                    mAppInfoList.add(appInfo);
                }catch (Exception e){
                    Log.d(TGA, e.getMessage());
                    e.printStackTrace();
                }
            }

            //xml 过滤
//            showLog("xml 过滤");
//            for (int i = 0; i < mAppInfoList.size(); i ++) {
//                for (int j = 0; j < mFilters.size(); j ++) {
//                    if (mFilters.get(j).getName().equals(mAppInfoList.get(i).getPackageName())){
//                        mAppInfoList.remove(i);
//                        //Log.d(TGA, "remove pkgName : " + mAppInfoList.get(i).getPackageName());
//                    }
//                }
//            }

            showLog("size is " + mAppInfoList.size());
            for (int i = 0; i < mAppInfoList.size(); i ++){
                if (packageName.equals(mAppInfoList.get(i).getPackageName())){
                    mAppInfo = mAppInfoList.get(i);
                    showLog("position : " + i);
                    break;
                }
            }
            //mAppInfo = mAppInfoList.get(position);

            Log.d(TAG, "mAppInfo : " + mAppInfo.getPackageName() + " " + mAppInfo.getName());

            mForceStopManager = new ForceStopManager(this, mAppInfo);
            mApplicationsState = ApplicationsState.getInstance(getApplication());
            Log.d(TAG, "mAppInfoList Size is " + mAppInfoList.size());

        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    private void onForceStopOk() {
        mForceStopManager.forceStop(mApplicationsState);
        Log.d(TAG, "onForceStopOK");
        //onBackPressed();
    }

    private void bindView(){
        mAppImage.setImageDrawable(mAppInfo.getIconResource());
        mNameApp.setText(mAppInfo.getName());
        mVersionApp.setText(mAppInfo.getVersion());
    }

    private void initView() {
        mAppImage = (ImageView) findViewById(R.id.image_app);
        mAppImage.setOnClickListener(this);
        m1AppTv = (TextView) findViewById(R.id.tv_1_app);
        mNameApp = (TextView) findViewById(R.id.tv_name_app);
        mNameApp.setOnClickListener(this);
        mVersionApp = (TextView) findViewById(R.id.tv_version_app);
        mVersionApp.setOnClickListener(this);
        mAppLin = (LinearLayout) findViewById(R.id.lin_app);
        mOpenApp = (TextView) findViewById(R.id.tv_open_app);
        mOpenApp.setOnClickListener(this);
        mStopAppTv = (TextView) findViewById(R.id.tv_stop_app);
        mStopAppTv.setOnClickListener(this);

        //bindView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_app:
                // TODO 17/10/23
                break;
            case R.id.tv_name_app:
                // TODO 17/10/23
                break;
            case R.id.tv_version_app:
                // TODO 17/10/23
                break;
            case R.id.tv_open_app:
                // TODO 17/10/23
                //toast("open");
                startApp(packageName);
                break;
            case R.id.tv_stop_app:
                // TODO 17/10/23
                toast("已停止运行" + mAppInfo.getName());
                if (mForceStopManager.canForceStop()){
                        onForceStopOk();
                        //Log.d(TGA, "getItemId : " + getItemId(position));
                        //Log.d(TGA, "getItemViewType : " + getItemViewType(position));
                        //removeData(position);
                        Log.d(TGA, "delete");
                    //Intent intent = new Intent(this, StopRunActivity.class);
                    //intent.putExtra("position", position);
                    this.setResult(STOP_RUN);
                    finish();
                    }else {
                        Log.d(TGA, "no delete");
                    }

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            try {
                this.setResult(CONTINUE_RUN);
                finish();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    //
    public void startApp(String appPackageName){
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
            finish();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "当前apk不能打开", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLog(String str){
        Log.d(TAG, str);
    }

}
