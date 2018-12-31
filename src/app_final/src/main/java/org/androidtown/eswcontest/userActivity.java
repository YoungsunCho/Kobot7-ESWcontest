package org.androidtown.eswcontest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class userActivity extends AppCompatActivity {
    TextView logOut, before;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        logOut = (TextView) findViewById(R.id.logOut);
//        before = (TextView) findViewById(R.id.before);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent = new Intent(userActivity.this, LoginActivity.class);
                userActivity.this.startActivity(HomeIntent);

            }
        });
//        before.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent HomeIntent = new Intent(userActivity.this, MainActivity.class);
//                userActivity.this.startActivity(HomeIntent);
//
//            }
//        });

    }

}
