package com.yeliang.photogalleryactivity.policy;

import com.yeliang.photogalleryactivity.request.BitmapRequest;

/**
 * 加载策略接口
 */
public interface LoadPolicy
{
	public int compare(BitmapRequest request1, BitmapRequest request2);
}
