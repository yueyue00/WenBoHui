package com.gheng.exhibit.http;

import java.util.List;

/**
 * 
 * @author lileixing
 */
public class PageBody<T> {
	public int pagecount;
	public int rcount;
	public int pageno;
	public List<T> rdata;
	public long time;
	public long rtime;
	public String expdate;
	
	@Override
	public String toString() {
		return "ResponseBody [pagecount=" + pagecount + ", rcount=" + rcount
				+ ", pageno=" + pageno + ", rdata=" + rdata + "]";
	}

}
