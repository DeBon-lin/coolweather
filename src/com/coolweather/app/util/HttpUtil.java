package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
		new Thread(new Runnable(){

			public void run(){
				
				try{
					HttpClient hc = (HttpClient) new DefaultHttpClient();
					HttpGet hg = new HttpGet(address);
					HttpResponse execute = hc.execute(hg);
					if(execute.getStatusLine().getStatusCode()==200)
					{
						HttpEntity entity = execute.getEntity();
						String response = EntityUtils.toString(entity,"utf-8");
						if(listener !=null){
							listener.onFinish(response);
						}
					}
				}catch (Exception e){
					if(listener !=null){
						listener.onError(e);
					}
				}
			}
		}).start();
	}
	
	
	
	
}
