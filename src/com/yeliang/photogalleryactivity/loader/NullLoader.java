package com.yeliang.photogalleryactivity.loader;

import android.graphics.Bitmap;
import android.util.Log;

import com.yeliang.photogalleryactivity.request.BitmapRequest;

public class NullLoader extends AbsLoader
{

	@Override
	protected Bitmap onLoadImage(BitmapRequest bitmapRequest)
	{
		Log.e(NullLoader.class.getSimpleName(), "###### wrong schema, your image uri is :" 
				+ bitmapRequest.imageUri);
		return null;
	}

}
