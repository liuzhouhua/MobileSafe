package com.example.mobilesafe.service;

import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.cardemulation.OffHostApduService;
import android.os.Bundle;
import android.os.IBinder;

public class GPSService extends Service {

	//位置服务
	private LocationManager lm;
	private MyLocationListener listener;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new MyLocationListener();
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = lm.getBestProvider(criteria, true);
		lm.requestLocationUpdates(provider, 0, 0, listener);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(listener);
		listener = null;
	}
	
	class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			String longitude = "jingdu："+location.getLongitude()+"\n";
			String latitude = "weidu："+location.getLatitude()+"\n";
			String accuracy = "accuracy："+location.getAccuracy()+"\n";
			//位置发生变化后发短信给安全号码(保存最后一次位置)
			//注意转换标准ＧＰＳ坐标转换为火星坐标
			try {
				InputStream is = getAssets().open("axisoffset.dat");
				ModifyOffset modifyOffset = ModifyOffset.getInstance(is);
				PointDouble double1 = modifyOffset.s2c(new PointDouble(location.getLongitude(), location.getLatitude()));
				longitude = "jingdu:"+modifyOffset.X+"\n";
				latitude = "weidu:"+modifyOffset.Y+"\n";
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("lastlocation", longitude+latitude+accuracy);
			editor.commit();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
	}
}
