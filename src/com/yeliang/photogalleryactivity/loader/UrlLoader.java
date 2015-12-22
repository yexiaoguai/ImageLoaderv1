package com.yeliang.photogalleryactivity.loader;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import com.yeliang.photogalleryactivity.core.CloseUtils;
import com.yeliang.photogalleryactivity.request.BitmapRequest;
import com.yeliang.photogalleryactivity.utils.BitmapDecoder;

public class UrlLoader extends AbsLoader
{

	@Override
	protected Bitmap onLoadImage(BitmapRequest bitmapRequest)
	{
		final String imageUrl = bitmapRequest.imageUri;
		FileOutputStream fos = null;
		InputStream is = null;
		try
		{
			URL url = new URL(imageUrl);
			final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			is = new BufferedInputStream(conn.getInputStream());
			is.mark(is.available());
			
			final InputStream inputStream = is;
			//decodeStream有点特殊，因为用的输入流的方式，所以必须标记下用了mark()方法
            //这样第一次加载图片得到了图片实际的宽和高
            //接着才能reset()方法回到标记了的位置，这样一张图片输入了两次，第一次和第二次的目的不一样
			BitmapDecoder bitmapDecoder = new BitmapDecoder()
			{

				@Override
				public Bitmap decodeBitmapWithOpthion(Options options)
				{
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
					if(options.inJustDecodeBounds)
					{
						try
						{
							inputStream.reset();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}else
					{
						//关闭流
						conn.disconnect();
					}
					return bitmap;
				}	
			};
			return bitmapDecoder.decodeBitmap(bitmapRequest.getImageViewWidth(),
					bitmapRequest.getImageViewHeight());
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			CloseUtils.close(is);
			CloseUtils.close(fos);
		}
		return null;
	}

}
