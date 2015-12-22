package com.yeliang.photogalleryactivity.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

/**
 * BitmapDecoder是一个按ImageView尺寸加载图片的辅助类
 * 封装先加载图片bound，计算出inSmallSize之后再加载图片的逻辑操作
 * BitmapDecoder类使用decodeBitmap方法封装了这个过程 ( 模板方法噢 ),用户只需要实现一个子类，
 * 并且覆写BitmapDecoder的decodeBitmapWithOption实现图片加载即可完成这个过程
 */
public abstract class BitmapDecoder
{
	/**
	 * 此方法抽象是因为加载图片的方式不一样，有的是通过Resource有的是通过File
	 */
	public abstract Bitmap decodeBitmapWithOpthion(Options options);
	
	/**
	 * @param width 图片的目标宽度
	 * @param height 图片的目标高度
	 * @return
	 */
	public Bitmap decodeBitmap(int width, int height)
	{
		//如果请求原图，则直接加载原图
		if(width <= 0 || height <= 0)
		{
			return decodeBitmapWithOpthion(null);
		}
		
		//1、获取只加载Bitmap宽高等数据的Option，即设置options.injustDecodeBounds = true;
		//把它设为true，那么BitmapFactory.decodeFile(String path, Options opt)并不会真的返回一个Bitmap给你，
		//它仅仅会把它的宽，高取回来给你，这样就不会占用太多的内存，也就不会那么频繁的发生OOM了
		BitmapFactory.Options options = getJustDecodeBuundsOptions();
		//2、通过options加载bitmap，此时返回的bitmap为空，数据将存储在options中，这次加载只是得出实际图片的宽和高
		decodeBitmapWithOpthion(options);
		//3、计算缩放比例，并且将options.inJustDecodeBounds设置为false;
		calculateInSmall(options, width, height);
		//4、通过options设置的缩放比例加载图片
		return decodeBitmapWithOpthion(options);	
	}

	private void calculateInSmall(Options options, int reqWidth, int reqHeight)
	{
		//设置采样率，也就是缩放比例
		options.inSampleSize = computeInSmallSize(options, reqWidth, reqHeight);
		
		Log.d(Thread.currentThread().getName(), "采样率sampleSize:" + options.inSampleSize + 
				", reqWidth = " + reqWidth + ", reqHeigh" + reqHeight);
		//图片质量
		options.inPreferredConfig = Config.RGB_565;
		//设置options.injustDecodeBounds = false;
		options.inJustDecodeBounds = false;
		//如果 inPurgeable 设为True的话表示使用BitmapFactory创建的Bitmap 
	    //用于存储Pixel的内存空间在系统内存不足时可以被回收， 
	    //在应用需要再次访问Bitmap的Pixel时（如绘制Bitmap或是调用getPixel）， 
	    //系统会再次调用BitmapFactory decoder重新生成Bitmap的Pixel数组。  
	    //为了能够重新解码图像，bitmap要能够访问存储Bitmap的原始数据。
		//下面两个参数要配合使用
		options.inPurgeable = true;
		options.inInputShareable = true;
	}

	private int computeInSmallSize(Options options, int reqWidth, int reqHeight)
	{
		//图片原始的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		Log.d(Thread.currentThread().getName(), "图片原始高度:" + height + ", 图片原始宽度:" + width);
		int inSampleSize = 1;
		//图片的原始宽度和高度大于ImageView的宽度和高度
		if(height > reqHeight || width > reqWidth)
		{
			final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            //通过下列判断得到采样率
            //假如采样率为2，将实际的宽和高除以2，比较是否小于ImageView的宽和高
            //小的话就说明原始的图片可以直接加载进ImageView
            //大的话还需要乘以2，接着把实际的宽和高除以4再进行判断，直到比ImageView的宽和高小.
            while((halfHeight / inSampleSize) >= reqHeight
                  && (halfWidth / inSampleSize) >= reqWidth)
            {
                inSampleSize *= 2;
            }
		}
        return inSampleSize;
	}

	private Options getJustDecodeBuundsOptions()
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 设置为true,表示解析Bitmap对象，该对象不占内存
		options.inJustDecodeBounds = true;
		return options;
	}
	
}
