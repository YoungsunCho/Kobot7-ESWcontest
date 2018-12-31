package org.androidtown.eswcontest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class HomeFragment extends android.app.Fragment {
    View v;
    ImageView imbeddedURL;
    TextView imbeddedURL1;
    TextView text1,text2,text3,text4,text5,text6;
    ImageView img1,img2,img3,img4,img5,img6;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home,container,false);

        text1 = (TextView) v.findViewById(R.id.text1);
        text2 = (TextView) v.findViewById(R.id.text2);
        text3 = (TextView) v.findViewById(R.id.text3);
        text4 = (TextView) v.findViewById(R.id.text4);
        text5 = (TextView) v.findViewById(R.id.text5);
        text6 = (TextView) v.findViewById(R.id.text6);

        img1 = (ImageView) v.findViewById(R.id.img1);
        img2 = (ImageView) v.findViewById(R.id.img2);
        img5 = (ImageView) v.findViewById(R.id.img5);
        img6 = (ImageView) v.findViewById(R.id.img6);

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLink("https://1boon.kakao.com/dano/5bc06982ed94d200019a3b7a?view=katalk");
            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLink("https://1boon.kakao.com/dano/5bc06982ed94d200019a3b7a?view=katalk");
            }
        });
        /////////////////
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLink("https://www.sciencetimes.co.kr/?news=%EA%B0%80%EC%9E%A5-%EC%A2%8B%EC%9D%80-%EC%88%98%EB%A9%B4-%EC%9E%90%EC%84%B8%EB%8A%94");
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLink("https://www.sciencetimes.co.kr/?news=%EA%B0%80%EC%9E%A5-%EC%A2%8B%EC%9D%80-%EC%88%98%EB%A9%B4-%EC%9E%90%EC%84%B8%EB%8A%94");
            }
        });
        ////////////////////
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLink("https://m.post.naver.com/viewer/postView.nhn?volumeNo=12351746&memberNo=17277636");
            }
        });
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLink("https://1boon.kakao.com/dano/5bc06982ed94d200019a3b7a?view=katalk");
            }
        });

        imbeddedURL = (ImageView)v.findViewById (R.id.imbedded);
        imbeddedURL1 = (TextView)v.findViewById (R.id.imbedded1);

        imbeddedURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLink("http://eswcontest.com/htm/main.php");
            }
        });
        imbeddedURL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLink("http://eswcontest.com/htm/main.php");
            }
        });
        return v;

    }
    public void startLink(String url){
        Intent LinkIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        HomeFragment.this.startActivity(LinkIntent);
    }
}

