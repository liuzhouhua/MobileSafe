package com.example.mobilesafe.receiver;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.GPSService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.sax.StartElementListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReceiver";
	private SharedPreferences sp;
	private DevicePolicyManager dpm;

	@Override
	public void onReceive(Context context, Intent intent) {
		//写接受短信的代码
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		
		for(Object object: objs){
			SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
			//发送者
			String sender = sms.getOriginatingAddress();
			String safenumber = sp.getString("safenumber", "");
			if(sender.contains(safenumber)){
				String body = sms.getMessageBody();
				
				if("#*location*#".equals(body)){
					Log.d(TAG, "得到手机的GPS");
					//启动服务
					Intent i = new Intent(context, GPSService.class);
					context.startService(i);
					SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
					String lastlocation = sp.getString("lastlocation", null);
					if(TextUtils.isEmpty(lastlocation)){
						//位置没有得到
						SmsManager.getDefault().sendTextMessage(sender, null, "get location...", null, null);
					}else{
						SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
					}
					//把这个广播终止掉，不让其他接受短信的接收到
					abortBroadcast();
				}else if("#*alarm*#".equals(body)){
					Log.d(TAG, "播放报警音乐");
					
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					player.setLooping(true);
					player.setVolume(1.0f, 1.0f);
					player.start();
					
					abortBroadcast();
				}else if("#*wipedata*#".equals(body)){
					Log.d(TAG, "远程清除数据");
					Intent intent2 = new Intent();
					ComponentName name = new ComponentName(context, MyAdmin.class);
					intent2.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, name);
					intent2.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "哥们开启我可以清除数据");
					context.startActivity(intent2);
					if(dpm.isAdminActive(name)){
						dpm.wipeData(0);
					}
					abortBroadcast();
				}else if("#*lockscrenn*#".equals(body)){
					Log.d(TAG, "远程锁屏");
					Intent intent3 = new Intent();
					ComponentName name = new ComponentName(context, MyAdmin.class);
					intent3.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, name);
					intent3.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "哥们开启我可以一键锁屏");
					context.startActivity(intent3);
					if(dpm.isAdminActive(name)){
						dpm.lockNow();
					}
					abortBroadcast();
				}
			}
			
		}
		
		
	}

}
