package org.androidtown.eswcontest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

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
import java.util.Date;


public class AlarmFragment extends android.app.Fragment {
    View v;
    String data ="", token;
    String login_Response = null;
    TextView text;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_alarm,container,false);
        text = (TextView) v.findViewById(R.id.text);
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
                Log.d("check","1");
                computeSnore(login_Response);
                Log.d("check","2");
                if(data.equalsIgnoreCase(""))
                    text.setText("알람 내역이 없습니다.");
                else text.setText(data);
                dialog.dismiss();
            }
        }, 1000);
    }

    public void computeSnore(String snoreData){
        String preData="", nowData="";
//        Log.d("snore",data);

        for(int i= 1; i<(snoreData.length()/21)-1; i++)
        {
            nowData = snoreData.split(",")[i*2-1].split("]")[0];

            if(nowData!="" && preData!="" && !nowData.equals(preData))
            {
                long time = new Long(snoreData.split(",")[(i-1)*2].split("]")[0].substring(1,14)).longValue();
                Date today = new Date (time);
                data+=today+"\n";
                Log.d("today",today+"");
                Log.d("data",data);
            }
            preData = nowData;
        }
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
                jsonInput.put("startDate", "2018-11-27 18:00");
                jsonInput.put("endDate", "2018-11-27 24:00");
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


}