package com.gheng.exhibit.http;

import com.gheng.exhibit.utils.AppTools;
import com.lidroid.xutils.exception.HttpException;

/**
 *
 * @author zhao
 */
public abstract class CallBack<T>{
	
    public void onStart() {
    }

    public void onCancelled() {
    }

    public void onLoading(long total, long current, boolean isUploading) {
    }
    
    Class<T> getGenericClass(){
    	return (Class<T>) AppTools.getGenericClass(getClass());
    }

    public abstract void onSuccess(T entity);

    public abstract void onFailure(HttpException error, String msg);
}
