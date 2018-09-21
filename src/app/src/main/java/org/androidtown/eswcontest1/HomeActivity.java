package org.androidtown.eswcontest1;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {

    TextView UnderBarSleeping;
    TextView UnderBarPosture;
    TextView UnderBarSnore;
    TextView UnderBarSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.FragmentView, new sleepFragment());
        fragmentTransaction.commit();

        ImageView buttonSleeping = (ImageView) findViewById(R.id.sleeping_img) ;
        UnderBarSleeping = (TextView) findViewById(R.id.sleeping_underbar) ;
        buttonSleeping.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(1) ;
            }
        });

        ImageView buttonPosture = (ImageView) findViewById(R.id.posture_img) ;
        UnderBarPosture = (TextView) findViewById(R.id.posture_underbar) ;
        buttonPosture.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(2) ;
            }
        });

        ImageView buttonSnore = (ImageView) findViewById(R.id.snore_img) ;
        UnderBarSnore = (TextView) findViewById(R.id.snore_underbar) ;
        buttonSnore.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(3) ;
            }
        });
        ImageView buttonSeting = (ImageView) findViewById(R.id.setting_img) ;
        UnderBarSetting = (TextView) findViewById(R.id.setting_underbar) ;
        buttonSeting.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(4) ;
            }
        });

    }
    public void switchFragment(int Num) {
        Fragment fr;

        switch (Num)
        {
            case 1:
                fr = new sleepFragment() ;
                UnderBarSleeping.setBackgroundColor(Color.parseColor("#FFFFFF"));
                UnderBarPosture.setBackgroundColor(Color.parseColor("#00000000"));
                UnderBarSnore.setBackgroundColor(Color.parseColor("#00000000"));
                UnderBarSetting.setBackgroundColor(Color.parseColor("#00000000"));

                break;
            case 2:
                fr = new postureFragment() ;
                UnderBarSleeping.setBackgroundColor(Color.parseColor("#00000000"));
                UnderBarPosture.setBackgroundColor(Color.parseColor("#FFFFFF"));
                UnderBarSnore.setBackgroundColor(Color.parseColor("#00000000"));
                UnderBarSetting.setBackgroundColor(Color.parseColor("#00000000"));

                break;
            case 3:
                fr = new snoreFragment();
                UnderBarSleeping.setBackgroundColor(Color.parseColor("#00000000"));
                UnderBarPosture.setBackgroundColor(Color.parseColor("#00000000"));
                UnderBarSnore.setBackgroundColor(Color.parseColor("#FFFFFF"));
                UnderBarSetting.setBackgroundColor(Color.parseColor("#00000000"));
                break;
            case 4:
                fr = new settingsFragment() ;
                UnderBarSleeping.setBackgroundColor(Color.parseColor("#00000000"));
                UnderBarPosture.setBackgroundColor(Color.parseColor("#00000000"));
                UnderBarSnore.setBackgroundColor(Color.parseColor("#00000000"));
                UnderBarSetting.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            default:
                fr = new sleepFragment() ;
                UnderBarSleeping.setBackgroundColor(Color.parseColor("#FFFFFF"));
                UnderBarPosture.setBackgroundColor(Color.parseColor("00000000"));
                UnderBarSnore.setBackgroundColor(Color.parseColor("#00000000"));
                UnderBarSetting.setBackgroundColor(Color.parseColor("#00000000"));


        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentView, fr);
        fragmentTransaction.commit();
    }

}

