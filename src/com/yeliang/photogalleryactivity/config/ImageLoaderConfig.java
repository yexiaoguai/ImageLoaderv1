package com.yeliang.photogalleryactivity.config;

import com.yeliang.photogalleryactivity.cache.ImageCache;
import com.yeliang.photogalleryactivity.cache.MemoryCache;
import com.yeliang.photogalleryactivity.policy.LoadPolicy;
import com.yeliang.photogalleryactivity.policy.SerialPolicy;

/**
 * ImageLoader配置类,只有通过Bulider内部类create()方法才能创建ImageLoaderConfig的实例
 */
public class ImageLoaderConfig
{
	/**
	 * 图片缓存对象
	 */
	public ImageCache imageCache = new MemoryCache();
	
	/**
	 * 加载时的图片和加载出错时的图片配置对象
	 */
	public DisplayConfig displayConfig = new DisplayConfig();
	
	/**
	 * 加载策略
	 */
	public LoadPolicy loadPolicy = new SerialPolicy();
	
	/**
	 * 线程数量 cpu数量加1
	 */
	public int threadCount = Runtime.getRuntime().availableProcessors() + 1;
	
	private ImageLoaderConfig()
	{
	}
	
	/**
	 *ImageLoaderConfig的内部类Builder
	 */
	public static class Builder
	{
		ImageCache imageCache = new MemoryCache();
		
		DisplayConfig displayConfig = new DisplayConfig();
		
		LoadPolicy loadPolicy = new SerialPolicy();

		int threadCount = Runtime.getRuntime().availableProcessors() + 1;
		
		/**
		 * 设置线程数量
		 */
		public Builder setThreadCount(int count)
		{
			threadCount = Math.max(1, count);
			return this;
		}
		/**
		 *设置缓存
		 */
		public Builder setCache(ImageCache cache)
		{
			imageCache = cache;
			return this;
		}
		/**
		 *设置图片加载中显示的图片ID
		 */
		public Builder setLoadingPlaceholder(int resId)
		{
			displayConfig.loadingResId = resId;
			return this;
		}
		/**
		 * 设置加载图片失败时候显示的图片ID
		 */
		public Builder setNotFoundPlaceholder(int resId)
		{
			displayConfig.failedResId = resId;
			return this;
		}
		
		public Builder setLoadPolicy(LoadPolicy policy)
		{
			if(policy != null)
			{
				loadPolicy = policy;
			}
			return this;
		}
		
		void applyConfig(ImageLoaderConfig config)
		{
			config.imageCache = this.imageCache;
			config.displayConfig = this.displayConfig;
			config.loadPolicy = this.loadPolicy;
			config.threadCount = this.threadCount;	
		}
		
		/**
		 * 根据已经设置好的属性创建配置对象
		 */
		public ImageLoaderConfig create()
		{
			ImageLoaderConfig config = new ImageLoaderConfig();
			//应用配置
			applyConfig(config);
			return config;
		}	
	}
}