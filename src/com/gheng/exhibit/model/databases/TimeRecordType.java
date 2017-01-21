package com.gheng.exhibit.model.databases;

/**
 *	时间记录类型
 * @author lileixing
 */
public enum TimeRecordType {

	SPEAKER("speakertime")
	 ,SCHEDULE("scheduletime")
	 ,SCHEDULE_TYPE("scheduletypetime")
	 ,SCHEDULE_INFO("scheduleinfotime")
	 ,COMPANY_TYPE("companytype")
	 ,COMPANY_INFO("companyinfo")
	 ,PRODUCT_TYPE("producttype")
	 ,PRODUCT_INFO("productinfo"),
	 BATCH("batch"),
	 AEREBATCH("areabatch"),
	 SURVEY("survey"),
	 OBJECTIVE("objective"),
	 QUESTION("question"),
	 OPTION("option")
	;
	
	private String name;
	
	private TimeRecordType(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
