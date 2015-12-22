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
	//SingleFragmentActivity�������ʵ�ָ÷�������һ����activity(����)�йܵ�fragmentʵ��
	protected abstract Fragment createFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "SingleFragmentActivity--onCreate()");
		
		setContentView(R.layout.activity_fragment);
		FragmentManager fm = getFragmentManager();
		//ʹ��������ͼ��ԴID��ȡfragment������ֻ�Ǹ�֪��Ƭ��λ�ã�û�еõ���Ƭ�Ƿ�Ϊnull
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if(fragment == null)
		{
			fragment = createFragment();
			//����һ���µ�fragment���񣬼���һ����Ӳ�����Ȼ���ύ������
        	//add������������Ϊ��������ͼ��ԴID���´�����Fragment(�������ṩ)
			fm.beginTransaction()
				.add(R.id.fragmentContainer, fragment)
				.commit();
		}
	}	
}
