package com.example.mobilesafe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobilesafe.domain.UrlBean;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	private RelativeLayout rl_root;
	private int version;
	private String desc;
	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();//初始化界面
		setContentView(R.layout.activity_main);
		initAnimation();//初始化动画
		checkVersion();
	}
	/*
	 *访问服务器获取json数据 
	 */
	private void checkVersion() {
		// TODO Auto-generated method stub
		new Thread(){//匿名内部类
			public void run() {
				try {
					URL url = new URL("http://10.0.2.2:8080/mobilesafe.json");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					int result = conn.getResponseCode();
					if(result == 200){
						InputStream is = conn.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(is));
						String line = reader.readLine();
						StringBuilder jsonString = new StringBuilder();
						while(line != null){
							jsonString.append(line);
							line = reader.readLine();
						}
						//解析json数据
						UrlBean parserJson = parseJson(jsonString);
						System.out.println(parserJson.getVersionCode()+parserJson.getUrl()+parserJson.getDesc());
						reader.close();
						conn.disconnect();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

	protected UrlBean parseJson(StringBuilder jsonString) {
		// TODO Auto-generated method stub
		UrlBean bean = new UrlBean();
		try {
			JSONObject jobject = new JSONObject(jsonString + "");
			version = jobject.getInt("version");
			desc = jobject.getString("desc");
			url = jobject.getString("url");
			bean.setVersionCode(version);
			bean.setUrl(url);
			bean.setDesc(desc);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}
	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_main);
		rl_root = (RelativeLayout) findViewById(R.id.rt_splash_root);
	}

	private void initAnimation() {
		// TODO Auto-generated method stub
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);                                                                                
		aa.setDuration(3000);                                                                                                              
		aa.setFillAfter(true);                                                                                                             
		RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);                                                                               
		ra.setDuration(3000);                                                                                                              
		ra.setFillAfter(true);                                                                                                             
		ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(3000);                                                                                                              
		sa.setFillAfter(true);                                                                                                             
		AnimationSet as = new AnimationSet(true);                                                                                          
		as.addAnimation(aa);                                                                                                               
		as.addAnimation(ra);                                                                                                               
		as.addAnimation(sa);
		rl_root.startAnimation(as);
	}
}
