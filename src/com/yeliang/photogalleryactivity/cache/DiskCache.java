package com.yeliang.photogalleryactivity.cache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.Log;

import com.yeliang.photogalleryactivity.disklrucache.DiskLruCache;
import com.yeliang.photogalleryactivity.disklrucache.DiskLruCache.Snapshot;
import com.yeliang.photogalleryactivity.disklrucache.IOUtil;
import com.yeliang.photogalleryactivity.request.BitmapRequest;
import com.yeliang.photogalleryactivity.utils.BitmapDecoder;
import com.yeliang.photogalleryactivity.utils.Md5Helper;

public class DiskCache implements ImageCache
{
	private static final String TAG = "DiskCache";
	/**
	 * 1MB
	 */
	private static final int MB = 1024 * 1024;
	
	/**
	 * cache dir
	 */
	private static final String IMAGE_DISK_CACHE = "bitmap";
	
	/**
	 * DIsk LRU Cache
	 */
	private DiskLruCache mDiskLruCache;
	
	/**
	 * Disk Cache Instance
	 */
	private static DiskCache mDiskCache;
	
	private DiskCache(Context context)
	{
		initDiskCache(context);
	}
	
	public static DiskCache getDiskCache(Context context)
	{
		if(mDiskCache == null)
		{
			synchronized (DiskCache.class)
			{
				if(mDiskCache == null)
				{
					mDiskCache = new DiskCache(context);
				}
			}
		}
		return mDiskCache;
	}
	
	/**
	 * 初始化sdcard缓存
	 */
	private void initDiskCache(Context context)
	{
		try
		{
			File cacheDir = getDiskCacheDir(context, IMAGE_DISK_CACHE);
			if(!cacheDir.exists())
			{
				cacheDir.mkdirs();
			}
			mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 50 * MB);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 获取sd缓存的目录,如果挂载了sd卡则使用sd卡缓存，否则使用应用的缓存目录
	 * @param context
	 * @param imageDiskCache 缓存目录名,比如bitmap
	 * @return
	 */
	private File getDiskCacheDir(Context context, String imageDiskCache)
	{
		String cachePath;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			Log.d(TAG, "###### context : " + context + ", dir = " 
					+ context.getExternalCacheDir().getPath());
			cachePath = context.getExternalCacheDir().getPath();
		}else
		{
			Log.d(TAG, "###### context : " + context + ", dir = " 
					+ context.getCacheDir().getPath());
			cachePath = context.getCacheDir().getPath();
		}
		Log.d(TAG, "path: " + cachePath + File.separator + imageDiskCache);
		return new File(cachePath + File.separator + imageDiskCache);
	}
	
	private int getAppVersion(Context context)
	{
		try
		{
			PackageInfo info = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;	
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return 1;
	}
	
	@Override
	public synchronized Bitmap get(final BitmapRequest key)
	{
		//图片解析器
		BitmapDecoder decoder = new BitmapDecoder()
		{
			
			@Override
			public Bitmap decodeBitmapWithOpthion(Options options)
			{
				final InputStream inputStream = getInputStream(key.imageUriMd5);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
				IOUtil.closeQuietly(inputStream);
				return bitmap;
			}
		};
		return decoder.decodeBitmap(key.getImageViewWidth(), 
				key.getImageViewHeight());
	}
	
	private InputStream getInputStream(String imageUriMd5)
	{
		Snapshot snapshot;
		try
		{	
			snapshot = mDiskLruCache.get(imageUriMd5);
			if(snapshot != null)
			{
				return snapshot.getInputStream(0);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * sd卡缓存只缓存从网络上下载的图片，本地图片不用缓存
	 */
	@Override
	public void put(BitmapRequest key, Bitmap value)
	{
		if(key.justCacheInMem)
		{
			Log.e(TAG, "######仅缓存在内存中");
			return;
		}
		DiskLruCache.Editor editor = null;
		try
		{
			editor = mDiskLruCache.edit(key.imageUriMd5);
			if(editor != null)
			{
				OutputStream outputStream = editor.newOutputStream(0);
				if(writeBitmapToDisk(value, outputStream))
				{
					//写入缓存
					editor.commit();
				}else
				{
					editor.abort();
				}
				IOUtil.closeQuietly(outputStream);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private boolean writeBitmapToDisk(Bitmap value, OutputStream outputStream)
	{
		BufferedOutputStream bos = new BufferedOutputStream(outputStream, 8 * 1024);
		value.compress(CompressFormat.JPEG, 100, bos);
		boolean result = true;
		try
		{
			bos.flush();
		} catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}finally
		{
			IOUtil.closeQuietly(bos);
		}
		return result;
	}

	@Override
	public void remove(BitmapRequest key)
	{
		try
		{
			mDiskLruCache.remove(Md5Helper.toMD5(key.imageUriMd5));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
