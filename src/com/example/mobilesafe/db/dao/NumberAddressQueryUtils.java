package com.example.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryUtils {
	
	private static String path = "data/data/com.example.mobilesafe/files/address.db";

	/**
	 * 传一个号码进来，返回一个归属地回去
	 * @param number
	 * @return
	 */
	public static String queryNumber(String number){
		String address = number;
		SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		//path 把address.db拷贝到data/data/<包名>/files/address.db
		
		//手机号码 13 14 16 17 18
		//手机号码的正则表达式
		if(number.matches("^1[34678]\\d{9}$")){
			//手机号码
			Cursor cursor = database.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)", new String[]{number.substring(0,7)});
			while(cursor.moveToNext()){
				String location = cursor.getString(0);
				address = location;
			}
			cursor.close();
		}else{
			//其他的号码如110 、119
			switch (number.length()) {
			case 3:
				//很粗糙的判断,如110
				address = "公安号码";
				break;
			case 4:
				address = "模拟器";
				break;
			case 5:
				//如10086
				address = "客服电话";
				break;
			case 7:
				address = "本地电话";
				break;
			case 8:
				address = "本地电话";
				break;
			default:
				//处理长途电话
				if(number.length()>10 && number.startsWith("0")){
					// 010-12345678
					Cursor cursor = database.rawQuery("select location from data2 where area =? ", new String[]{number.substring(1, 3)});
				
					while(cursor.moveToNext()){
						String location = cursor.getString(0);
						address = location.substring(0, location.length()-2);
					}
					cursor.close();
					//0513-82423117
					cursor = database.rawQuery("select location from data2 where area =? ", new String[]{number.substring(1, 4)});
					
					while(cursor.moveToNext()){
						String location = cursor.getString(0);
						address = location.substring(0, location.length()-2);
					}
					cursor.close();
				}
				break;
			}
		}
		return address;
	}
}
