package com.coolweather.app.service;

import com.coolweather.app.R;
import com.coolweather.app.activity.WeatherActivity;
import com.coolweather.app.receiver.AutoUpdateReceiver;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
  		String a =preferences.getString("temp1","");
  		String b =preferences.getString("temp2", "");
  		String c=preferences.getString("city_name", "");

    	Notification n = new Notification(R.drawable.logo,"天气预报",System.currentTimeMillis());
		Intent i1 = new Intent(this,WeatherActivity.class);
		PendingIntent intent1 = PendingIntent.getActivity(this,0, i1, 0);
		n.setLatestEventInfo(this, c+"的气温","最低气温"+a+",最高气温"+b,intent1);
		startForeground(1, n);

        new Thread(new Runnable() {

            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60*60*1000*8;//八小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this,AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(i, flags, startId);
    }
    private void updateWeather() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCode = sharedPreferences.getString("weather_code", "");
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

            @Override
            public void onFinish(String response) {
                Utility.handlerWeatherResponse(AutoUpdateService.this, response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

}