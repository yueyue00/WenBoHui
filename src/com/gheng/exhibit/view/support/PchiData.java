package com.gheng.exhibit.view.support;

/**
 *
 * @author lileixing
 */
public class PchiData {
	
	public String name;
	
	public int icon;
	
	public int mode;
	
	public PchiData(){}
	
	public PchiData(String name,int mode,int icon){
		this.name = name;
		this.mode = mode;
		this.icon = icon;
	}
	
	public PchiData(String name,int icon){
		this.name = name;
		this.icon = icon;
	}
}
