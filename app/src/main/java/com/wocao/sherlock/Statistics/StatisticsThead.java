package com.wocao.sherlock.Statistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by silen on 17-4-16.
 */

public class StatisticsThead {
    private Context context;
    public StatisticsThead(Context context) {
        super();
        this.context=context;
    }
    public void start(){
        Calendar calendar=Calendar.getInstance();
        final int dateInt=((calendar.get(Calendar.YEAR))*100+calendar.get(Calendar.MONTH)+1)*100+calendar.get(Calendar.DAY_OF_MONTH);
        final SharedPreferences sp=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        if (sp.getInt("statisticsdate",0)!=dateInt) {
            HttpThread httpThread=new HttpThread();
            httpThread.setSuccess(new HttpThread.Success() {

                @Override
                public void success() {
                    sp.edit().putInt("statisticsdate",dateInt).commit();
                }
            });
            httpThread.start();
        }
    }
     static class HttpThread extends Thread{
        Success success;
        @Override
        public void run() {
            super.run();
            String result=getResultForHttpGet("http://sherlock.silen-dev.cn/Statistics/AppVisitStatisticsServlet");
            if (null!=result&&!result.trim().equals("")){
                success.success();
            }
        }

        interface Success{
            void success();
        }
        public void setSuccess(Success success){
            this.success=success;
        }
    }
    public static String getResultForHttpGet(String urlPath) {

        String result = "";
        try {
            URL url = new URL(urlPath);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(false);
            con.setRequestMethod("GET");
            con.setConnectTimeout(3000);

            if (con.getResponseCode() == 200) {

                InputStream is = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));

                String line = "";
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                Log.i("Statistics",result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

        // result;
    }
}
