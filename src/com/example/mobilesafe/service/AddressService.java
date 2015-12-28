package com.example.mobilesafe.service;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AddressService extends Service {
	//监听来电
	private TelephonyManager tm;
	
	private MyPhoneStateListener listener;
	private OutCallReceiver receiver;
	//窗体管理者---这是个服务
	private WindowManager wm;
	private View view;
	
	private class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			//得到要打出去的电话号码
			String phone = getResultData();
			String address = NumberAddressQueryUtils.queryNumber(phone);
//			Toast.makeText(context, address, 1).show();
			myToast(address);
		}

	}
	
	private class MyPhoneStateListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			//第一个参数是状态。第二个参数是电话号码
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING://电话铃声响起的时候，即来电的时候
				//更加得到的电话号码查询他的归属地，并显示字土司里面
				String address = NumberAddressQueryUtils.queryNumber(incomingNumber);
//				Toast.makeText(getApplicationContext(), address, 1).show();
				myToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE://电话的空闲状态：挂电话、来电话拒绝
				//把号码显示归属地的view移除
				if(view!=null){
					wm.removeView(view);
				}
				break;
			default:
				break;
			}
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//注册一个广播接受者
		receiver = new OutCallReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, intentFilter);
		
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		//监听来电
		listener  = new MyPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	
		//实例化窗体
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//代码取消注册一个广播接受者
		unregisterReceiver(receiver);
		receiver = null;
		
		//取消监听来电
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}
	
	private void myToast(String address) {
//		view = new TextView(getApplicationContext());
//		view.setText(address);
//		view.setTextSize(22);
//		view.setTextColor(Color.RED);
		//"半透明","活力橙","卫士蓝","金属灰","苹果绿"
		int[] ids = {R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue
				,R.drawable.call_locate_gray,R.drawable.call_locate_green};
		view = View.inflate(this, R.layout.address_show, null);
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		view.setBackgroundResource(ids[sp.getInt("which", 0)]);
		TextView textView =(TextView) view.findViewById(R.id.tv_address);
		textView.setText(address);
		//窗体参数设置
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;//半透明
        params.type = WindowManager.LayoutParams.TYPE_TOAST;//类型：土司
        //params.setTitle("Toast");//标题
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		
		wm.addView(view, params);
	}
}
