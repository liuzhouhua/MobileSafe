package com.example.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	protected static final String TAG = "BaseSetupActivity";
	//1、定义一个手势识别器
	private GestureDetector detector;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//2、实例化这个手势识别器
				detector = new GestureDetector(this, new OnGestureListener() {
					
					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						return false;
					}
					
					@Override
					public void onShowPress(MotionEvent e) {
						
					}
					
					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
							float distanceY) {
						return false;
					}
					
					@Override
					public void onLongPress(MotionEvent e) {
						
					}
					
					/**
					 * 当我们的手指在上面滑动的时候回调
					 * e1:手指第一次按在屏幕上的点
					 * e2:手机离开屏幕时的点
					 * velocityX:x轴方向的速度
					 * velocityY:y轴方向的速度
					 */
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
							float velocityY) {
						
						//屏蔽在X轴滑动很慢的情形
						if(Math.abs(velocityX) < 200){
							Toast.makeText(BaseSetupActivity.this, "滑动太慢了", 1).show();
							return true;
						}
						
						//屏蔽斜着滑动这种情况
						if(Math.abs(e2.getRawY() - e1.getRawY())>100){
							Toast.makeText(BaseSetupActivity.this, "不能斜着滑动", 1).show();
							return true;
						}
						
						
						if(e2.getRawX() - e1.getRawX() > 200){
							//显示上一个页面：从左往右滑动
							Log.i(TAG, "显示上一个页面：从左往右滑动");
							showPre();
							return true;
						}
						if(e1.getRawX() - e2.getRawX() > 200){
							//显示下一个页面：从右往左滑动
							Log.i(TAG, "显示下一个页面：从右往左滑动");
							showNext();
							return true;
						}
						return false;
					}
					
					@Override
					public boolean onDown(MotionEvent e) {
						return false;
					}
				});
	}
	
	protected abstract void showNext();
	
	protected abstract void showPre();
	
	public void next(View view){
		showNext();
	}
	
	/**
	 * 上一步
	 * @param view
	 */
	public void pre(View view){
		showPre();
	}


	/**
	 * 3、使用手势识别器
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
