package com.example.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	/**
	 * MD5加密
	 * @param password
	 * @return
	 */
	public static String md5PassWord(String password) {
		try {
			// 得到一个信息摘要器
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			// 把每一个byte做一个与运算0xff
			for (byte b : result) {
				// 0xff是十六进制，十进制为255
				int nuber = b & 0xff;
				String str = Integer.toHexString(nuber);
				if (str.length() == 1) {
					buffer.append("0");
				}
				buffer.append(str);
			}
			// 这就是MD5加密得到的值
			System.out.println(buffer);
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

}
