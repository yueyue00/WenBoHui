package com.gheng.exhibit.model.databases;


/**
 *
 * @author lileixing
 */
public class OrderBy {

	private String columnName;
	
	private boolean desc;
	
	private boolean code;
	
	private OrderBy(String columnName){
		this.columnName = columnName;
	}
	
	private OrderBy(String colunName,boolean desc){
		this.columnName = colunName;
		this.desc = desc;
	}
	
	private OrderBy(String colunName,boolean desc,boolean code){
		this.columnName = colunName;
		this.desc = desc;
		this.code = code;
	}
	
	public static OrderBy create(String columnName){
		return new OrderBy(columnName);
	}
	
	public static OrderBy create(String columnName,boolean desc){
		return new OrderBy(columnName,desc);
	}
	
	public static OrderBy create(String columnName,boolean desc,boolean code){
		return new OrderBy(columnName,desc,code);
	}
	
	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	
	/**
	 * @return the desc
	 */
	public boolean isDesc() {
		return desc;
	}
	
	/**
	 * @return the code
	 */
	public boolean isCode() {
		return code;
	}
	
}
