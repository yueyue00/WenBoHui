package com.gheng.exhibit.http.body.response;

import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.model.databases.Product;
import com.gheng.exhibit.model.databases.ProductType;

/**
 *
 * @author lileixing
 */
public class ProductUpdateBody {

	public PageBody<ProductType> producttype;
	
	public PageBody<Product> product;
	
}
