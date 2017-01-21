package com.gheng.exhibit.http.body.request;

import com.gheng.exhibit.utils.Constant;
import com.gheng.exhibit.utils.SharedData;

/**
 *
 * @author lileixing
 */
public class CompanyEditParam {

	public String eid = SharedData.getBatchId()+"";
	
	public String companyid;
	
	public String name;
	
	public String enname;
	
	public String type = "1";
	
}
