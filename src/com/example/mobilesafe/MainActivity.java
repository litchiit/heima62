package com.example.mobilesafe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobilesafe.domain.UrlBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final int LOADMAIN = 1;
	private static final int SHOWUPDATEDIALOG = 2;
	private RelativeLayout rl_root;
	private int versionCode;
	private String versionName;
	private TextView tv_versionName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();// 初始化界面
		initData();
		setContentView(R.layout.activity_main);
		checkVersion();
	}

	private void initData() {
		// TODO Auto-generated method stub
		PackageManager pm = getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo("com.example.mobilesafe", 0);
			versionCode = pi.versionCode;
			versionName = pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 访问服务器获取json数据
	 */
	private void checkVersion() {
		// TODO Auto-generated method stub
		new Thread() {// 匿名内部类
			public void run() {
				try {
					URL url = new URL("http://192.168.1.107:8080/mobilesafe.json");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					int result = conn.getResponseCode();
					if (result == 200) {
						InputStream is = conn.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(is));
						String line = reader.readLine();
						StringBuilder jsonString = new StringBuilder();
						while (line != null) {
							jsonString.append(line);
							line = reader.readLine();
						}
						// 解析json数据
						UrlBean parserJson = parseJson(jsonString);
						isNewVersion(parserJson);
						System.out.println(parserJson.getVersionCode() + parserJson.getUrl() + parserJson.getDesc());
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
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADMAIN:
				Intent intent = new Intent(MainActivity.this,HomeActivity.class);
				startActivity(intent);
				break;
			case SHOWUPDATEDIALOG:
				showUpdateDialog();
				break;
			default:
				break;
			}
		};
	};
	
	protected void isNewVersion(UrlBean parserJson) {
		// TODO Auto-generated method stub
		int servercode = parserJson.getVersionCode();
		if(servercode == versionCode){
			loadMain();
		}else{
			Message msg = Message.obtain();
			msg.what = SHOWUPDATEDIALOG;
			handler.sendMessage(msg);
		}
	}

	private void loadMain() {
		Message msg = Message.obtain();
		msg.what = LOADMAIN;
		handler.sendMessage(msg);
	}

	protected void showUpdateDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("版本有更新")
			.setMessage("是否跟新新版本?")
			.setPositiveButton("跟新", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//更新APK
					
				}
			}).setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					loadMain();
				}
			});
		builder.show();
	}

	protected UrlBean parseJson(StringBuilder jsonString) {
		// TODO Auto-generated method stub
		UrlBean bean = new UrlBean();
		try {
			JSONObject jobject = new JSONObject(jsonString + "");
			int version = jobject.getInt("version");
			String desc = jobject.getString("desc");
			String url = jobject.getString("url");
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
		tv_versionName = (TextView) findViewById(R.id.tv_splash_version_name);
	}
}
