package com.example.mobilesafe.ui;

import com.example.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 
 * 我们的自定义组合控件，它里面有两个TextView，还有一个ImageView，还有一个View
 * @author Administrator
 *
 */
public class SettingClickView extends RelativeLayout {
	
	private TextView tv_desc;
	private TextView tv_title;
	
	private String desc_on;
	private String desc_off;
	
	/**
	 * 初始化布局文件
	 * @param context
	 */
	private void initView(Context context) {
		//把一个布局文件转化为View并且加载在SettingItemView
		View.inflate(context, R.layout.setting_click_view, this);
		tv_desc = (TextView) this.findViewById(R.id.tv_description);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
	}

	//自定义样式的情况下使用这个构造方法
	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	//在布局文件有的情况下实例化类调用这个构造方法
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "title");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "desc_off");
		tv_title.setText(title);
		tv_desc.setText(desc_off);
	}

	//new出来的对象调用这个构造方法
	public SettingClickView(Context context) {
		super(context);
		initView(context);
	}
	
	
	/**
	 * 设置组合控件的状态
	 * @param checked
	 */
	public void setChecked(boolean checked){
		if(checked){
			setDesc(desc_on);
		}else{
			setDesc(desc_off);
		}
	}
	
	
	/**
	 * 设置组合控件的描述信息
	 * @param text
	 */
	public void setDesc(String text){
		tv_desc.setText(text);
	}
	/**设置组合控件的标题
	 * 
	 * @param title
	 */
	public void setTitle(String title){
		tv_title.setText(title);
	}
}
