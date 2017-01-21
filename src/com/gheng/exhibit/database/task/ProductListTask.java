package com.gheng.exhibit.database.task;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.gheng.exhibit.http.CallBack;
import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.model.databases.Company;
import com.gheng.exhibit.model.databases.DataBaseHelper;
import com.gheng.exhibit.model.databases.OrderBy;
import com.gheng.exhibit.model.databases.Product;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.view.BaseActivity;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.HttpException;

/**
 *	产品列表
 * @author lileixing
 */
public class ProductListTask extends AsyncTask<Void, Void, PageBody<Product>> {

	private Selector selector;
	
	private int pageSize;
	
	private int pageNo;
	
	private List<OrderBy> orderList;
	
	private CallBack<PageBody<Product>> callBack;
	
	public ProductListTask(final Selector selector,final int pageNo,final int pageSize,final List<OrderBy> orderList,CallBack<PageBody<Product>> callBack){
		this.selector = selector;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.orderList = orderList;
		this.callBack = callBack;
	}
	
	public ProductListTask(final Selector selector,final int pageNo,CallBack<PageBody<Product>> callBack){
		this(selector, pageNo, Constant.PAGE_SIZE, null, callBack);
	}
	
	@Override
	protected PageBody<Product> doInBackground(Void... params) {
		if(callBack == null)
			return null;
		PageBody<Product> page = new DataBaseHelper(BaseActivity.getDbUtils()).pageQuery(selector, pageNo, pageSize, orderList);
		List<Product> rdata = page.rdata;
		List<Long> idStr = new ArrayList<Long>();
		if(AppTools.isNotBlack(rdata)){
			for (Product product : rdata) {
				if(product.getCompanyid() != null){
					idStr.add(product.getCompanyid());
				}
			}
		}
		Selector tSelector = Selector.from(Company.class);
		ApiUtil.changeSelector(tSelector);
		if(idStr.size() > 0){
			tSelector.and("id", "in", idStr);
		}
		List<Company> findAll = null;
		try {
			findAll = BaseActivity.getDbUtils().findAll(tSelector);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error........................................");
			return null;
		}
		if(AppTools.isNotBlack(findAll)){
			for (Product product : rdata) {
				if(product.getCompanyid() != null){
					Company company = findCompanyById(findAll,product.getCompanyid());
					product.company = company;
				}
			}
		}
		return page;
	}
	
	private Company findCompanyById(List<Company> list,long id){
		for (Company company : list) {
			if(company.getId() == id){
				return company;
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(PageBody<Product> result) {
		if(result == null){
			callBack.onFailure(new HttpException("数据库异常"), "数据库异常");
		}else {
			callBack.onSuccess(result);
		}
	}

}
