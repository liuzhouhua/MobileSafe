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
 * 我们的自定义组合控件，它里面有两个TextView，还有一个CheckBox，还有一个View
 * @author Administrator
 *
 */
public class SettingItemView extends RelativeLayout {
	
	private CheckBox cb_status;
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
		View.inflate(context, R.layout.setting_item_view, this);
		cb_status = (CheckBox) this.findViewById(R.id.cb_status);
		tv_desc = (TextView) this.findViewById(R.id.tv_description);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
	}

	//自定义样式的情况下使用这个构造方法
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	//在布局文件有的情况下实例化类调用这个构造方法
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "title");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "desc_off");
		tv_title.setText(title);
	}

	//new出来的对象调用这个构造方法
	public SettingItemView(Context context) {
		super(context);
		initView(context);
	}
	
	/**
	 * 校验组合控件是否选中
	 * @return
	 */
	public boolean isChecked(){
		return cb_status.isChecked();
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
		cb_status.setChecked(checked);
	}
	
	
	/**
	 * 设置组合控件的描述信息
	 * @param text
	 */
	public void setDesc(String text){
		tv_desc.setText(text);
	}
	
}
