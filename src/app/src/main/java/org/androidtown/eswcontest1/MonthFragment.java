package org.androidtown.eswcontest1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class MonthFragment extends Fragment {
    TextView textView;
    //추가
    TextView button;
    LineChart lineChart;
    public MonthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month,null);

        lineChart = (LineChart) view.findViewById(R.id.lineChart);

        createChart();
        return  view;
    }

    void createChart(){
        List ChartData;
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 4f));
        entries.add(new Entry(1, 8f));
        entries.add(new Entry(2, 6f));
        entries.add(new Entry(3, 2f));
        entries.add(new Entry(4, 18f));
        entries.add(new Entry(5, 9f));
        entries.add(new Entry(6, 16f));
        entries.add(new Entry(7, 5f));
        entries.add(new Entry(8, 3f));
        entries.add(new Entry(9, 3f));
        entries.add(new Entry(10, 7f));
        entries.add(new Entry(11, 8f));
        entries.add(new Entry(12, 6f));
        entries.add(new Entry(13, 2f));
        entries.add(new Entry(14, 18f));
        entries.add(new Entry(15, 9f));
        entries.add(new Entry(16, 16f));
        entries.add(new Entry(17, 5f));
        entries.add(new Entry(18, 3f));
        entries.add(new Entry(19, 7f));
        entries.add(new Entry(20, 7f));
        entries.add(new Entry(21, 8f));
        entries.add(new Entry(22, 6f));
        entries.add(new Entry(23, 2f));
        entries.add(new Entry(24, 18f));
        entries.add(new Entry(25, 9f));
        entries.add(new Entry(26, 16f));
        entries.add(new Entry(27, 5f));
        entries.add(new Entry(28, 3f));
        entries.add(new Entry(29, 7f));
        entries.add(new Entry(30, 7f));
        entries.add(new Entry(31, 7f));

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("September");
        labels.add("October");
        labels.add("November");
        labels.add("December");

        // Create LineData with labels and dataset prepared previously
        LineData data = new LineData(dataset);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        /*dataset.setDrawCubic(true); //선 둥글게 만들기
        dataset.setDrawFilled(true); //그래프 밑부분 색칠*/

        lineChart.setData(data);
        lineChart.animateY(3000);


    }
}
