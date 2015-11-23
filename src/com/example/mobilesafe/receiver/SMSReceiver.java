package com.example.mobilesafe.receiver;

import com.example.mobilesafe.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReceiver";
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		//写接受短信的代码
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		for(Object object: objs){
			SmsMessage sms = SmsMessage.createFromPdu((byte[])object);
			//发送者
			String sender = sms.getOriginatingAddress();
			String safenumber = sp.getString("safenumber", "");
			if(sender.contains(safenumber)){
				String body = sms.getMessageBody();
				
				if("#*location*#".equals(body)){
					Log.d(TAG, "得到手机的GPS");
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
					abortBroadcast();
				}else if("#*lockscrenn*#".equals(body)){
					Log.d(TAG, "远程锁屏");
					abortBroadcast();
				}
			}
			
		}
		
		
	}

}
