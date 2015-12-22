package com.yeliang.photogalleryactivity.cache;

import com.yeliang.photogalleryactivity.request.BitmapRequest;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCache implements ImageCache
{
	private LruCache<String, Bitmap> mMemoryCache;
	
	public MemoryCache()
	{
		//初始化内存缓存
		initMemoryCache();
	}
	
	private void initMemoryCache()
	{
		//计算可使用的最大内存
		final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
		//取4分之一的可用内存作为缓存
		final int cacheSize = maxMemory / 4;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize)
		{
			@Override
			protected int sizeOf(String key, Bitmap bitmap)
			{
				return bitmap.getRowBytes() * bitmap.getHeight() /1024;
			}
		};
	}

	@Override
	public Bitmap get(BitmapRequest key)
	{
		return mMemoryCache.get(key.imageUri);
	}

	@Override
	public void put(BitmapRequest key, Bitmap value)
	{
		mMemoryCache.put(key.imageUri, value);
	}

	@Override
	public void remove(BitmapRequest key)
	{
		mMemoryCache.remove(key.imageUri);
	}

}
