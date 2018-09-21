package org.androidtown.eswcontest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextId;
    private EditText editTextPw;
    private TextView register;

    int check_screen = 0;
    common com = new common();
    String login_Response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        mainProcessing();

        editTextId = (EditText) findViewById(R.id.idText);
        editTextPw = (EditText) findViewById(R.id.passwordText);
        Button login = (Button) findViewById(R.id.login);

        register = (TextView) findViewById(R.id.registerButton);

        //버튼이 클릭되면 여기 리스너로 옴
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectServer connectServerPost = new ConnectServer();
                connectServerPost.requestPost("https://eswcontest.neoidm.com/api/v1.0/login",
                        "3133_mgr", "youngsun34");


                if (login_Response == null) {
                    register.setText("null");
                } else {
                    common com = (common) getApplication();
                    com.token = login_Response.split("rData\":\"")[1].split("\"")[0];

                    register.setText(login_Response);
                }

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        //registBroadcastReceiver();
        Log.d("activity status", "stop");
//        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//        boolean isScreenOn = false;
//        check_screen = 1;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO
                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                boolean isScreenOn = false;
                check_screen = 0;
                while (check_screen == 0) {
                    isScreenOn = powerManager.isScreenOn();

                    if (!isScreenOn) {
                        Log.d("stop_screen", "OFF");
                        common com = (common) getApplication();
                        ConnectServerPut connectServerPut = new ConnectServerPut();
                        connectServerPut.requestPost(" https://eswcontest.neoidm.com/api/v1.0/clients/swc_device0/30073/0/4",com.token);

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
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String myResponse = response.body().string();

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login_Response = myResponse;
                            Log.d("error", "Connect Server Error is " + login_Response);
                            if (login_Response.split("ok\":")[1].split(",")[0].equals("true")) {
                                common com = (common) getApplication();
                                Intent HomeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                LoginActivity.this.startActivity(HomeIntent);
                            }
                        }
                    });
                }
            });
        }
    }

    class ConnectServerPut {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        private String responseString;

        public void requestPost(String url,String token) {
            JSONObject jsonInput = new JSONObject();

            try {
                jsonInput.put("id", "4");
                jsonInput.put("value", "0");
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
                    .addHeader("Authorization", "Bearer " + token)
                    .url(url)
                    .put(reqBody)
                    .build();

            final String[] a = {null};

            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String myResponse = response.body().string();

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login_Response = myResponse;
                            Log.d("error", "Connect Server Error is " + login_Response);
                            if (login_Response.split("ok\":")[1].split(",")[0].equals("true")) {
                                common com = (common) getApplication();
                                Intent HomeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                LoginActivity.this.startActivity(HomeIntent);
                            }
                        }
                    });
                }
            });
        }

    }

}
