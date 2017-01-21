package com.gheng.exhibit.http.body.response;

import com.gheng.exhibit.http.PageBody;
import com.gheng.exhibit.model.databases.AreaBatch;
import com.gheng.exhibit.model.databases.Batch;

/**
 *
 * @author lileixing
 */
public class BatchUpdateBody {
	
	public PageBody<Batch> batch;
	
	public PageBody<AreaBatch> areabatch;
	
}
