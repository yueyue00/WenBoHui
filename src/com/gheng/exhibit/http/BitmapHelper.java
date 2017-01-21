package com.gheng.exhibit.http;

import android.content.Context;

import com.gheng.exhibit.utils.Constant;
import com.lidroid.xutils.BitmapUtils;
import com.smartdot.wenbo.huiyi.R;


/**
 * 对xutils的BitmapUtil进行二次封装
 * @author zhao
 */
public class BitmapHelper {
	
	private static BitmapUtils bitmapUtils;

	public static BitmapUtils createBitmapUtils(Context context){
		if(bitmapUtils == null){
			bitmapUtils = new BitmapUtils(context, Constant.CACHE_DIR);
			bitmapUtils.configDefaultBitmapMaxSize(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
			bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
			bitmapUtils.configDefaultConnectTimeout(60000);
			bitmapUtils.configDefaultReadTimeout(60000);
		}
		return bitmapUtils;
	}
	
}
