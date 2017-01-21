package com.hebg3.mxy.utils;

import android.os.AsyncTask;
import android.os.Message;

public class AsyncTaskForSwispLayoutRefreshStop extends AsyncTask<Object, Object, Object>{

	Message m;
	
	public AsyncTaskForSwispLayoutRefreshStop(Message m){
		this.m=m;
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		if((Integer)result==1){
			this.m.what=1000;
			this.m.sendToTarget();
		}
	}

}
