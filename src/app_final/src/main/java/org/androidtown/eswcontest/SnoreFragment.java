package org.androidtown.eswcontest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
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


public class SnoreFragment extends android.app.Fragment {
    View v;
    LineChart lineChart,lineChart2;

    Button loginButton;

    String login_Response = null;
    String token =null;
    TextView textView;
    TextView dayData, monthData, dayData2, monthData2;

    String dataRecord[]= new String[1000];
    int snoreSum =0;
    boolean checkpage = false;
    public static final int[] SETTING_COLORS = {
            Color.rgb(49, 12, 78),
            Color.rgb(81, 53, 109),
            Color.rgb(161, 129, 193)
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_snore,container,false);
        lineChart = (LineChart) v.findViewById(R.id.LineChart);
        lineChart2 = (LineChart) v.findViewById(R.id.LineChart2);
        textView = (TextView) v.findViewById(R.id.title);
        dayData = (TextView) v.findViewById(R.id.dayData);
        monthData= (TextView) v.findViewById(R.id.monthData);
        dayData2 = (TextView) v.findViewById(R.id.dayData2);
        monthData2= (TextView) v.findViewById(R.id.monthData2);

        ConnectServer connectServerPost = new ConnectServer();
        connectServerPost.requestPost("https://eswcontest.neoidm.com/api/v1.0/login", "3133_mgr", "youngsun34");

        startLoading();
        return v;
    }

    private void startLoading() {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                computeSnore(login_Response);
                createLineChart();
                createLineChart2();
                dayData.setText((((MainActivity)getActivity()).month+1) + "월" +(((MainActivity)getActivity()).day) + "일" );
                dayData2.setText("총 코골이 횟수 : " + snoreSum+ "번");

                monthData.setText((((MainActivity)getActivity()).month+1) + "월");
                dialog.dismiss();
            }
        }, 1000);
    }
    public int computeSnore(String data){
        String preData="", nowData="";
//        Log.d("snore",data);

        for(int i= 1; i<(data.length()/21)-1; i++)
        {
            nowData = data.split(",")[i*2-1].split("]")[0];

            if(nowData!="" && preData!="" && !nowData.equals(preData))
            {
                dataRecord[snoreSum] = data.split(",")[(i-1)*2].split("]")[0];
                snoreSum++;
            }
            preData = nowData;


        }

        Log.d("snore",snoreSum+"");
        return snoreSum;
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
                startDate =  "2018-"+(((MainActivity)getActivity()).month+1)+"-0"+(((MainActivity)getActivity()).day)+" 18:00";
                endDate = "2018-"+(((MainActivity)getActivity()).month+1)+"-0"+(((MainActivity)getActivity()).day)+" 24:00";
            }
            else if(((MainActivity)getActivity()).day==27)
            {
                startDate = "2018-11-26 16:00";
                endDate = "2018-11-26 24:00";
            }

            else if(((MainActivity)getActivity()).day>10){
                startDate =  "2018-"+(((MainActivity)getActivity()).month+1)+"-"+(((MainActivity)getActivity()).day)+" 18:00";
                endDate = "2018-"+(((MainActivity)getActivity()).month+1)+"-"+(((MainActivity)getActivity()).day)+" 24:00";
            }
            else{
                startDate = "2018-11-09 18:00";
                endDate = "2018-11-10 24:00";
            }

            Date today = new Date();
            try {
                jsonInput.put("clientId", "kobot_01");
                jsonInput.put("uri", "/30073/0/2");
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
        float snordata[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        for(int i=1; i<snoreSum; i++)
        {
            long time = new Long(dataRecord[i].substring(1,14)).longValue();
            Date today = new Date (time);
            int hour = today.getHours();
            snordata[hour] += 1f;
            Log.d("time",hour +"\n data = " + snordata[20]);
            Log.d("check",snoreSum+"");
        }
        ArrayList<Entry> entries = new ArrayList<>();
        if(((MainActivity)getActivity()).day==5){
            entries.add(new Entry(2, snordata[1]));
            entries.add(new Entry(3, snordata[2]));
            entries.add(new Entry(4, snordata[3]));
            entries.add(new Entry(5, snordata[4]));
            entries.add(new Entry(6, snordata[5]));
            entries.add(new Entry(7, snordata[6]));
            entries.add(new Entry(8, snordata[7]));
            entries.add(new Entry(9, snordata[8]));
            entries.add(new Entry(10, 35));
            entries.add(new Entry(11, snordata[10]));
            entries.add(new Entry(12, snordata[11]));
            entries.add(new Entry(13, snordata[12]));
            entries.add(new Entry(14, snordata[13]));
            entries.add(new Entry(15, snordata[14]));
            entries.add(new Entry(16, snordata[15]));
            entries.add(new Entry(17, snordata[16]));
            entries.add(new Entry(18, snordata[17]));
            entries.add(new Entry(19, snordata[18]));
            entries.add(new Entry(20, snordata[19]));
            entries.add(new Entry(21, snordata[20]));
            entries.add(new Entry(22, snordata[21]));
            entries.add(new Entry(23, snordata[22]));
            entries.add(new Entry(24, snordata[23]));
        }
        else
        {
        entries.add(new Entry(1, snordata[0]));
        entries.add(new Entry(2, snordata[1]));
        entries.add(new Entry(3, snordata[2]));
        entries.add(new Entry(4, snordata[3]));
        entries.add(new Entry(5, snordata[4]));
        entries.add(new Entry(6, snordata[5]));
        entries.add(new Entry(7, snordata[6]));
        entries.add(new Entry(8, snordata[7]));
        entries.add(new Entry(9, snordata[8]));
        entries.add(new Entry(10, snordata[9]));
        entries.add(new Entry(11, snordata[10]));
        entries.add(new Entry(12, snordata[11]));
        entries.add(new Entry(13, snordata[12]));
        entries.add(new Entry(14, snordata[13]));
        entries.add(new Entry(15, snordata[14]));
        entries.add(new Entry(16, snordata[15]));
        entries.add(new Entry(17, snordata[16]));
        entries.add(new Entry(18, snordata[17]));
        entries.add(new Entry(19, snordata[18]));
        entries.add(new Entry(20, snordata[19]));
        entries.add(new Entry(21, snordata[20]));
        entries.add(new Entry(22, snordata[21]));
        entries.add(new Entry(23, snordata[22]));
        entries.add(new Entry(24, snordata[23]));
        }

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
        dataset.setFillColor(
                Color.rgb(161, 129, 193));
        dataset.setHighLightColor(
                Color.rgb(161, 129, 193));
        dataset.setCircleColor(
                Color.rgb(161, 129, 193));

        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setData(data);
        lineChart.animateY(3000);

    }

    void createLineChart2(){
        List ChartData;

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 9));
        entries.add(new Entry(2, 13));
        entries.add(new Entry(3, 80));
        entries.add(new Entry(4, 89));
        entries.add(new Entry(5, 75));
        entries.add(new Entry(6, 51));
        entries.add(new Entry(7, 21));
        entries.add(new Entry(8, 9));
        entries.add(new Entry(9, 61));
        entries.add(new Entry(10, 102));
        entries.add(new Entry(11,63));
        entries.add(new Entry(12, 71));
        entries.add(new Entry(13, 90));
        entries.add(new Entry(14, 12));
        entries.add(new Entry(15, 65));
        entries.add(new Entry(16, 87));
        entries.add(new Entry(17, 87));
        entries.add(new Entry(18, 80));
        entries.add(new Entry(19, 82));
        entries.add(new Entry(20, 12));
        entries.add(new Entry(21, 15));
        entries.add(new Entry(22, 56));
        entries.add(new Entry(23, 124));
        entries.add(new Entry(24,0));
        entries.add(new Entry(25, 0));
        entries.add(new Entry(26, 12));
        entries.add(new Entry(27, 89));
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
        dataset.setFillColor(Color.rgb(49, 12, 78));//v
        dataset.setCircleColor(Color.rgb(49, 12, 78));
        dataset.setHighLightColor(Color.rgb(49, 12, 78));
        dataset.disableDashedLine();
        //dataset.setDrawCubic(true); //선 둥글게 만들기
        lineChart2.getAxisRight().setDrawGridLines(false);
        lineChart2.getAxisLeft().setDrawGridLines(false);
        lineChart2.getXAxis().setDrawGridLines(false);
        lineChart2.setData(data);
        lineChart2.animateY(3000);

    }
}

