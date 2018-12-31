package org.androidtown.eswcontest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;


public class MainFragment extends Fragment {
    View v;
    int page;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.add(R.id.FragmentView, new HomeFragment());
        fragmentTransaction.commit();
        BottomBar bottomBar = (BottomBar) v.findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_call_home) {
                    switchFragment(1);
                }
                if (tabId == R.id.tab_call_chart) {
                    switchFragment(2);
                }
                if (tabId == R.id.tab_call_calendar) {
                    switchFragment(3);
                }
                if (tabId == R.id.tab_call_user) {
                    switchFragment(4);
                }
            }
        });

        return v;
    }
    public void switchFragment(int Num) {
        Fragment fr;

        switch (Num)
        {
            case 1:
                fr = new HomeFragment() ;
                break;
            case 2:
                fr = new ChartFragment() ;
                break;
            case 3:
                fr = new CalendarFragment();
                break;
            case 4:
                fr = new AlarmFragment() ;
                break;
            default:
                fr = new HomeFragment() ;
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentView, fr);
        fragmentTransaction.commit();
    }
}
