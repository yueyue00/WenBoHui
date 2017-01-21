package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 *	APP版本信息
 * @author lileixing
 */
@Table(name="AppInfo")
public class AppInfo extends BaseModel{

	@Column(column = "appversion")
	private Integer appVersion;
	
	@Column(column = "mapversion")
	private Integer mapVersion;
	
	@Column(column = "wifversion")
	private Integer wifiVersion;
	
	@Column(column = "bconversion")
	private Integer bconVersion;

	/**
	 * @return the appVersion
	 */
	public Integer getAppVersion() {
		return appVersion;
	}

	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(Integer appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * @return the mapVersion
	 */
	public Integer getMapVersion() {
		return mapVersion;
	}

	/**
	 * @param mapVersion the mapVersion to set
	 */
	public void setMapVersion(Integer mapVersion) {
		this.mapVersion = mapVersion;
	}

	/**
	 * @return the wifiVersion
	 */
	public Integer getWifiVersion() {
		return wifiVersion;
	}

	/**
	 * @param wifiVersion the wifiVersion to set
	 */
	public void setWifiVersion(Integer wifiVersion) {
		this.wifiVersion = wifiVersion;
	}

	/**
	 * @return the bconVersion
	 */
	public Integer getBconVersion() {
		return bconVersion;
	}

	/**
	 * @param bconVersion the bconVersion to set
	 */
	public void setBconVersion(Integer bconVersion) {
		this.bconVersion = bconVersion;
	}
	
}
