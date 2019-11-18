package com.space.robotcontroller.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.space.robotcontroller.R;

/**
 * Created by licht on 2019/9/5.
 */

public class ConfigActivity extends AppCompatActivity {

    private EditText mEdtAddress;
    private SharedPreferences mPreferences;
    private EditText mEdtPort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        mEdtAddress = findViewById(R.id.edt_address);
        mEdtPort = findViewById(R.id.edt_port);
        Button mBtnConnection = findViewById(R.id.btn_connection);
        mPreferences = getSharedPreferences("robot_controller_ip", MODE_PRIVATE);
        String ipAddress = mPreferences.getString("ip_address", "");
        int ipPort = mPreferences.getInt("ip_port", 2000);
        if (!TextUtils.isEmpty(ipAddress)) {
            mEdtAddress.setText(ipAddress);
            mEdtPort.setText(String.valueOf(ipPort));
        }
        mBtnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = mEdtAddress.getText().toString().trim();
                String port = mEdtPort.getText().toString().trim();
                if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)) {
                    return;
                }
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("ip_address", ip);
                editor.putInt("ip_port", Integer.parseInt(port));
                editor.apply();
                startActivity(new Intent(ConfigActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}
