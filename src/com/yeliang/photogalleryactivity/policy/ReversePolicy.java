package com.yeliang.photogalleryactivity.policy;

import com.yeliang.photogalleryactivity.request.BitmapRequest;

/**
 * 逆序加载策略,即从最后加入队列的请求进行加载
 */
public class ReversePolicy implements LoadPolicy
{

	@Override
	public int compare(BitmapRequest request1, BitmapRequest request2)
	{
		return request2.serialNum - request1.serialNum;
	}

}
