package com.gheng.exhibit.http.body.response;

import java.util.List;

/**
 * 
 * @author lileixing
 */
public class HotelBody<T>
{
	public int pagecount;
	public int rcount;
	public int pageno;
	public List<T> rdata;
	public long time;

	public String linkaddress;
	public String linkname;
	public String linktel;
	public String qq;
	public String email;
	public String fax;
	public String postal;
	public String mobile;

	@Override
	public String toString()
	{
		return "ResponseBody [pagecount=" + pagecount + ", rcount=" + rcount + ", pageno=" + pageno + ", rdata=" + rdata + "]";
	}

}
