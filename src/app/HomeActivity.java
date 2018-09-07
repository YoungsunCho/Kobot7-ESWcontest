package org.androidtown.test1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends Activity {
    BarChart barChart;
    String data = null;
    TextView sleepingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sleepingButton = (TextView) findViewById(R.id.sleepingButton);

        common com = (common) getApplication();

        ConnectServer connectServer = new ConnectServer();
        connectServer.requestDatarecord("https://eswcontest.neoidm.com/api/v1.0/datarecord",com.token);



        barChart = (BarChart)findViewById(R.id.barChart);
        createChart();//차트 만들기

        //차트 클릭하면 액티비티 이동
        barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //common com = (common) getApplication();
                //com.sleepingTime_today = com.computeSumTime(data)/60;
                Intent SleepingIntent = new Intent(HomeActivity.this, SleepingActivity.class);
                HomeActivity.this.startActivity(SleepingIntent);

            }
        });
    }

    void createChart(){
        List<BarEntry> entries = new ArrayList<BarEntry>();
        for (int i = 1;i <= 7;i++) {
            entries.add(new BarEntry((float) i, i));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "주간 수면시간");
        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);
        barChart.setFitBars(false);
        barChart.animateXY(1000,1000);
        barChart.invalidate();

    }
    class ConnectServer {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        private String responseString;

        public void requestDatarecord(String url,String token) {
            JSONObject jsonInput = new JSONObject();

            try {
                jsonInput.put("clientId", "swc_device0");
                jsonInput.put("uri", "30073/0/0");
                jsonInput.put("startDate", "2018-09-06 20:00");
                jsonInput.put("endDate", "2018-09-06 21:09");
                jsonInput.put("unitType", "0");
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            String requestUrl = urlBuilder.build().toString();

            RequestBody reqBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    jsonInput.toString()
            );


            //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
            Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + token)
                    .url(url)
                    .post(reqBody)
                    .build();


            //만들어진 Request를 서버로 요청할 Client 생성
            //Callback을 통해 비동기 방식으로 통신을 하여 서버로부터 받은 응답을 어떻게 처리 할 지 정의함
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String myResponse = response.body().string();

                    HomeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            data = myResponse;
                            Log.d("error", "Connect Server Error is " +data);
                        }
                    });
                }
            });
        }
    }

}