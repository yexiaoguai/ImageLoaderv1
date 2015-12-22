package com.yeliang.photogalleryactivity.activity;

import android.app.Fragment;
import android.util.Log;

import com.yeliang.photogalleryactivity.R;
import com.yeliang.photogalleryactivity.cache.MemoryCache;
import com.yeliang.photogalleryactivity.config.ImageLoaderConfig;
import com.yeliang.photogalleryactivity.core.ImageLoader;
import com.yeliang.photogalleryactivity.fragment.PhotoGalleryFragment;

public class PhotoGalleryActivity extends SingleFragmentActivity
{
	private static final String TAG = "PhotoGalleryActivity";
	private ImageLoader imageLoader;

	@Override
	protected Fragment createFragment()
	{
		Log.d(TAG, "PhotoGalleryActivity--createFragment()");
		initImageLoader();
		
		return new PhotoGalleryFragment();
	}

	private void initImageLoader()
	{
		ImageLoaderConfig config = new ImageLoaderConfig.Builder().setCache(new MemoryCache())
				.setLoadingPlaceholder(R.drawable.loading)
				.setNotFoundPlaceholder(R.drawable.not_found)
				.setThreadCount(4)
				.create();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);	
	}
	
	@Override
	protected void onDestroy()
	{
		imageLoader.stop();
		super.onDestroy();
	}
	
	public final static String[] imageThumbUrls = new String[] 
	{
        //"http://t1.27270.com/uploads/tu/201507/382/slt.jpg",
        //"http://t1.27270.com/uploads/tu/201503/1054/slt.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
        //"http://t1.27270.com/uploads/150619/7-150619163IV33.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
        "http://img.my.csdn.net/uploads/201407/26/not_found_haha.jpg", // 不存在的图片
        "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg",
        //"http://t1.27270.com/uploads/tu/201508/01/slt.jpg",
        //"http://m.xxxiao.com/wp-content/uploads/sites/3/2015/04/m.xxxiao.com_60062ba95a4dd80a711e82cdf57dc5ab-760x500.jpg",
        //"http://t1.27270.com/uploads/tu/201508/03/slt.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383130_7393.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383129_8813.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383100_3554.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383093_7894.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383092_2432.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383092_3071.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383091_3119.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383059_6589.jpg",
        "http://img.my.csdn.net/uploads/201407/26/1406383059_8814.jpg",
        "http://b.hiphotos.baidu.com/zhidao/pic/item/a6efce1b9d16fdfafee0cfb5b68f8c5495ee7bd8.jpg",
        "http://pic47.nipic.com/20140830/7487939_180041822000_2.jpg",
        "http://pic41.nipic.com/20140518/4135003_102912523000_2.jpg",
        "http://img2.imgtn.bdimg.com/it/u=1133260524,1171054226&fm=21&gp=0.jpg",
        "http://h.hiphotos.baidu.com/image/pic/item/3b87e950352ac65c0f1f6e9efff2b21192138ac0.jpg",
        "http://pic42.nipic.com/20140618/9448607_210533564001_2.jpg",
        "http://pic10.nipic.com/20101027/3578782_201643041706_2.jpg",
        "http://picview01.baomihua.com/photos/20120805/m_14_634797817549375000_37810757.jpg",
        "http://img2.3lian.com/2014/c7/51/d/26.jpg",
        "http://img3.3lian.com/2013/c1/34/d/93.jpg",
        "http://b.zol-img.com.cn/desk/bizhi/image/3/960x600/1375841395686.jpg",
        "http://picview01.baomihua.com/photos/20120917/m_14_634834710114218750_41852580.jpg",
        "http://cdn.duitang.com/uploads/item/201311/03/20131103171224_rr2aL.jpeg",
        "http://imgrt.pconline.com.cn/images/upload/upc/tx/wallpaper/1210/17/c1/spcgroup/14468225_1350443478079_1680x1050.jpg",
        "http://pic41.nipic.com/20140518/4135003_102025858000_2.jpg",
        "http://www.1tong.com/uploads/wallpaper/landscapes/200-4-730x456.jpg",
        "http://pic.58pic.com/58pic/13/00/22/32M58PICV6U.jpg",
        "http://picview01.baomihua.com/photos/20120629/m_14_634765948339062500_11778706.jpg",
        "http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=429e7b1b92ef76c6d087f32fa826d1cc/7acb0a46f21fbe09cc206a2e69600c338744ad8a.jpg",
        "http://pica.nipic.com/2007-12-21/2007122115114908_2.jpg",
        "http://cdn.duitang.com/uploads/item/201405/13/20140513212305_XcKLG.jpeg",
        "http://photo.loveyd.com/uploads/allimg/080618/1110324.jpg",
        "http://img4.duitang.com/uploads/item/201404/17/20140417105820_GuEHe.thumb.700_0.jpeg",
        "http://cdn.duitang.com/uploads/item/201204/21/20120421155228_i52eX.thumb.600_0.jpeg",
        "http://img4.duitang.com/uploads/item/201404/17/20140417105856_LTayu.thumb.700_0.jpeg",
        "http://img04.tooopen.com/images/20130723/tooopen_20530699.jpg",
        "http://www.qjis.com/uploads/allimg/120612/1131352Y2-16.jpg",
        "http://pic.dbw.cn/0/01/33/59/1335968_847719.jpg",
        "http://a.hiphotos.baidu.com/image/pic/item/a8773912b31bb051a862339c337adab44bede0c4.jpg",
        "http://h.hiphotos.baidu.com/image/pic/item/f11f3a292df5e0feeea8a30f5e6034a85edf720f.jpg",
        "http://img0.pconline.com.cn/pconline/bizi/desktop/1412/ER2.jpg",
        "http://pic.58pic.com/58pic/11/25/04/91v58PIC6Xy.jpg",
        "http://img3.3lian.com/2013/c2/32/d/101.jpg",
        "http://pic25.nipic.com/20121210/7447430_172514301000_2.jpg",
        "http://img02.tooopen.com/images/20140320/sy_57121781945.jpg",
        "http://www.renyugang.cn/emlog/content/plugins/kl_album/upload/201004/852706aad6df6cd839f1211c358f2812201004120651068641.jpg"
    };

}
