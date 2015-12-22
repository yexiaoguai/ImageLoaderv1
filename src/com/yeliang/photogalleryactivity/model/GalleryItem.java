package com.yeliang.photogalleryactivity.model;

public class GalleryItem
{
	private String mTitle;
	private String mDescription;
	private String mPicUrl;
	private String mUrl;
	
	public String toString()
	{
		return mTitle;
	}
	
	public String getTitle()
	{
		return mTitle;
	}
	
	public void setTitle(String title)
	{
		mTitle = title;
	}
	
	public String getDescription()
	{
		return mDescription;
	}
	
	public void setDescription(String description)
	{
		mDescription = description;
	}
	
	public String getPicUrl()
	{
		return mPicUrl;
	}
	
	public void setPicUrl(String picUrl)
	{
		mPicUrl = picUrl;
	}
	
	public String getUrl()
	{
		return mUrl;
	}
	
	public void setUrl(String url)
	{
		mUrl = url;
	}

}
