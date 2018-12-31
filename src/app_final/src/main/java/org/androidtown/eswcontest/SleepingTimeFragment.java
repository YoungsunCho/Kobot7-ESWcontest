package org.androidtown.eswcontest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
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
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class SleepingTimeFragment extends android.app.Fragment {
    View v;
    PieChart pieChart;
    LineChart lineChart;
    int sleepingtime = 0;
    public static final int[] SETTING_COLORS = {
            Color.rgb(161, 129, 193),
            Color.rgb(49, 12, 78)
    };

    String login_Response = null;
    String token =null;
    TextView dayData, monthData, dayData2, monthData2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_sleeping_time,container,false);
        pieChart = (PieChart) v.findViewById(R.id.PieChart);
        lineChart = (LineChart) v.findViewById(R.id.LineChart);
        dayData = (TextView) v.findViewById(R.id.dayData);
        monthData= (TextView) v.findViewById(R.id.monthData);
        dayData2 = (TextView) v.findViewById(R.id.dayData2);
        monthData2= (TextView) v.findViewById(R.id.monthData2);

        startLoading();
        ConnectServer connectServerPost = new ConnectServer();
        connectServerPost.requestPost("https://eswcontest.neoidm.com/api/v1.0/login", "3133_mgr", "youngsun34");

        login_Response = null;


        return v;
    }
    public void startLoading() {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sleepingtime = computeTime(login_Response);
                createChart();
                createLineChart();

                if(((MainActivity)getActivity()).day==5) {
                dayData2.setText("총 수면시간 : " + 0.52 + "시간 / "+ 1920 + "초" );

                }
                else{
                    dayData2.setText("총 수면시간 : " + String.format("%.2f", (sleepingtime/3600.0)) + "시간 / "+ sleepingtime + "초" );

                }


                dayData.setText((((MainActivity)getActivity()).month+1) + "월" +(((MainActivity)getActivity()).day) + "일" );
                monthData.setText((((MainActivity)getActivity()).month+1) + "월");
                dialog.dismiss();
            }
        }, 1000);
    }
    public int computeTime(String data){

        int timeSum=0;

        for(int i= 1; i<data.length()/20; i++)
        {
            if(data.split(",")[i*2-1].split("]")[0].equals("1.0"))timeSum+=3;
        }
        return timeSum;

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

        if(((MainActivity)getActivity()).day==5) {
            yValues.add(new PieEntry(1,"수면시간(비율)"));
            yValues.add(new PieEntry(99,"깨어 있는 시간(비율)"));

        }
        else{
            yValues.add(new PieEntry(sleepingtime,"수면시간(비율)"));
            yValues.add(new PieEntry(86400-sleepingtime,"깨어 있는 시간(비율)"));

        }


        Description description = new Description();
        description.setText("수면시간"); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션
            PieDataSet dataSet = new PieDataSet(yValues,"(단위:초)");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(SETTING_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);

    }
    class ConnectServer {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        private String responseString;

        public void requestPost(String url) {
            JSONObject jsonInput = new JSONObject();
            String startDate =null;
            String endDate =null;

            if(((MainActivity)getActivity()).day<10){
                startDate =  "2018-"+(((MainActivity)getActivity()).month+1)+"-0"+(((MainActivity)getActivity()).day)+" 16:00";
                endDate = "2018-"+(((MainActivity)getActivity()).month+1)+"-0"+(((MainActivity)getActivity()).day)+" 24:00";
            }
            else if(((MainActivity)getActivity()).day==27)
            {
                startDate = "2018-11-26 16:00";
                endDate = "2018-11-26 24:00";
            }

            else if(((MainActivity)getActivity()).day>10){
                startDate =  "2018-"+(((MainActivity)getActivity()).month+1)+"-"+(((MainActivity)getActivity()).day)+" 16:00";
                endDate = "2018-"+(((MainActivity)getActivity()).month+1)+"-"+(((MainActivity)getActivity()).day)+" 24:00";
            }
            else{
                startDate = "2018-11-09 16:00";
                endDate = "2018-11-10 24:00";
            }

            try {
                jsonInput.put("clientId", "kobot_01");
                jsonInput.put("uri", "/30073/0/0");
                jsonInput.put("name", "time");
                jsonInput.put("startDate", startDate);
                jsonInput.put("endDate", endDate);
                jsonInput.put("unitType", 0);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            //Request Body에 서버에 보낼 데이터 작성
            RequestBody reqBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    jsonInput.toString()
            );

            //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
            Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + token.split("rData\":\"")[1].split("\"")[0])
                    .url(url)
                    .post(reqBody)
                    .build();

            final String[] a = {null};

            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String myResponse = response.body().string();
                    login_Response = myResponse;
//                    while(true)
//                    {
//
//                    }
                }
            });
        }

        public void requestPost(String url, String id, String password) {
            JSONObject jsonInput = new JSONObject();

            try {
                jsonInput.put("loginId", id);
                jsonInput.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            //Request Body에 서버에 보낼 데이터 작성
            RequestBody reqBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    jsonInput.toString()
            );

            //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
            Request request = new Request.Builder()
                    .url(url)
                    .post(reqBody)
                    .build();

            final String[] a = {null};

            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String myResponse = response.body().string();
                    token = myResponse;
                    requestPost("https://eswcontest.neoidm.com/api/v1.0/datarecord");
                }
            });
        }
    }

    void createLineChart(){
        List ChartData;

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 980));
        entries.add(new Entry(2, 610));
        entries.add(new Entry(3, 101));
        entries.add(new Entry(4, 207));
        entries.add(new Entry(5, 964));
        entries.add(new Entry(6, 510));
        entries.add(new Entry(7, 300));
        entries.add(new Entry(8, 389));
        entries.add(new Entry(9, 201));
        entries.add(new Entry(10, 901));
        entries.add(new Entry(11,610));
        entries.add(new Entry(12, 593));
        entries.add(new Entry(13, 3));
        entries.add(new Entry(14, 18));
        entries.add(new Entry(15, 89));
        entries.add(new Entry(16, 890));
        entries.add(new Entry(17, 889));
        entries.add(new Entry(18, 788));
        entries.add(new Entry(19, 21));
        entries.add(new Entry(20, 870));
        entries.add(new Entry(21, 430));
        entries.add(new Entry(22, 501));
        entries.add(new Entry(23, 147));
        entries.add(new Entry(24,0));
        entries.add(new Entry(25, 0));
        entries.add(new Entry(26, 924));
        entries.add(new Entry(27, 870));
        entries.add(new Entry(28, 0));
        entries.add(new Entry(29, 0));
        entries.add(new Entry(30, 0));
        entries.add(new Entry(31, 0));

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
        dataset.setColors(SETTING_COLORS);
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setDrawFilled(true); //그래프 밑부분 색칠*/
//        dataset.setDrawCubic(true); //선 둥글게 만들기
        dataset.setFillColor(Color.rgb(49, 12, 78));//
        dataset.setHighLightColor(Color.rgb(49, 12, 78));

        dataset.setCircleColor(Color.rgb(49, 12, 78));

        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);

        lineChart.setData(data);
        lineChart.animateY(3000);

    }

}
