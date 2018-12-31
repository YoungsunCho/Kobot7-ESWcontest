package org.androidtown.eswcontest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    TextView logOut;
    TextView sensorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        logOut = (TextView) findViewById(R.id.logOut);
        sensorButton = (TextView) findViewById(R.id.sensor);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent = new Intent(SettingActivity.this, LoginActivity.class);
                SettingActivity.this.startActivity(HomeIntent);

            }
        });
        sensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent = new Intent(SettingActivity.this, SensorActivity.class);
                SettingActivity.this.startActivity(HomeIntent);

            }
        });
    }
}
