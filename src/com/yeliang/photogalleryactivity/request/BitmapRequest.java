package com.yeliang.photogalleryactivity.request;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import android.widget.ImageView;

import com.yeliang.photogalleryactivity.config.DisplayConfig;
import com.yeliang.photogalleryactivity.core.ImageLoader;
import com.yeliang.photogalleryactivity.core.ImageLoader.ImageListener;
import com.yeliang.photogalleryactivity.policy.LoadPolicy;
import com.yeliang.photogalleryactivity.utils.ImageViewHelper;
import com.yeliang.photogalleryactivity.utils.Md5Helper;

/**
 * 网络请求类. 注意GET和DELETE不能传递参数,因为其请求的性质所致,用户可以将参数构建到url后传递进来到Request中.
 * BitmapRequest只是一个存储了ImageView、图片uri、DisplayConfig以及ImageListener的一个对象.
 * 封装这个对象的目的在加载图片时减少参数的个数，在BitmapRequest的构造函数中我们会将图片uri设置为ImageView的tag，
 * 这样做的目的是防止图片错位显示.
 */
public class BitmapRequest implements Comparable<BitmapRequest>
{
	Reference<ImageView> mImageViewRef;
	public DisplayConfig displayConfig;
	public ImageListener imageListener;
	public String imageUri = "";
	public String imageUriMd5 = "";
	
	/**
	 * 请求序列号
	 */
	public int serialNum = 0;
	
	/**
	 * 是否取消该请求
	 */
	public boolean isCancel = false;
	
	public boolean justCacheInMem = false;
	
	/**
	 * 加载策略
	 */
	LoadPolicy mLoadPolicy = ImageLoader.getInstance().getConfig().loadPolicy;
	
	/**
	 * 构造方法，将配置信息初始化.
	 * 在BitmapRequest的构造函数中我们会将图片uri设置为ImageView的tag，
	 * 这样做的目的是防止图片错位显示.
	 * 
	 */
	public BitmapRequest(ImageView imageView, String uri, DisplayConfig config,
			ImageListener listener)
	{
		//弱引用，当imageView = null;的时候，ImageView对象会被GC回收，而不会造成内存泄露
		mImageViewRef = new WeakReference<ImageView>(imageView);
		displayConfig = config;
		imageListener = listener;
		imageUri = uri;
		imageView.setTag(uri);
		imageUriMd5 = Md5Helper.toMD5(imageUri);
	}
	
	/**
	 * 配置策略
	 * BitmapRequest类实现了Compare接口，请求队列会根据它的序列号进行排序，
	 * 排序策略用户也可以通过配置类来设置.
	 */
	public void setLoadPolicy(LoadPolicy policy)
	{
		if(policy != null)
		{
			mLoadPolicy = policy;
		}
	}
	
	/**
	 * 判断imageView的tag与uri是否相等
	 */
	public boolean isImageViewValid()
	{
		return mImageViewRef.get() != null ? mImageViewRef.get().getTag().equals(imageUri) : false;
 	}
	
	public ImageView getImageView()
	{
		return mImageViewRef.get();
	}
	
	public int getImageViewWidth()
	{
		return ImageViewHelper.getImageViewWidth(mImageViewRef.get());
	}
	
	public int getImageViewHeight()
	{
		return ImageViewHelper.getImageViewHeight(mImageViewRef.get());
	}

	@Override
	public int compareTo(BitmapRequest another)
	{
		return mLoadPolicy.compare(this, another);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((imageUri == null) ? 0 : imageUri.hashCode());
		result = prime * result
				+ ((mImageViewRef == null) ? 0 : mImageViewRef.hashCode());
		result = prime * result + serialNum;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BitmapRequest other = (BitmapRequest) obj;
		if (imageUri == null)
		{
			if (other.imageUri != null)
				return false;
		} else if (!imageUri.equals(other.imageUri))
			return false;
		if (mImageViewRef == null)
		{
			if (other.mImageViewRef != null)
				return false;
		} else if (!mImageViewRef.equals(other.mImageViewRef))
			return false;
		if (serialNum != other.serialNum)
			return false;
		return true;
	}
	
}
