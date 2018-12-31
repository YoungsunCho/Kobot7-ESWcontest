package org.androidtown.eswcontest;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;


public class CalendarFragment extends android.app.Fragment {
    View v;
    String body;
    RadioButton radioButton1,radioButton2, radioButton3;

    int today = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_calendar,container,false);
        final TextView textview = (TextView) v.findViewById(R.id.TextView);
        radioButton1 = (RadioButton) v.findViewById(R.id.radioButton1);
        radioButton2= (RadioButton) v.findViewById(R.id.radioButton2);
        radioButton3= (RadioButton) v.findViewById(R.id.radioButton3);


        final CalendarView simpleCalendarView = (CalendarView) v.findViewById(R.id.calendarView); // get the reference of CalendarView
        simpleCalendarView.setDate(System.currentTimeMillis());        Date date = new Date();
        ((MainActivity)getActivity()).month = date.getMonth();
        ((MainActivity)getActivity()).day = date.getDate();
        radioButton1.setChecked(true); ;

        textview.setText( "2018년 "+ (((MainActivity)getActivity()).month+1) + "월 " + ((MainActivity)getActivity()).day+"일");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.add(R.id.FragmentView3, new SleepingTimeFragment());
        fragmentTransaction.commit();

        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                ((MainActivity)getActivity()).month = month;
                ((MainActivity)getActivity()).day = dayOfMonth;
                switchFragment(1);
                textview.setText( "2018년 "+ (((MainActivity)getActivity()).month+1) + "월 " + ((MainActivity)getActivity()).day+"일");
                radioButton1.setChecked(true); ;
            }
        });


        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(1);
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(2);
            }
        });
        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(3);
            }
        });
        return v;

    }

    public void switchFragment(int Num) {
        android.app.Fragment fr;

        switch (Num) {
            case 1:
                fr = new SleepingTimeFragment();
                break;
            case 2:
                fr = new SnoreFragment();
                break;
            case 3:
                fr = new PostureFragment();
                break;
            default:
                fr = new SleepingTimeFragment();
                break;
        }
        FragmentManager fm = getFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentView3, fr);
        fragmentTransaction.commit();

    }
}
