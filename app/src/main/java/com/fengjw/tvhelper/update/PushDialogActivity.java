package com.fengjw.tvhelper.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.fengjw.tvhelper.R;

/**
 * Created by fengjw on 2017/9/11.
 */

public class PushDialogActivity extends AppCompatActivity  implements View.OnClickListener{
    private TextView title;
    private Button cancel;
    private Button sure;
    private String str = "您有新的应用需要更新\n请问是否进行更新？";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_push_dialog);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1.0f;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.TOP | Gravity.LEFT);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(str);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        sure = (Button)findViewById(R.id.sure);
        sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.sure:
                finish();
                Intent intent = new Intent(this,DownloadAllActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
