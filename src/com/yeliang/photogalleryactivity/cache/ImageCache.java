package com.yeliang.photogalleryactivity.cache;

import com.yeliang.photogalleryactivity.request.BitmapRequest;

import android.graphics.Bitmap;

/**
 *缓存的接口，方法有通过key得到get(Bitmap)，还有put(key value),以及移除
 */
public interface ImageCache
{
	public Bitmap get(BitmapRequest key);
	
	public void put(BitmapRequest key, Bitmap value);
	
	public void remove(BitmapRequest key);

}
