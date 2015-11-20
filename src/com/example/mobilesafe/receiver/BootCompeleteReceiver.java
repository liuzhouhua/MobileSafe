package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BootCompeleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;
	private  static final String TAG = "BootCompeleteReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		//读取之前保存的SIM信息
		String saveSim = sp.getString("sim",null);
		
		//读取当前的SIM卡信息
		String realSim = tm.getSimSerialNumber();
		
		//比较是否一样，不一样发条短信给之前绑定的手机卡
		if(realSim.equals(saveSim)){
			//sim没有变更
			Log.i(TAG, "sim没有变更");
		}else{
			//sim已经变更
			Log.i(TAG, "sim已经变更");
		}
		
	}

}
