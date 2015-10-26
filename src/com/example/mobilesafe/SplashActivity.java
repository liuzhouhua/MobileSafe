package com.example.mobilesafe;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.widget.TextView;

public class SplashActivity extends Activity {
	
	private TextView tv_splash_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("版本号"+getVersionName());
	}

	/*
	 * 得到应用程序的版本名称
	 * */
	private String getVersionName(){
		//用来管理手机的apk
		PackageManager pm = getPackageManager();
		//得到指定APK的功能清单文件
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

}
