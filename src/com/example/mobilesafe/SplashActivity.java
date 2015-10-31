package com.example.mobilesafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobilesafe.utils.StreamTools;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	
	private static final String TAG = "SplashActivity";

	protected static final int ENTER_HOME = 0;

	protected static final int SHOW_UPDATE_DIALOG = 1;

	protected static final int URL_ERROR = 2;

	protected static final int NETWORK_ERROR = 3;

	protected static final int JSON_ERROR = 4;
	
	private TextView tv_splash_version,tv_update_info;
	private String description;
	private String apkurl;
	private SharedPreferences sp;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOW_UPDATE_DIALOG://显示升级的对话框
				Log.d(TAG, "显示升级的对话框");
				showUpdateDialog();
				break;
			case ENTER_HOME://进入主页面
				enterHome();
				break;
			case URL_ERROR://URL错误
				enterHome();
				Toast.makeText(getApplicationContext(), "URL错误", Toast.LENGTH_SHORT).show();
				break;
			case NETWORK_ERROR://网络异常
				enterHome();
				Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
				break;
			case JSON_ERROR://JSON解析出错
				enterHome();
				Toast.makeText(getApplicationContext(), "JSON解析出错", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("版本号"+getVersionName());
	
		tv_update_info = (TextView) findViewById(R.id.tv_update_info);
		
		boolean update = sp.getBoolean("update", false);
		if(update){
			//检查升级
			checkUpdate();
		}else{
			//自动升级已关闭
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					//进入主页面
					enterHome();
				}
			}, 2000);
		}
		
		//动画
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(500);
		findViewById(R.id.rl_root_splash).startAnimation(aa);
	}

	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(getApplicationContext());
		builder.setTitle("提示升级");
//		builder.setCancelable(false);
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				//进入主页面
				enterHome();
				dialog.dismiss();
			}
		});
		builder.setMessage(description);
		builder.setPositiveButton("立即升级", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//下载APK，并且替换安装
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					//SD卡存在
					//afnal框架使用
					FinalHttp finalHttp = new FinalHttp();
					finalHttp.download(apkurl, Environment.getExternalStorageDirectory()+"/mobilesafe2.0.apk",
							new AjaxCallBack<File>() {

								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
									super.onFailure(t, errorNo, strMsg);
								}

								@Override
								public void onLoading(long count, long current) {
									super.onLoading(count, current);
									int progress = (int) (current*100/count);
									tv_update_info.setVisibility(View.VISIBLE);
									tv_update_info.setText("下载进度:"+progress+"%");
								}

								@Override
								public void onSuccess(File t) {
									super.onSuccess(t);
									installAPK(t);
								}

								//安装APK
								private void installAPK(File t) {
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");
									intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
								
									startActivity(intent);
								}
								
							});
				}else{
					Toast.makeText(getApplicationContext(), "没有SD卡，请安装再试", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
		builder.setNegativeButton("下次再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHome();
			}
		});
		builder.show();
	}

	protected void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	/*
	 * 检查是否有新版本，如果有就升级
	 * */
	private void checkUpdate() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message message = Message.obtain();
				long startTime = System.currentTimeMillis();
				try{
					URL url = new URL(getString(R.string.serverurl));
					//联网
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setConnectTimeout(4000);
					con.setRequestMethod("GET");
					int code = con.getResponseCode();
					if(code==200){
						//联网成功
						InputStream is = con.getInputStream();
						//把流转成String
						String result = StreamTools.readFromStream(is);
						Log.d(TAG, "connect result :"+result);
						JSONObject object = new JSONObject(result);
						//得到服务器的版本信息
						String version = object.getString("version");
						description = object.getString("description");
						apkurl = object.getString("apkurl");
					
						//校验是否有新版
						if(getVersionName().equals(version)){
							//版本一致，没有新版本，进入主页面
							message.what = ENTER_HOME;
						}else{
							//有新版本，弹出一个升级对话框
							message.what = SHOW_UPDATE_DIALOG;
						}
					}
				}catch(MalformedURLException e){
					e.printStackTrace();
					message.what = URL_ERROR;
				} catch (IOException e) {
					e.printStackTrace();
					message.what = NETWORK_ERROR;
				} catch (JSONException e) {
					e.printStackTrace();
					message.what = JSON_ERROR;
				}finally{
					long endTime = System.currentTimeMillis();
					long dTime = endTime - startTime;
					if(dTime < 2000){
						try {
							Thread.sleep(2000 - dTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					mHandler.sendMessage(message);
				}
			}
		}).start();
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
