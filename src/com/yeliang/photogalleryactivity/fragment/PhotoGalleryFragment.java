package com.yeliang.photogalleryactivity.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.yeliang.photogalleryactivity.R;
import com.yeliang.photogalleryactivity.activity.PhotoGalleryActivity;
import com.yeliang.photogalleryactivity.core.ImageLoader;
import com.yeliang.photogalleryactivity.model.GalleryItem;

public class PhotoGalleryFragment extends Fragment
{
	private static final String TAG = "PhotoGalleryFragment";
	
	GridView mGridView;
	ArrayList<GalleryItem> mItems;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//执行异步任务，通过百度得到imageUri
		//new BaiDuItemsTask().execute();
		Log.d(TAG, "onCreate()");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
		Log.d(TAG, "onCreateView()");
		
		mGridView = (GridView)view.findViewById(R.id.gridView);
		mGridView.setAdapter(new GalleryItemAdapter(
				PhotoGalleryActivity.imageThumbUrls));
		
		return view;
	}
	
	//private class BaiDuItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>>
	//{

	//	@Override
	//	protected ArrayList<GalleryItem> doInBackground(Void... params)
	//	{
	//		Log.d(TAG, "AAAAAAAAA" + new BaiDuFetchr().fetchItems().get(0).getPicUrl());
	//		return new BaiDuFetchr().fetchItems();
	//	}
		
	//	@Override
	//	protected void onPostExecute(ArrayList<GalleryItem> result)
	//	{
	//		mItems = result;
	//		mGridView.setAdapter(new GalleryItemAdapter(mItems));
	//	}	
	//}

	private class GalleryItemAdapter extends ArrayAdapter<String>
	{

		public GalleryItemAdapter(String[] imagethumburls)
		{
			super(getActivity(), 0, imagethumburls);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			//GalleryItem  galleryItem = getItem(position);
			//String picUri = galleryItem.getPicUrl();
			//Log.d(TAG, picUri);
			
			if(convertView == null)
			{
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.gallery_item, parent, false);
			}
			ImageView imageView = (ImageView)convertView
					.findViewById(R.id.gallery_item_imageView);
			
			//加载图片，用的是自己写的工具类
			ImageLoader.getInstance().displayImage(imageView, getItem(position));
			return convertView;
		}		
	}

}
