package com.yeliang.photogalleryactivity.core;

import java.util.concurrent.BlockingQueue;

import android.util.Log;

import com.yeliang.photogalleryactivity.loader.Loader;
import com.yeliang.photogalleryactivity.loader.LoaderManager;
import com.yeliang.photogalleryactivity.request.BitmapRequest;

/**
 * 网络请求Dispatcher,继承自Thread，从网络请求队列中循环读取请求并且执行
 * 可以说一个线程执行一个BItmapRequest
 */
public class RequestDispatcher extends Thread
{
	/**
	 * 网络请求队列
	 */
	private BlockingQueue<BitmapRequest> mRequestQueue;
	
	public RequestDispatcher(BlockingQueue<BitmapRequest> queue)
	{
		mRequestQueue = queue;
	}
	
	/**
	 * 在线程里执行
	 */
	@Override
	public void run()
	{
		try
		{
			//线程不中断下一直循环
			while(!this.isInterrupted())
			{
				final BitmapRequest request = mRequestQueue.take();
				if(request.isCancel)
				{
					continue;
				}
				//解析图片uri的schema.
				//例如网络图片对象的schema是http或者https，sd卡存储的图片对应的schema为file.
				//根据schema我们从LoaderManager中获取对应的Loader来加载图片.
				final String schema = parseSchema(request.imageUri);
				Loader imageLoader = LoaderManager.getInstance().getLoader(schema);
				imageLoader.loadImage(request);
			}
		} catch (Exception e)
		{
			Log.i(getName(), "###### 请求分发器退出");
		}	
	}
	
	/**
	 * 解析出uri的Schema
	 */
	private String parseSchema(String uri)
	{
		if(uri.contains("://"))
		{
			Log.d(getName(), "#####image Schema is : " + uri.split("://")[0]);
			return uri.split("://")[0];
		}else
		{
			Log.d(getName(), "##### wrong scheme, image uri is : " + uri);
		}
		return "";	
	}
	
}
