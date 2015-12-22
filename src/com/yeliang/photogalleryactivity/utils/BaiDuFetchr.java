package com.yeliang.photogalleryactivity.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.yeliang.photogalleryactivity.core.CloseUtils;
import com.yeliang.photogalleryactivity.model.GalleryItem;

public class BaiDuFetchr
{
	private static final String TAG = "FlickrFetchr";
	
	private static final String ENDPOINT = "http://apis.baidu.com/txapi/mvtp/meinv";
	private static final String API_KEY = "f2474640b4cbb3f4cf0beabc36eede3e";
	private static final String PARAM_EXTRAS = "num=20";
	
	/**
	 * 
	 */
	public String getUrl(String urlSpec) throws IOException
	{
		Log.d(TAG, "getUrlBytes");
		BufferedReader reader = null;
		StringBuffer sbf = new StringBuffer();
		String result = null;
		URL url = new URL(urlSpec);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("apikey",  API_KEY);
		connection.connect();
		try
		{
			InputStream in = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
					
			if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
			{
				return null;
			}
			String startRead = null;
			if((startRead = reader.readLine()) != null)
			{
				sbf.append(startRead);
			}
			result = sbf.toString();		
		}finally
		{
			if(reader != null)
			{
				CloseUtils.close(reader);
			}
			connection.disconnect();
		}	
		return result;
	}
	
	private GalleryItem parseJSONWithJSONObject(String jsonData, int i)
	{
		GalleryItem item = null;
		try
		{
			JSONObject jb = new JSONObject(jsonData);
			JSONObject weatherInfo = jb.getJSONObject(String.valueOf(i));
				
			String title = weatherInfo.getString("title");
			String description = weatherInfo.getString("description");
			String picUrl = weatherInfo.getString("picUrl");
			String url = weatherInfo.getString("url");
				
			Log.d("MainActivity", "title is " + title);
			Log.d("MainActivity", "description is " + description);
			Log.d("MainActivity", "picUrl is " + picUrl);
			Log.d("MainActivity", "url is " + url);
			
			item = new GalleryItem();
			item.setTitle(title);
			item.setDescription(description);
			item.setPicUrl(picUrl);
			item.setUrl(url);
		} catch (Exception e)
		{
			e.printStackTrace();
		}	
		return item;
	}
	
	public ArrayList<GalleryItem> fetchItems()
	{
		ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();
		GalleryItem item;
		
		String httpUrl = ENDPOINT;
		String httpArg = PARAM_EXTRAS;
		httpUrl = httpUrl + "?" + httpArg;
		String jsonString = null;
		try
		{
			jsonString = getUrl(httpUrl);
		} catch (IOException e)
		{
			Log.e(TAG, "Failed to fetch item", e);
		}	
		
		String result = jsonString.substring(23,jsonString.length() - 1);
		String[] array = result.split("\\}");
		for(int i = 0; i < array.length; i++)
		{
			//Log.d(TAG, array[i]);
			String newResult = array[i];
			newResult = newResult + "}}";
			if(i != 0)
			{
				newResult = newResult.substring(1, newResult.length());
			}
			newResult = "{" + newResult;
			
			item = parseJSONWithJSONObject(newResult, i);
			items.add(item);
		}
		return items;	
	}
	
}
