package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	
	private TextView tv_safenumber;
	private ImageView iv_lock;
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//判断是否做过设置向导，如果没有做过就跳转到设置向导页面去设置，否则就留在当前页面
		boolean configed = sp.getBoolean("configed", false);
		if(configed){
			//做过设置向导，就留在手机防盗页面
			setContentView(R.layout.activity_lost_find);
			tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
			iv_lock  = (ImageView) findViewById(R.id.iv_lock);
			if(sp.getBoolean("start", false)){
				iv_lock.setImageResource(R.drawable.lock);
			}
			if(!TextUtils.isEmpty(sp.getString("safenumber", null))){
				tv_safenumber.setText(sp.getString("safenumber", null));
			}
			
		}else{
			//还没有做过设置向导
			Intent intent = new Intent(this, Setup1Activity.class);
			startActivity(intent);
			//关闭当前页面
			finish();
		}
		
	}
	

	/**
	 * 重新进入手机防盗向导页面
	 */
	
	public void reEnterSetup(View view){
		//还没有做过设置向导
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		//关闭当前页面
		finish();
	}

}
