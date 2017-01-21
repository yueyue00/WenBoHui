package com.gheng.exhibit.database.task;

import android.os.AsyncTask;

import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.model.databases.Company;
import com.gheng.exhibit.model.databases.Product;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;

/**
 *	数据库查询任务
 * @author lileixing
 */
public class ProductInfoTask extends AsyncTask<Void, Void, Product > {

	private CallBack<Product> callBack;
	
	private long id;
	
	public ProductInfoTask(long id,CallBack<Product> callBack){
		this.callBack = callBack;
		this.id = id;
	}
	
	@Override
	protected Product doInBackground(Void... params) {
		if(callBack == null)
			return null;
		Product product = null;
		try {
			product = BaseActivity.getDbUtils().findById(Product.class, id);
		} catch (DbException e) {
			return null;
		}
		if(product != null){
			Long companyid = product.getCompanyid();
			if(companyid != null){
				try {
					Company company = BaseActivity.getDbUtils().findById(Company.class, companyid);
					product.company = company;
				} catch (DbException e) {
					e.printStackTrace();
					product.company = null;
				}
				
			}
		}
		return product;
	}
	
	
	@Override
	protected void onPostExecute(Product result) {
		if(callBack == null)
			return;
		if(result == null){
			callBack.onFailure(new HttpException("数据库异常"), "数据库异常");
		}else {
			callBack.onSuccess(result);
		}
	}

}
