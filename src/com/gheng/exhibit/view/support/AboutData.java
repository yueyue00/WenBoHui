package com.gheng.exhibit.view.support;

/**
 * 
 * @author lileixing
 */
public class AboutData {

	public String name;

	public String searchKey;

	public boolean isNew = false;

	public AboutData(String name, boolean isNew) {
		this.name = name;
		this.isNew = isNew;
	}

	public AboutData(String name) {
		this.name = name;
	}

}
