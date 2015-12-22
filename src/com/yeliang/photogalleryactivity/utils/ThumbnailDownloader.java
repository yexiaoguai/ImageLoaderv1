package com.yeliang.photogalleryactivity.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

/**
 * ͬ������Ϣѭ���ĺ�̨�߳�
 */
public class ThumbnailDownloader<Token> extends HandlerThread
{
	private static final String TAG = "ThumbnailDownloader";
	private static final int MESSAGE_DOWNLOAD = 0;
	
	Handler mHandler;
	Map<Token, String> requestMap = Collections
			.synchronizedMap(new HashMap<Token, String>());
	Handler mResponseHandler;
	Listener<Token> mListener;
	
	public interface Listener<Token>
	{
		void onThumbnaiDownloaded(Token token, Bitmap thumbnail);
	}
	
	public void setListener(Listener<Token> listener)
	{
		mListener = listener;
	}

	public ThumbnailDownloader(Handler responseHandler)
	{
		super(TAG);
		mResponseHandler = responseHandler;
	}
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onLooperPrepared()
	{
		mHandler = new Handler()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg)
			{
				if(msg.what == MESSAGE_DOWNLOAD)
				{
					//�õ�token
					Token token = (Token)msg.obj;
					Log.d(TAG, "Got a request for url: " + requestMap.get(token));
					handleRequest(token);
				}
			}	
		};
	}
	
	/**
	 * ��ArrayAdapter��getView�л���ø÷���
	 * mThumbnailThread.queueThumbnail(imageView, url);
	 * ImageView����Token
	 */
	public void queueThumbnail(Token token, String url)
	{
		Log.d(TAG, "Got an URL: " + url);
		requestMap.put(token, url);
		
		mHandler.obtainMessage(MESSAGE_DOWNLOAD, token).sendToTarget();
	}
	
	private void handleRequest(final Token token)
	{
		final String url = requestMap.get(token);
		if(url == null)
		{
			return;
		}
			
		try
		{
			URL imageUrl = new URL(url);
			final HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
			final Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
			conn.disconnect();
			Log.d(TAG, "Bitmap created");
			
			mResponseHandler.post(new Runnable()
			{
				
				@Override
				public void run()
				{
					if(requestMap.get(token) != url)
					{
						return;
					}
					requestMap.remove(token);
					mListener.onThumbnaiDownloaded(token, bitmap);
				}
			});
		}catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	public void clearQueue()
	{
		mHandler.removeMessages(MESSAGE_DOWNLOAD);
		requestMap.clear();
	}

}
