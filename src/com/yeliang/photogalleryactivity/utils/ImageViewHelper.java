package com.yeliang.photogalleryactivity.utils;

import java.lang.reflect.Field;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 这个类就是要得到ImageView的宽和高
 * 如果ImageView是Match_parent，就直接width = imageView.getWidth();
 * 如果ImageView是WRAP_CONTENT，就通过反射的方式得到mMaxWidth字段的值
 */
public class ImageViewHelper
{
	private static final String TAG = "ImageViewHelper";
	
	private static int DEFAULT_WIDTH = 200;
	private static int DEFAULT_HEIGHT = 200;
	
	/**
	 * 其实这个得到的宽度就是ImageView的Match_parent，或者是ViewGroup的宽度
	 */
	public static int getImageViewWidth(ImageView imageView)
	{
		if(imageView != null)
		{
			final ViewGroup.LayoutParams params = imageView.getLayoutParams();
			int width = 0;
			if(params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT)
			{
				//Match_parent
				width = imageView.getWidth();
				Log.d(Thread.currentThread().getName(), "imageView.getWidth():" + width);
			}
			if(width <= 0 && params != null)
			{
				//WRAP_CONTENT
				width = params.width;
				Log.d(Thread.currentThread().getName(), "params.width:" + width);
			}
			//如果上面的成立，就是WRAP_CONTENT，下面也会成立
			if(width <= 0)
			{
				width = getImageViewFieldValue(imageView, "mMaxWidth");
				Log.d(Thread.currentThread().getName(), "getImageViewFieldValue:" + width);
			}
			Log.d(Thread.currentThread().getName(), "最终得出的目标width:" + width);
			return width;
		}
		return DEFAULT_WIDTH;
	}
	
	public static int getImageViewHeight(ImageView imageView)
	{
		if(imageView != null)
		{
			final ViewGroup.LayoutParams params = imageView.getLayoutParams();
			int height = 0;
			if(params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT)
			{
				height = imageView.getHeight();
			}
			if(height <= 0 && params != null)
			{
				height = params.height;
			}
			if(height <= 0)
			{
				height = getImageViewFieldValue(imageView, "mMaxHeight");
			}
			return height;
		}	
		return DEFAULT_HEIGHT;
	}

	/**
	 * 运用反射的方式得到字段(field)mMaxHeight的值，将其返回
	 */
	private static int getImageViewFieldValue(ImageView imageView, String fieldName)
	{
		int value = 0;
		try
		{
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer)field.get(imageView);
			Log.d(TAG, "fieldValue:" + fieldValue);
			if(fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
			{
				value = fieldValue;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return value;
	}

}
