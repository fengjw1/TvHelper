package com.fengjw.tvhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fengjw.tvhelper.stop.StopRunningActivity;
import com.fengjw.tvhelper.update.DownloadAllActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_update;
    private Button btn_stoprunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
