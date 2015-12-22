package com.yeliang.photogalleryactivity.core;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.yeliang.photogalleryactivity.cache.ImageCache;
import com.yeliang.photogalleryactivity.cache.MemoryCache;
import com.yeliang.photogalleryactivity.config.DisplayConfig;
import com.yeliang.photogalleryactivity.config.ImageLoaderConfig;
import com.yeliang.photogalleryactivity.policy.SerialPolicy;
import com.yeliang.photogalleryactivity.request.BitmapRequest;

/**
 * ImageLoader
 */
public class ImageLoader
{
	private static final ImageLoader mImageLoader = new ImageLoader();
	
	private ImageLoader()
	{
	}
	
	public static ImageLoader getInstance()
	{
		return mImageLoader;
	}
	
	/**
	 * 缓存,默认为内存缓存
	 */
	private ImageCache mImageCache = new MemoryCache();
	
	/**
	 * 网络请求队列
	 */
	private RequestQueue mRequestQueue;
	
	/**
     * 图片加载配置对象
     */
	private ImageLoaderConfig mConfig;
	
	/**
	 * 初始化ImageLoader对象，将ImageLoaderConfig配置进去
	 * 并且检查配置的合法性
	 */
	public void init (ImageLoaderConfig config)
	{
		mConfig = config;
		//更新缓存方法对象，有可能在config中设置成DiskCache
		mImageCache = mConfig.imageCache;
		//检查配置
		checkConfig();
		//初始化网络请求队列
		mRequestQueue = new RequestQueue(mConfig.threadCount);
		//开启请求队列，其实是开启了 初始化了RequestDispatcher对象 网络请求的线程
		//这个时候请求队列里面还没有BitmapRequest对象(请求对象)
		//后面用户调用displayImage()方法时才添加了BitmapRequest对象
		mRequestQueue.start();
	}
	
	private void checkConfig()
	{
		if(mConfig == null)
		{
			throw new RuntimeException(
					"The config of SimpleImageLoader is Null,"
					+ " please call the init(ImageLoaderConfig config) method to initialize");
		}
		//客户端在设置的时候，有可能设置为null
		if(mConfig.loadPolicy == null)
		{
			mConfig.loadPolicy = new SerialPolicy();
		}
		if(mImageCache == null)
		{
			mImageCache = new MemoryCache();
		}
	}
	
	public ImageLoaderConfig getConfig()
	{
		return mConfig;
	}
	
	public void displayImage(ImageView imageView, String uri)
	{
		displayImage(imageView, uri, null, null);
	}
	
	public void displayImage(ImageView imageView, String uri, DisplayConfig config)
	{
		displayImage(imageView, uri, config, null);
	}
	
	public void displayImage(ImageView imageView, String uri, ImageListener listener)
	{
		displayImage(imageView, uri, null, listener);
	}

	/**
	 * 用户调用displayImage之后它会将这个调用封装成一个BitmapRequest请求,然后将该请求添加到请求队列中
	 */
	private void displayImage(final ImageView imageView, final String uri, 
			final DisplayConfig config, final ImageListener listener)
	{
		//网络请求类
		BitmapRequest request = new BitmapRequest(imageView, uri, config, listener);
		//加载配置对象，如果没有设置则使用ImageLoader的配置
		request.displayConfig = request.displayConfig != null ? request.displayConfig
				: mConfig.displayConfig;
		//添加队列中
		mRequestQueue.addRequest(request);	
	}
	
	public void stop()
	{
		mRequestQueue.stop();
	}
	
	/**
	 * 图片加载Listener
	 */
	public static interface ImageListener
	{
		public void onComplete(ImageView imageView, Bitmap bitmap, String uri);
	}

}
