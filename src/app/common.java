package org.androidtown.test1;

import android.app.Application;
import android.util.Log;

public class common extends Application {

    public String token;
    public int sleepingTime_today;

    public void onCreate() {
        super.onCreate();
    }

    public int computeSumTime(String data){
        int sleeping_time = 0;
        String data_result = data;
        if(data !=null)
        {
            while(data_result.indexOf("],") != -1);
            {
                if(data_result.split("]")[0].split(",")[1].equals("1.0"))
                {
                    sleeping_time +=21;
                }
                data_result = data_result.split("],")[1];
            }

        }
        return sleeping_time;
    }
}
