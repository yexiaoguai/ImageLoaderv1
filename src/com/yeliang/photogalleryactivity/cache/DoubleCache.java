package com.yeliang.photogalleryactivity.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.yeliang.photogalleryactivity.request.BitmapRequest;

/**
 * 综合缓存,内存和sd卡双缓存
 */
public class DoubleCache implements ImageCache
{
	MemoryCache mMemoryCache = new MemoryCache();
	DiskCache mDiskCache;
	
	public DoubleCache(Context context)
	{
		mDiskCache = DiskCache.getDiskCache(context);
	}

	@Override
	public Bitmap get(BitmapRequest key)
	{
		Bitmap value = mMemoryCache.get(key);
		if(value == null)
		{
			value = mDiskCache.get(key);
			savaBitmapIntoMemory(key, value);
		}
		return value;
	}

	/**
	 * 如果Value从disk中读取,那么存入内存缓存
	 */
	private void savaBitmapIntoMemory(BitmapRequest key, Bitmap value)
	{
		if(value != null)
		{
			mMemoryCache.put(key, value);
		}	
	}

	@Override
	public void put(BitmapRequest key, Bitmap value)
	{
		mDiskCache.put(key, value);
		mMemoryCache.put(key, value);
	}

	@Override
	public void remove(BitmapRequest key)
	{
		mDiskCache.remove(key);
		mMemoryCache.remove(key);
	}
	
}
