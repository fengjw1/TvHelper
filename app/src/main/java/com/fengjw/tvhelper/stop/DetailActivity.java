package com.fengjw.tvhelper.stop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.settingslib.applications.ApplicationsState;
import com.fengjw.tvhelper.R;
import com.fengjw.tvhelper.stop.utils.ForceStopManager;
import com.fengjw.tvhelper.stop.utils.StopAppInfo;

import static com.fengjw.tvhelper.stop.StopRunningActivity.TGA;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    private Button stopBtn;
    private ForceStopManager mForceStopManager;
    private ApplicationsState mApplicationsState;
    private ApplicationsState.Session mSession;
    private PackageManager mPackageManager;
    private StorageManager mStorageManager;
    private String mPackageName;
    private StopAppInfo mAppInfo;

    private TextView title;
    private Button cancel;
    private Button sure;
    private String str = "是否强行停止运行应用？";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_dialog);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1.0f;
        getWindow().setAttributes(params);
        //getWindow().setGravity(Gravity.TOP | Gravity.LEFT);
        getWindow().setGravity(Gravity.CENTER);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(str);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        sure = (Button)findViewById(R.id.sure);
        sure.setOnClickListener(this);
        //final Intent intent = getIntent();
        try {
            init();
        }catch (Exception e){
            e.printStackTrace();
        }

        //setContentView(R.layout.activity_detail);
        Log.d(TGA, "enter the OnclickListener");
//        try {
//            stopBtn = (Button) findViewById(R.id.stopApp);
//            stopBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mForceStopManager.canForceStop()){
//                        onForceStopOk();
//                        Intent intent = new Intent(DetailActivity.this, StopRunningActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }else {
//                        Log.d(TGA, "mForceStopManager.canForceStop() is false");
//                    }
//                    Log.d(TGA, "out the onForceStopOk");
//                    finish();
//                }
//            });
//        }catch (Exception e){
//            e.printStackTrace();
//        }


    }

    private void init(){
        mPackageManager = getPackageManager();
        //mStorageManager = getSystemService(StorageManager.class);
        final Uri uri = getIntent().getData();
        if (uri == null) {
            Log.e(TGA, "No app to inspect (missing data uri in intent)");
            finish();
            return;
        }

        mPackageName = uri.getSchemeSpecificPart();
        mApplicationsState = ApplicationsState.getInstance(getApplication());
        //mSession = mApplicationsState.newSession(this);
        final int userId = UserHandle.myUserId();
        final ApplicationsState.AppEntry entry = mApplicationsState.getEntry(mPackageName, userId);
        //根据包名和userId来取出app信息
        if (entry == null) {
            Log.e(TGA, "Failed to load entry for package " + mPackageName);
            finish();
            return;
        }
        mAppInfo = new StopAppInfo(this, entry);

        mForceStopManager = new ForceStopManager(this, mAppInfo);

        Log.d(TGA, "appInfo: name =" + mAppInfo.getName()
                + " CacheSize =" + mAppInfo.getCacheSize()
                + " DateSize =" + mAppInfo.getDataSize()
                + " Size =" + mAppInfo.getSize()
                + " Version =" + mAppInfo.getVersion());


    }

    private void onForceStopOk() {
        mForceStopManager.forceStop(mApplicationsState);
        //onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sure:
                if (mForceStopManager.canForceStop()){
                    onForceStopOk();
                    Intent intent = new Intent(DetailActivity.this, StopRunningActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Log.d(TGA, "mForceStopManager.canForceStop() is false");
                }
                Log.d(TGA, "out the onForceStopOk");
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
            default:
                break;
        }
    }
}
