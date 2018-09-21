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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class snoreDayFragment extends Fragment {
    TextView dayCheck;
    TextView todaySTime;
    TextView avgSTime;
    //추가
    TextView button;
    LineChart lineChart;
    ImageView leftButton;
    ImageView rightButton;
    int month,day;
    public snoreDayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snore_day,null);
        leftButton = (ImageView) view.findViewById(R.id.leftButton);
        rightButton = (ImageView) view.findViewById(R.id.rightButton);
        dayCheck = (TextView) view.findViewById(R.id.dayCheck);
        todaySTime = (TextView) view.findViewById(R.id.todaysleep);
        avgSTime = (TextView) view.findViewById(R.id.avgsleep);
        lineChart = (LineChart) view.findViewById(R.id.lineChart);
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
    }  void createChart(){
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
        entries.add(new Entry(6, randomNum));
        sum+=randomNum;
        entries.add(new Entry(6, randomNum));
        sum+=randomNum;
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

        dataset.setColors(ColorTemplate.VORDIPLOM_COLORS); //
   //     dataset.setDrawCubic(true); //선 둥글게 만들기
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setDrawFilled(true); //그래프 밑부분 색칠*/

        lineChart.setData(data);
        lineChart.animateY(500);
    }
}