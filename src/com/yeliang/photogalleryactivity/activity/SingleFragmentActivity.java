package com.yeliang.photogalleryactivity.activity;

import com.yeliang.photogalleryactivity.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

public abstract class SingleFragmentActivity extends Activity
{
	private static final String TAG = "SingleFragmentActivity";
	//SingleFragmentActivity的子类会实现该方法返回一个由activity(子类)托管的fragment实例
	protected abstract Fragment createFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "SingleFragmentActivity--onCreate()");
		
		setContentView(R.layout.activity_fragment);
		FragmentManager fm = getFragmentManager();
		//使用容器视图资源ID获取fragment，但是只是告知碎片的位置，没有得到碎片是否为null
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if(fragment == null)
		{
			fragment = createFragment();
			//创建一个新的fragment事务，加入一个添加操作，然后提交该事务
        	//add方法两个参数为：容器视图资源ID和新创建的Fragment(由子类提供)
			fm.beginTransaction()
				.add(R.id.fragmentContainer, fragment)
				.commit();
		}
	}	
}
