package org.androidtown.eswcontest1;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class snoreFragment extends Fragment {
    //추가
    TextView dayButton;
    TextView weekButton;
    TextView monthButton;
    int pageNum = 1;

    public snoreFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public snoreFragment(int num) {
        pageNum = num;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snore,null);
        dayButton = (TextView)view.findViewById(R.id.dayButton);
        weekButton = (TextView) view.findViewById(R.id.weekButton) ;
        monthButton = (TextView) view.findViewById(R.id.monthButton) ;

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch(pageNum){
            case 1:
                fragmentTransaction.add(R.id.FragmentView, new snoreDayFragment());
                break;
            case 2:
                fragmentTransaction.add(R.id.FragmentView, new snoreWeekFragment());
                break;
            case 3:
                fragmentTransaction.add(R.id.FragmentView, new snoreMonthFragment());
                break;
            default:
                fragmentTransaction.add(R.id.FragmentView, new snoreDayFragment());
                break;
        }
        fragmentTransaction.commit();

        switch(pageNum){
            case 1:
                dayButton.setTextSize(18);
                weekButton.setTextSize(15);
                monthButton.setTextSize(15);
                break;
            case 2:
                weekButton.setTextSize(18);
                dayButton.setTextSize(15);
                monthButton.setTextSize(15);
                break;
            case 3:
                weekButton.setTextSize(15);
                dayButton.setTextSize(15);
                monthButton.setTextSize(18);
                break;
            default:
                weekButton.setTextSize(15);
                dayButton.setTextSize(15);
                dayButton.setTextSize(18);
                break;
        }
        dayButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum = 1;
                switchFragment(1);
            }
        });
        weekButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum = 2;
                switchFragment(2) ;
            }
        });
        monthButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum = 3;
                switchFragment(3) ;
            }
        });

        return  view;
    }

    public void switchFragment(int Num) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentView, new snoreFragment(Num));
        fragmentTransaction.commit();
    }
}
