package com.fengjw.tvhelper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.fengjw.tvhelper.recenttask.RecentTaskActivity;
import com.fengjw.tvhelper.recenttask.utils.AppInfo;
import com.fengjw.tvhelper.recenttask.utils.AppInfoProvider;
import com.fengjw.tvhelper.recenttask.utils.ScrollTextView;

import java.util.List;

/**
 * Created by fengjw on 2017/11/8.
 */

public class NewMainActivity extends Activity implements View.OnClickListener {

    private RelativeLayout mHomeUpdateRl;
    private RelativeLayout mHomeRecentRl;
    private RelativeLayout mHomeFileRl;
    private HomeFocusListener mFocusListenerUpdate;
    private HomeFocusListener mFocusListenerRecent;
    private HomeFocusListener mFocusListenerFile;
    private HomeFocusListener mFocusListenerMemory;
    private RelativeLayout mHomeMemoryRl;
    private ScrollTextView mHomeUpdateTv;
    private ScrollTextView mHomeRecentTv;
    private ScrollTextView mHomeFileTv;
    private ScrollTextView mHomeMemoryTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d("fengjw", "old NewMainActivity");
        initView();
    }

    private void initView() {
        mHomeUpdateRl = (RelativeLayout) findViewById(R.id.rl_home_update);
        mHomeRecentRl = (RelativeLayout) findViewById(R.id.rl_home_recent);
        mHomeFileRl = (RelativeLayout) findViewById(R.id.rl_home_file);
        mHomeMemoryRl = (RelativeLayout) findViewById(R.id.rl_home_memory);
        mHomeUpdateTv = (ScrollTextView) findViewById(R.id.tv_home_update);
        mHomeRecentTv = (ScrollTextView) findViewById(R.id.tv_home_recent);
        mHomeFileTv = (ScrollTextView) findViewById(R.id.tv_home_file);
        mHomeMemoryTv = (ScrollTextView) findViewById(R.id.tv_home_memory);

        showItem();

        String tvUpdateName = mHomeUpdateTv.getText().toString();
        String tvRecentName = mHomeRecentTv.getText().toString();
        String tvFileName = mHomeFileTv.getText().toString();
        String tvMemoryName = mHomeMemoryTv.getText().toString();

        mFocusListenerUpdate = new HomeFocusListener(mHomeUpdateTv, tvUpdateName);
        mFocusListenerRecent = new HomeFocusListener(mHomeRecentTv, tvRecentName);
        mFocusListenerFile = new HomeFocusListener(mHomeFileTv, tvFileName);
        mFocusListenerMemory = new HomeFocusListener(mHomeMemoryTv, tvMemoryName);

        mHomeUpdateRl.setOnClickListener(this);
        mHomeRecentRl.setOnClickListener(this);
        mHomeFileRl.setOnClickListener(this);
        mHomeMemoryRl.setOnClickListener(this);
        mHomeUpdateRl.setOnFocusChangeListener(mFocusListenerUpdate);
        mHomeRecentRl.setOnFocusChangeListener(mFocusListenerRecent);
        mHomeFileRl.setOnFocusChangeListener(mFocusListenerFile);
        mHomeMemoryRl.setOnFocusChangeListener(mFocusListenerMemory);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        String pkgName = null;
        String className = null;
        switch (view.getId()) {
            case R.id.rl_home_update:
                //mHomeUpdateTv.requestFocus();
                pkgName = "com.fengjw.apkupdatetool";
                className = "com.fengjw.apkupdatetool.DownloadAllActivity";
                intent.setComponent(new ComponentName(pkgName, className));
                startActivity(intent);
                break;
            case R.id.rl_home_recent:
                intent.setClass(this, RecentTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_home_file:
                pkgName = "com.ktc.filemanager";
                className = "com.ktc.filemanager.activity.FirstPageActivity";
                //startApp(pkgName);
                intent.setComponent(new ComponentName(pkgName, className));
                startActivity(intent);
                break;
            case R.id.rl_home_memory:
                pkgName = "com.ktc.systemmanager";
                className = "com.ktc.systemmanager.HomeDialogActivity";
                //startApp(pkgName);
                intent.setComponent(new ComponentName(pkgName, className));
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    class HomeFocusListener implements View.OnFocusChangeListener {

        private ScrollTextView mTextView;
        private String mName;

        public HomeFocusListener(ScrollTextView view, String name) {
            mTextView = view;
            mName = name;
        }

        @Override
        public void onFocusChange(View view, boolean b) {
            if (b) {
                mTextView.setText(mName);
                mTextView.setCanFocused(true);
                zoomOutWindow(view);
            } else {
                mTextView.setText(mName);
                mTextView.setCanFocused(false);
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

    private void showItem() {
        int RESULT_UPDATE = 0;
        int RESULT_FILE = 0;
        int RESULT_MEMORY = 0;
        AppInfoProvider appInfoProvider = new AppInfoProvider(this);
        List<AppInfo> appInfoList = appInfoProvider.getAllApps();
        for (AppInfo appInfo : appInfoList) {
            if (appInfo.getPkg_name().equals("com.ktc.filemanager")) {
                RESULT_FILE = 1;
                break;
            }
        }

        for (AppInfo appInfo : appInfoList) {
            if (appInfo.getPkg_name().equals("com.fengjw.apkupdatetool")) {
                RESULT_UPDATE = 1;
                break;
            }
        }

        for (AppInfo appInfo : appInfoList) {
            if (appInfo.getPkg_name().equals("com.ktc.systemmanager")) {
                RESULT_MEMORY = 1;
                break;
            }
        }

        if (RESULT_UPDATE == 0) {
            mHomeUpdateRl.setVisibility(View.GONE);
        }
        if (RESULT_FILE == 0) {
            mHomeFileRl.setVisibility(View.GONE);
        }
        if (RESULT_MEMORY == 0) {
            mHomeMemoryRl.setVisibility(View.GONE);
        }
    }

}
