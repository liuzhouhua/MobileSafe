package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup3Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}
	
	/**
	 * 下一步
	 * @param view
	 */
	public void next(View view){
		Intent intent = new Intent(this, Setup4Activity.class);
		startActivity(intent);
		finish();
	}
	
	/**
	 * 上一步
	 * @param view
	 */
	public void pre(View view){
		Intent intent = new Intent(this, Setup2Activity.class);
		startActivity(intent);
		finish();
	}
}