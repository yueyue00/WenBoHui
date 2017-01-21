package com.gheng.exhibit.model.databases;

import java.util.List;

import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.utils.ApiUtil;
import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.LogUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

/**
 *
 * @author lileixing
 */
public class DataBaseHelper {

	private DbUtils dbUtil;
	
	public DataBaseHelper(DbUtils dbUtil){
		this.dbUtil = dbUtil;
	}
	
	public <T> PageBody<T> pageQuery(Selector selector,int pageNo,int pageSize,List<OrderBy> orderList){
		try {
			ApiUtil.changeSelector(selector);
			if(!dbUtil.getDatabase().isOpen())
				dbUtil = DbUtils.create(dbUtil.getDaoConfig());
			long count = dbUtil.count(selector);
			if(orderList != null && orderList.size() > 0){
				for (OrderBy orderBy : orderList) {
					selector.orderBy(orderBy.getColumnName(),orderBy.isDesc(),orderBy.isCode());
				}
			}
			selector.limit(pageSize);
			selector.offset(pageSize * (pageNo-1));
			LogUtils.i("SQL", selector.toString());
			List<T> list = dbUtil.findAll(selector);
			PageBody<T> page = new PageBody<T>();
			page.pagecount = ((int)count + pageSize - 1) / pageSize;
			page.pageno = pageNo;
			page.rcount = (int) count;
			page.rdata = list;
			return page;
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public <T> PageBody<T> pageQuery(Selector selector,int pageNo,int pageSize){
		return pageQuery(selector, pageNo, pageSize,null);
	}
	
	public <T> PageBody<T> pageQuery(Selector selector,int pageNo){
		return pageQuery(selector, pageNo, Constant.PAGE_SIZE,null);
	}
	
	public <T> PageBody<T> pageQuery(Selector selector,int pageNo,List<OrderBy> orderList){
		return pageQuery(selector, pageNo, Constant.PAGE_SIZE,orderList);
	}
}
