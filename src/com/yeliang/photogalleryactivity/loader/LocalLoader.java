package com.yeliang.photogalleryactivity.loader;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.util.Log;

import com.yeliang.photogalleryactivity.request.BitmapRequest;
import com.yeliang.photogalleryactivity.utils.BitmapDecoder;

public class LocalLoader extends AbsLoader
{
	private static final String TAG = "LocalLoader";

	@Override
	protected Bitmap onLoadImage(BitmapRequest bitmapRequest)
	{
		//得到本地路径
		final String imagePath = Uri.parse(bitmapRequest.imageUri).getPath();
		Log.d(TAG, "imagePath:" + imagePath);
		final File imgFile = new File(imagePath);
		if(!imgFile.exists())
		{
			Log.d(TAG, "####图片不存在");
			return null;
		}
		
		//从sd卡中加载的图片仅缓存到内存中，不做本地缓存
		bitmapRequest.justCacheInMem = true;
		//其实没有加载图片,得到图片的宽和高
		BitmapDecoder decoder = new BitmapDecoder()
		{
			
			@Override
			public Bitmap decodeBitmapWithOpthion(Options options)
			{
				Log.d(TAG, "测试执行顺序---2");
				return BitmapFactory.decodeFile(imagePath, options);
			}
		};
		Log.d(TAG, "测试执行顺序---1");
		return decoder.decodeBitmap(bitmapRequest.getImageViewWidth(),
				bitmapRequest.getImageViewHeight());
	}

}
