package com.example.mobilesafe;

import com.example.mobilesafe.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	
	protected static final String TAG = "HomeActivity";
	private GridView list_home;
	private MyAdapter adapter;
	private SharedPreferences sp;
	private EditText et_setup_pwd;
	private EditText et_setup_confirm;
	private Button ok;
	private Button cancel;
	private AlertDialog dialog;
	private static String[] names={
		"手机防盗","通讯卫士","软件管理",
		"进程管理","流量统计","手机杀毒",
		"缓存清理","高级工具","设置中心"
	};
	
	private static int[] ids={
		R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
		R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
		R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		list_home = (GridView) findViewById(R.id.list_home);
		adapter = new MyAdapter();
		list_home.setAdapter(adapter);
		list_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 8://进入设置中心
				 Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
				 startActivity(intent);
					
					break;
				case 0://进入手机防盗页面
					showLostFindDialog();
				case 1:
					break;
				case 7://进入高级工具
					Intent intent7 = new Intent(HomeActivity.this, AtoolsActivity.class);
					startActivity(intent7);
					break;
				default:
					break;
				}
			}
			
		});
	}
	
	protected void showLostFindDialog() {
		//判断是否设置过密码
		if(isSetupPassword()){
			//已经设置密码，弹出输入对话框
			showEnterDialog();
		}else{
			//没有设置密码，弹出设置密码对话框
			showSetupPasswdDialog();
		}
	}
	
	/**
	 * 设置密码对话框
	 */
	private void showSetupPasswdDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		//自定义一个布局文件
		View view = View.inflate(HomeActivity.this, R.layout.dialog_setup_passwd, null);
		et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		et_setup_confirm = (EditText) view.findViewById(R.id.et_setup_confirm);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//取消对话框
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//取出密码
				String passwd = et_setup_pwd.getText().toString().trim();
				String passwd_confirm = et_setup_confirm.getText().toString().trim();
				if(TextUtils.isEmpty(passwd) || TextUtils.isEmpty(passwd_confirm)){
					Toast.makeText(HomeActivity.this, "密码为空", 0).show();
					return;
				}
				//判断是否一致才去保存
				if(passwd.equals(passwd_confirm)){
					//一致的话，就保存密码，把对话框清掉，还要进入手机防盗页面
					Editor editor = sp.edit();
					editor.putString("password", MD5Utils.md5PassWord(passwd));//保存成加密后的
					editor.commit();
					dialog.dismiss();
					Log.i(TAG, "一致的话，就保存密码，把对话框清掉，还要进入手机防盗页面");
					Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(HomeActivity.this, "密码不一致", 0).show();
					return;
				}
			}
		});
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 输入密码对话框
	 */
	private void showEnterDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		//自定义一个布局文件
		View view = View.inflate(HomeActivity.this, R.layout.dialog_enter_passwd, null);
		et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//取消对话框
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//取出密码
				String passwd = et_setup_pwd.getText().toString().trim();
				//取出加密后的
				String savePassword = sp.getString("password", "");
				if(TextUtils.isEmpty(passwd)){
					Toast.makeText(HomeActivity.this, "密码为空", 1).show();
					return;
				}
				if(MD5Utils.md5PassWord(passwd).equals(savePassword)){
					//输入的密码是之前设置的密码
					//对话框消掉，进入主页面
					dialog.dismiss();
					Log.i(TAG, "对话框消掉，进入手机防盗面");
					Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(HomeActivity.this, "密码错误", 1).show();
					et_setup_pwd.setText("");
					return;
				}
			}
		});
		
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 判断是否设置过密码
	 * @return
	 */
	private boolean isSetupPassword(){
		String password = sp.getString("password", null);
		return !TextUtils.isEmpty(password);
	}

	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.list_item_home, null);
			ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
			iv_item.setImageResource(ids[position]);
			tv_item.setText(names[position]);
			return view;
		}
		
	}
}
