package com.fengjw.tvhelper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.fengjw.tvhelper.recenttask.RecentTaskActivity;

/**
 * Created by fengjw on 2017/11/8.
 */

public class NewMainActivity extends Activity implements View.OnClickListener {
    
    private RelativeLayout mHomeUpdateRl;
    private RelativeLayout mHomeRecentRl;
    private RelativeLayout mHomeFileRl;
    private HomeFocusListener mFocusListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        initView();
    }

    private void initView() {
        mHomeUpdateRl = (RelativeLayout) findViewById(R.id.rl_home_update);
        mHomeRecentRl = (RelativeLayout) findViewById(R.id.rl_home_recent);
        mHomeFileRl = (RelativeLayout) findViewById(R.id.rl_home_file);

        mHomeUpdateRl.setOnClickListener(this);
        mHomeRecentRl.setOnClickListener(this);
        mHomeFileRl.setOnClickListener(this);

        mFocusListener = new HomeFocusListener();
        mHomeUpdateRl.setOnFocusChangeListener(mFocusListener);
        mHomeRecentRl.setOnFocusChangeListener(mFocusListener);
        mHomeFileRl.setOnFocusChangeListener(mFocusListener);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        String pkgName = null;
        switch (view.getId()){
            case R.id.rl_home_update:
                pkgName = "com.fengjw.apkupdatetool";
                String className = "com.fengjw.apkupdatetool.DownloadAllActivity";
                intent.setComponent(new ComponentName(pkgName, className));
                startActivity(intent);
                break;
            case R.id.rl_home_recent:
                intent.setClass(this, RecentTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_home_file:
                pkgName = "com.ktc.filemanager";
                startApp(pkgName);
                break;
            default:
                break;
        }

    }

    class HomeFocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean b) {
            if (b) {
                zoomOutWindow(view);
            } else {
                zoomInWindow(view);
            }
        }
    }

    private void zoomOutWindow(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animationSet.addAnimation(animation);
        animationSet.setFillAfter(true);
        view.clearAnimation();
        view.startAnimation(animationSet);
    }

    private void zoomInWindow(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation animation = new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animationSet.addAnimation(animation);
        animationSet.setFillAfter(true);
        view.startAnimation(animationSet);
    }


    public void startApp(String appPackageName) {
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
            //finish();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "文件管理未安装！", Toast.LENGTH_SHORT).show();
        }
    }

}
