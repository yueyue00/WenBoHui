package com.gheng.exhibit.utils;

import android.os.Bundle;

/**
 *
 *日志拦截器
 * @author lileixing
 */
public class WebViewLogInterceptor {

	public void logToServer(Bundle bd){
		if(bd.getBoolean("log")){
			int browsetype = bd.getInt("browsetype");
			int type = bd.getInt("type");
			int objId = bd.getInt("objid");
			int searchtype = Constant.SERACH_TYPE_ENTER;
//			ApiUtil.postBrowseLog(browsetype, objId, type, searchtype, null);
		}
	}
	
}
