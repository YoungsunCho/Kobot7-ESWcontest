package org.androidtown.eswcontest;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class ChartFragment extends android.app.Fragment {
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_chart,container,false);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.add(R.id.FragmentView2, new SleepingTimeFragment());
        fragmentTransaction.commit();
        Date date = new Date();
        ((MainActivity)getActivity()).month = date.getMonth();
        ((MainActivity)getActivity()).day = date.getDate();

        BottomBar bottomBar = (BottomBar) v.findViewById(R.id.topBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_sleepingTime) {
                    switchFragment(1);
                }
                if (tabId == R.id.tab_snore) {
                    switchFragment(2);
                }
                if (tabId == R.id.tab_posture) {
                    switchFragment(3);
                }
            }
        });

        return v;

    }

    public void switchFragment(int Num) {
        android.app.Fragment fr;

        switch (Num)
        {
            case 1:
                fr = new SleepingTimeFragment();
                break;
            case 2:
                fr = new SnoreFragment() ;
                break;
            case 3:
                fr = new PostureFragment();
                break;
            default:
                fr = new SleepingTimeFragment() ;
                break;
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentView2, fr);
        fragmentTransaction.commit();
    }


}
