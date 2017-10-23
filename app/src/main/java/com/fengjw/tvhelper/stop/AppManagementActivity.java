package com.fengjw.tvhelper.stop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fengjw.tvhelper.R;

public class AppManagementActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView mAppImage;
    private TextView m1AppTv;
    private TextView mNameApp;
    private TextView mVersionApp;
    private LinearLayout mAppLin;
    private TextView mOpenApp;
    private TextView mStopAppTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_app);
        initView();
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
                toast("open");
                break;
            case R.id.tv_stop_app:
                // TODO 17/10/23
                toast("stop");
                break;
            default:
                break;
        }
    }

    private void toast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}
