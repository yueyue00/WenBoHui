package com.gheng.exhibit.http.body.response;

import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.model.databases.Company;
import com.gheng.exhibit.model.databases.CompanyType;

/**
 *
 * @author lileixing
 */
public class CompanyUpdateBody {

	public PageBody<CompanyType> companytype;
	
	public PageBody<Company> company;
	
}
