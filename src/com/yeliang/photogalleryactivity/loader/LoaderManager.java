package com.yeliang.photogalleryactivity.loader;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据不同的schema得到不同的loader，是单例模式
 * 该类通过hashMap保存了<Schema, Loader>键值对
 */
public class LoaderManager
{
	public static final String HTTP = "http";
	public static final String HTTPS = "https";
	public static final String FILE = "file";
	
	private Map<String, Loader> mLoaderMap = new HashMap<String, Loader>();
	
	private Loader mNullLoader = new NullLoader();
	
	private static LoaderManager INSTANCE;
	
	private LoaderManager()
	{
		register(HTTP, new UrlLoader());
		register(HTTPS, new UrlLoader());
		register(FILE, new LocalLoader());
	}
	
	/**
	 * 单例模式，构造方法将<schema, loader>键值对加入map
	 */
	public static LoaderManager getInstance()
	{
		if(INSTANCE == null)
		{
			synchronized (LoaderManager.class)
			{
				if(INSTANCE == null)
				{
					INSTANCE = new LoaderManager();
				}
			}
		}
		return INSTANCE;
	}
	
	public final synchronized void register(String schema, Loader loader)
	{
		mLoaderMap.put(schema, loader);
	}
	
	/**
	 * 通过schema获取到loader的不同实例，也就是说根据Http和file不同Load方式
	 */
	public Loader getLoader(String schema)
	{
		if(mLoaderMap.containsKey(schema))
		{
			return mLoaderMap.get(schema);
		}
		return mNullLoader;
	}
	
}
