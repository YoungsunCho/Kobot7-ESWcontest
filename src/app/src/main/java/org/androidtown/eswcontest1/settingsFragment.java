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


public class settingsFragment extends Fragment {
    //추가

    TextView userInfoButton;
    TextView userInfoText;
    boolean OnUserinfo;
    public settingsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public settingsFragment(int num) {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,null);

        return  view;
    }

}
