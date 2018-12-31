package org.androidtown.eswcontest;

import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    String login_Response = null;

    String screanCheck =null;
    String token= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ConnectServer connectServerPost = new ConnectServer();
//                connectServerPost.requestPost("https://eswcontest.neoidm.com/api/v1.0/login",
//                        "3133_mgr", "youngsun34");
//                while(login_Response==null){}

                Intent HomeIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(HomeIntent);

            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO
                ConnectServer connectServerPost = new ConnectServer();
                connectServerPost.requestPost("https://eswcontest.neoidm.com/api/v1.0/login",
                        "3133_mgr", "youngsun34");
                while(login_Response==null){}

                boolean isScreenOn = false;
                boolean preScreenOn = false;

                while (true) {
                    PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                    isScreenOn = powerManager.isScreenOn();

                    if (!isScreenOn && preScreenOn) {
                        Log.d("stop_screen", "OFF");
                        preScreenOn = isScreenOn;
                        ConnectServerPut connectServerPut = new ConnectServerPut();
                        connectServerPut.requestPost(" https://eswcontest.neoidm.com/api/v1.0/clients/kobot_01/30073/0/4","0");
                        // Log.d("data", screanCheck);

                    }
                    else if(isScreenOn &&!preScreenOn){
                        Log.d("stop_screen", "ON");
                        preScreenOn = isScreenOn;
                        ConnectServerPut connectServerPut = new ConnectServerPut();
                        connectServerPut.requestPost(" https://eswcontest.neoidm.com/api/v1.0/clients/kobot_01/30073/0/4","1");

                    }
                }

            }
        });
        thread.start();
    }

    class ConnectServer {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        private String responseString;

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
                    login_Response = response.body().string();

                }
            });
        }
    }

    class ConnectServerPut {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        private String responseString;

        public void requestPost(String url,String data) {
            JSONObject jsonInput = new JSONObject();

            try {
                jsonInput.put("id", "4");
                jsonInput.put("value", data);
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
                    .addHeader("Authorization", "Bearer " + login_Response.split("rData\":\"")[1].split("\"")[0])
                    .url(url)
                    .put(reqBody)
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
                    final String screanCheck2 = response.body().string();

                    Log.d("stop_screen",  screanCheck2);
                }

            });
        }

    }


}
