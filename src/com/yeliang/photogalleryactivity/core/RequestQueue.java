package com.yeliang.photogalleryactivity.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import android.util.Log;

import com.yeliang.photogalleryactivity.request.BitmapRequest;

/**
 * 请求队列，使用优先队列，使得请求可以按照优先级进行处理.
 * mSerialNumGenerator会给每一个请求分配一个序列号.
 * PriorityBlockingQueue会根据BitmapRequest的compare策略来决定BitmapRequest的顺序.
 * RequestQueue内部会启动用户指定数量的线程来从请求队列中读取请求，
 * 分发线程不断地从队列中读取请求，然后进行图片加载处理.
 * mDispatcherNums就是线程的数量，通过在ImageLoader对象init()初始化中将用户配置的数量加载过来
 */
public class RequestQueue
{
	private static final String TAG = "RequestQueue";
	/**
	 * 请求队列
	 * 该队列根据优先级进行排序处理
	 */
	private BlockingQueue<BitmapRequest> mRequestQueu = new PriorityBlockingQueue<BitmapRequest>();
	
	/**
	 * 请求的序列化生成器
	 */
	private AtomicInteger mSerialNumGenerator = new AtomicInteger();
	
	/**
	 * 默认的核心数
	 */
	public static int DEFAULT_CORE_NUMS = Runtime.getRuntime().availableProcessors() + 1;
	
	/**
	 * CPU核心数 + 1个分发线程数
	 */
	private int mDispatcherNums = DEFAULT_CORE_NUMS;
	
	/**
	 * NetworkExecutor,执行网络请求的线程
	 */
	private RequestDispatcher[] mDispatchers = null;
	
	/**
	 * 构造函数，都是调用RequestQueue(int coreNums)
	 * 默认设置的数量为 CPU核心数 + 1个分发线程数
	 */
	protected RequestQueue()
	{
		this(DEFAULT_CORE_NUMS);
	}
	
	/**
	 * coreNums 线程核心数
	 */
	protected RequestQueue(int coreNums)
	{
		mDispatcherNums = coreNums;
	}
	
	/**
	 * 为每个请求生成一个序列号
	 */
	private int generateSerialNumber()
	{
		return mSerialNumGenerator.incrementAndGet();
	}
	
	/**
	 * 启动RequestDispatcher
	 */
	private final void startDispatchers()
	{
		//根据构造函数设置的数量，设置多少个执行网络请求的线程
		mDispatchers = new RequestDispatcher[mDispatcherNums];
		for(int i = 0; i < mDispatcherNums; i++)
		{
			Log.d(TAG, "#### 启动线程 " + i);
			mDispatchers[i] = new RequestDispatcher(mRequestQueu);
			//线程开启执行run()方法
			mDispatchers[i].start();
		}
	}
	
	/**
	 * 开启线程.start();
	 */
	public void start()
	{
		stop();
		startDispatchers();
	}
	
	/**
	 * 停止RequestDispatcher，就是中断线程
	 */
	public void stop()
	{
		if(mDispatchers != null && mDispatchers.length > 0)
		{
			for(int i = 0; i < mDispatchers.length; i++)
			{
				mDispatchers[i].interrupt();
			}
		}
	}
	
	/**
	 *不能重复添加请求 
	 */
	public void addRequest(BitmapRequest request)
	{
		if(!mRequestQueu.contains(request))
		{
			//队列不包含该BitmapRequest请求
			//为该BitmapRequest添加一个序列号
			request.serialNum = this.generateSerialNumber();
			//加入队列
			mRequestQueu.add(request);
		}else
		{
			Log.d(TAG, "### 请求队列中已经含有该BitmapRequest");
		}
	}
	
	public void clear()
	{
		mRequestQueu.clear();
	}
	
	public BlockingQueue<BitmapRequest> getAllRequest()
	{
		return mRequestQueu;
	}

}
