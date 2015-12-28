package com.example.mobilesafe;

import com.example.mobilesafe.service.AddressService;
import com.example.mobilesafe.ui.SettingClickView;
import com.example.mobilesafe.ui.SettingItemView;
import com.example.mobilesafe.utils.ServiceUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {
	
	//设置是否开启自动更新
	private SettingItemView siv_update;
	//设置是否开启显示归属地
	private SettingItemView siv_show_address;
	private SharedPreferences sp;
	
	private Intent showAddress;
	
	//设置归属地显示框背景
	private SettingClickView settingClickView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
		
		//设置是否开启自动升级
		boolean update = sp.getBoolean("update", false);
		if(update){
			//自动升级已开启
			siv_update.setChecked(true);
		}else{
			//自动升级已关闭
			siv_update.setChecked(false);
		}
		siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				//判断是否选中
				//已经打开自动升级
				if(siv_update.isChecked()){
					siv_update.setChecked(false);
					editor.putBoolean("update", false);
				}else{
					//没有打开自动升级
					siv_update.setChecked(true);
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});
		
		//设置是否显示号码归属地
		showAddress = new Intent(this, AddressService.class);
		boolean isRunning = ServiceUtils.isServiceRunning(this, "com.example.mobilesafe.service.AddressService");
		if(isRunning){
			//监听来电的服务是运行的
			siv_show_address.setChecked(true);
		}else{
			//监听来电的服务时关闭的
			siv_show_address.setChecked(false);
		}
		//设置号码归属地显示控件
		siv_show_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(siv_show_address.isChecked()){
					siv_show_address.setChecked(false);
					stopService(showAddress);
				}else{
					siv_show_address.setChecked(true);
					startService(showAddress);
				}
			}
		});
		
		settingClickView = (SettingClickView) findViewById(R.id.scv_changebg);
		settingClickView.setTitle("归属地提示框风格");
		final String[] items = {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
		int which = sp.getInt("which", 0);
		settingClickView.setDesc(items[which]);
		settingClickView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int dd = sp.getInt("which", 0);
				//弹出一个对话框
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("归属地提示框风格");
				builder.setSingleChoiceItems(items, dd, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						//保存选择参数
						Editor editor = sp.edit();
						editor.putInt("which", which);
						editor.commit();
						settingClickView.setDesc(items[which]);
						//取消对话框
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("cancel", null);
				builder.show();
			}
		});
		
	}
}
