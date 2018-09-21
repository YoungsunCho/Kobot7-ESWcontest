package org.androidtown.eswcontest1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeekFragment extends Fragment {
    TextView dayCheck;
    TextView todaySTime;
    TextView avgSTime;
    //추가
    TextView button;
    ImageView leftButton;
    ImageView rightButton;
    int month,week;
    TextView textView;
    //추가
    LineChart lineChart;


    public WeekFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week,null);

        lineChart = (LineChart) view.findViewById(R.id.lineChart);
        leftButton = (ImageView) view.findViewById(R.id.leftButton);
        rightButton = (ImageView) view.findViewById(R.id.rightButton);
        dayCheck = (TextView) view.findViewById(R.id.dayCheck);
        todaySTime = (TextView) view.findViewById(R.id.todaysleep);
        avgSTime = (TextView) view.findViewById(R.id.avgsleep);
        week = 3;month = 9;
        dayCheck.setText(month+"월"+week+"째주");

        leftButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (week - 1 < 1) {
                    if(month-1 <1){
                        month = 12;
                    }
                    else {
                        month--;
                    }
                    week=4;
                } else {
                    week--;
                }
                dayCheck.setText(month+"월"+week+"째주");
                createChart();

            }
        });
        rightButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (week + 1 > 4) {
                    if(month+1 >12){
                        month = 1;
                    }
                    else {
                        month++;
                    }
                    week=1;
                } else {
                    week++;
                }
                dayCheck.setText(month+"월"+week+"째주");
                createChart();

            }
        });
        createChart();
        return  view;
    }

    void createChart(){
        Random random = new Random();

        List ChartData;
        ArrayList<Entry> entries = new ArrayList<>();

        int randomNum,sum=0;
        randomNum = random.nextInt(5)+5;
        entries.add(new Entry(0, randomNum));
        sum+=randomNum;
        randomNum = random.nextInt(5)+5;
        entries.add(new Entry(1, randomNum));
        sum+=randomNum;
        randomNum = random.nextInt(5)+5;
        entries.add(new Entry(2, randomNum));
        sum+=randomNum;
        randomNum = random.nextInt(5)+5;
        entries.add(new Entry(3, randomNum));
        sum+=randomNum;
        randomNum = random.nextInt(5)+5;
        entries.add(new Entry(4, randomNum));
        sum+=randomNum;
        randomNum = random.nextInt(5)+5;
        entries.add(new Entry(5, randomNum));
        sum+=randomNum;
        randomNum = random.nextInt(5)+5;
        entries.add(new Entry(6, randomNum));
        sum+=randomNum;

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("월");
        labels.add("화");
        labels.add("수");
        labels.add("목");
        labels.add("금");
        labels.add("토");
        labels.add("일");

        // Create LineData with labels and dataset prepared previously
        LineData data = new LineData(dataset);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
//        dataset.setDrawCubic(true); //선 둥글게 만들기
        dataset.setDrawFilled(true); //그래프 밑부분 색칠*/

        lineChart.setData(data);
        lineChart.animateY(500);

        todaySTime.setText(sum+"h");
        avgSTime.setText((48-sum)+"h");


    }
}