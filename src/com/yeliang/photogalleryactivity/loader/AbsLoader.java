package com.yeliang.photogalleryactivity.loader;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.yeliang.photogalleryactivity.cache.ImageCache;
import com.yeliang.photogalleryactivity.config.DisplayConfig;
import com.yeliang.photogalleryactivity.core.ImageLoader;
import com.yeliang.photogalleryactivity.request.BitmapRequest;

/**
 * 加载图片的过程有如下几个步骤:
 * 1、判断缓存中是否含有该图片;
 * 2、如果有则将图片直接投递到UI线程，并且更新UI；
 * 3、如果没有缓存，则从对应的地方获取到图片，并且将图片缓存起来，然后再将结果投递给UI线程，更新UI；
 * 将部分变化的内容交给子类，比如从哪里加载图片都是子类完成的
 */
public abstract class AbsLoader implements Loader
{
	private static final String TAG = "AbsLoader";
	/**
	 * 图片缓存
	 */
	private static ImageCache mCache = ImageLoader.getInstance().getConfig().imageCache;

	@Override
	public void loadImage(BitmapRequest request)
	{
		//缓存通过BitmapRequest对象来得到Bitmap图像
		Bitmap resultBitmap = mCache.get(request);
		if(resultBitmap == null)
		{
			Log.e(Thread.currentThread().getName(), 
					"###### 没有缓存 :" + resultBitmap + ", uri = " + request.imageUri);
			//这里要加载“正在显示中”的图片
			showLoading(request);
			resultBitmap = onLoadImage(request);
			cacheBitmap(request, resultBitmap);
		}else
		{
			request.justCacheInMem = true;
		}
		//将结果传递到UI线程，更新ImageView
		deliveryToUIThread(request, resultBitmap);
	}
	
	/**
	 * 具体得到Bitmap图片的方法由子类来完成.
	 * 可以通过本地和网络的方式
	 */
	protected abstract Bitmap onLoadImage(BitmapRequest bitmapRequest);
	
	/**
	 * 将得到的Bitmap图片缓存起来 
	 * 缓存图片
	 */
	private void cacheBitmap(BitmapRequest request, Bitmap bitmap)
	{
		if(bitmap != null && mCache != null)
		{
			synchronized (mCache)
			{
				mCache.put(request, bitmap);
			}
		}
	}
	
	/**
	 * 显示加载中的视图,这里也要判断imageview的tag与image uri的想等性，否则逆序加载时出现问题
	 */
	protected void showLoading(final BitmapRequest request)
	{
		final ImageView imageView = request.getImageView();
		//以下条件判断用户是否有设置DisplayConfig
		if(request.isImageViewValid()
				&& hasLoadingPlaceholder(request.displayConfig))
		{
			//post主要的作用是，正在子线程进行操作，抛到UI线程上
			imageView.post(new Runnable()
			{
				@Override
				public void run()
				{
					//这个时候显示“加载中”的图片
					imageView.setImageResource(request.displayConfig.loadingResId);			
				}
			});
		}
	}
	
	/**
	 * 将得到的Bitmap传递到UI线程，并且更新ImageVIew
	 */
	protected void deliveryToUIThread(final BitmapRequest request,
			final Bitmap bitmap)
	{
		final ImageView imageView = request.getImageView();
		if(imageView == null)
		{
			return;
		}
		imageView.post(new Runnable()
		{	
			@Override
			public void run()
			{
				updateImageView(request, bitmap);
			}
		});
	}
	
	/**
	 * 更新ImageView
	 */
	private void updateImageView(BitmapRequest request, Bitmap bitmap)
	{
		final ImageView imageView = request.getImageView();
		final String uri = request.imageUri;
		if(bitmap != null && imageView.getTag().equals(uri))
		{
			imageView.setImageBitmap(bitmap);
		}
		
		//加载失败
		if(bitmap == null && hasFaildPlaceholder(request.displayConfig))
		{
			Log.d(TAG, "图片加载失败");
			imageView.setImageResource(request.displayConfig.failedResId);
		}
		
		//回调接口，如果用户设置了该类继承了Listener接口，就执行该逻辑
		if(request.imageListener != null)
		{
			request.imageListener.onComplete(imageView, bitmap, uri);
		}
	}

	private boolean hasLoadingPlaceholder(DisplayConfig displayConfig)
	{
		return displayConfig != null && displayConfig.loadingResId > 0;
	}
	
	private boolean hasFaildPlaceholder(DisplayConfig displayConfig)
	{
		return displayConfig != null && displayConfig.failedResId > 0;
	}

}
