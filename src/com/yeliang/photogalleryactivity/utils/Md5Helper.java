package com.yeliang.photogalleryactivity.utils;

import java.security.MessageDigest;

/**
 * MD5辅助类，对字符串取MD5
 */
public class Md5Helper
{
	/**
	 * 使用MD5算法对传入的key进行加密并且返回
	 */
	private static MessageDigest mDigest = null;
	
	static 
	{
		try
		{
			mDigest = MessageDigest.getInstance("MD5");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 对可以进行MD5加密，如果没有MD5加密算法，则直接使用key对应的hash值
	 */
	public static String toMD5(String key)
	{
		String cachekey;
		//获取MD5算法失败时，直接使用key对应的hash值
		if(mDigest == null)
		{
			return String .valueOf(key.hashCode());
		}
		mDigest.update(key.getBytes());
		cachekey = bytesToHexString(mDigest.digest());
		return cachekey;
	}

	private static String bytesToHexString(byte[] digest)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < digest.length; i++)
		{
			String hex = Integer.toHexString(0xFF & digest[i]);
			if(hex.length() == 1)
			{
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}	
	
}
