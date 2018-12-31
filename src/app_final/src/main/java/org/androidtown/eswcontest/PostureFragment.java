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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PostureFragment extends android.app.Fragment {
    View v;
    PieChart pieChart,pieChart2;
    String login_Response = null;
    String token =null;
    TextView textView;
    int postureSum[] = {0,0,0};

    TextView dayData, monthData, dayData2, monthData2;

    public static final int[] SETTING_COLORS = {
            Color.rgb(49, 12, 78),
            Color.rgb(81, 53, 109),
            Color.rgb(161, 129, 193)
    };

    public static final int[] SETTING_COLORS2 = {
            Color.rgb(211, 169, 233),
            Color.rgb(121, 93, 149),
            Color.rgb(99, 52, 128)
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_posture,container,false);
        pieChart = (PieChart) v.findViewById(R.id.PieChart);
        pieChart2 = (PieChart) v.findViewById(R.id.PieChart2);
        dayData = (TextView) v.findViewById(R.id.dayData);
        monthData= (TextView) v.findViewById(R.id.monthData);
        dayData2 = (TextView) v.findViewById(R.id.dayData2);
        monthData2= (TextView) v.findViewById(R.id.monthData2);


        ConnectServer connectServerPost = new ConnectServer();
        connectServerPost.requestPost("https://eswcontest.neoidm.com/api/v1.0/login", "3133_mgr", "youngsun34");

        startLoading();
        return v;
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
            yValues.add(new PieEntry(32,"left"));
            yValues.add(new PieEntry(82,"front"));
            yValues.add(new PieEntry(49,"right"));
        }
        else{
            yValues.add(new PieEntry(postureSum[0],"left"));
            yValues.add(new PieEntry(postureSum[1],"front"));
            yValues.add(new PieEntry(postureSum[2],"right"));
        }


        Description description = new Description();
        description.setText("수면시간"); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"(단위:시간)");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(SETTING_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);

    }
    void createChart2(){
        pieChart2.setUsePercentValues(true);
        pieChart2.getDescription().setEnabled(false);
        pieChart2.setExtraOffsets(5,10,5,5);

        Random randomnum= new Random();
        int time = randomnum.nextInt(5)+5;

        pieChart2.setDragDecelerationFrictionCoef(0.95f);


        pieChart2.setDrawHoleEnabled(false);
        pieChart2.setHoleColor(Color.BLACK);


        pieChart2.setTransparentCircleRadius(61f);


        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();


        yValues.add(new PieEntry(90,"left"));
        yValues.add(new PieEntry(89,"front"));
        yValues.add(new PieEntry(60,"right"));

        Description description = new Description();
        description.setText("수면시간"); //라벨
        description.setTextSize(15);
        pieChart2.setDescription(description);

        pieChart2.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"(단위:시간)");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(SETTING_COLORS2);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        pieChart2.setData(data);

    }

    private void startLoading() {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                computeTime(login_Response);
                createChart();
                createChart2();
                dayData.setText((((MainActivity)getActivity()).month+1) + "월" +(((MainActivity)getActivity()).day) + "일 자세" );
                monthData.setText((((MainActivity)getActivity()).month+1) + "월 자세 평균");
                dialog.dismiss();
            }
        }, 1000);
    }
    public int computeTime(String data){

        int timeSum=0;
        String data2 = null;
        Log.d("error", data);
        for(int i= 1; i<data.length()/21; i++)
        {
            data2 = data.split(",")[i*2-1].split("]")[0];
            Log.d("error", data2);
            if(data2.equals("71.0") || data2.equals("51.0"))postureSum[0]++;
            else if(data2.equals("72.0") || data2.equals("52.0"))postureSum[1]++;
            else if(data2.equals("73.0") ||data2.equals("53.0"))postureSum[2]++;
        }
        return timeSum;
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
                jsonInput.put("uri", "/30073/0/3");
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

}
