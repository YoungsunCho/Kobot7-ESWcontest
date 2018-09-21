package org.androidtown.eswcontest1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;


public class postureDayFragment extends Fragment {
    TextView dayCheck;
    TextView todaySTime;
    TextView avgSTime;
    //추가
    TextView button;
    PieChart pieChart;
    ImageView leftButton;
    ImageView rightButton;
    int month,day;
    public postureDayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posture_day,null);
        leftButton = (ImageView) view.findViewById(R.id.leftButton);
        rightButton = (ImageView) view.findViewById(R.id.rightButton);
        dayCheck = (TextView) view.findViewById(R.id.dayCheck);
        todaySTime = (TextView) view.findViewById(R.id.todaysleep);
        avgSTime = (TextView) view.findViewById(R.id.avgsleep);
        pieChart = (PieChart) view.findViewById(R.id.PieChart);
        day = 20;month = 9;
        dayCheck.setText(month+"월"+day+"일");

        leftButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (day - 1 < 1) {
                    if(month-1 <1){
                        month = 12;
                    }
                    else {
                        month--;
                    }
                    switch(month)
                    {
                        case 2:
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            day = 31;
                            break;
                        default:
                            day=30;
                            break;
                    }
                } else {
                    day--;
                }
                dayCheck.setText(month+"월"+day+"일");
                createChart();

            }
        });
        rightButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num;
                switch(month)
                {
                    case 2:
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        num = 30;
                        break;
                    default:
                        num=31;
                        break;
                }
                if (day + 1 > num) {
                    if(month+1 >12){
                        month = 1;
                    }
                    else {
                        month++;
                    }
                    day=1;
                } else {
                    day++;
                }
                dayCheck.setText(month+"월"+day+"일");
                createChart();

            }
        });


        createChart();

        return  view;
    }
    @SuppressLint("SetTextI18n")
    void createChart(){
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        Random randomnum= new Random();
        int time = randomnum.nextInt(5)+5;

        pieChart.setDragDecelerationFrictionCoef(0.95f);


        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.BLACK);


        pieChart.setTransparentCircleRadius(61f);


        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();


        yValues.add(new PieEntry(5,"left"));
        yValues.add(new PieEntry(3,"front"));
        yValues.add(new PieEntry(2,"right"));


        Description description = new Description();
        description.setText("수면시간"); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);


        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션


        PieDataSet dataSet = new PieDataSet(yValues,"(단위:시간)");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);


        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);

    }
}