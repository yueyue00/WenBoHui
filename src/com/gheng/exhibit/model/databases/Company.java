package com.gheng.exhibit.model.databases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gheng.exhibit.utils.StringTools;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * 参展商名录
 * 
 * @author zhao
 */
@Table(name = "Company")
public class Company extends BaseModel {

	/**
	 * 公司名称
	 */
	@Column(column = "name")
	private String name;
	/**
	 * 公司名称英文
	 */
	@Column(column = "enname")
	private String enname;
	/**
	 * 展位号
	 */
	@Column(column = "standreference")
	private String standreference;

	@Column(column = "remark")
	private String remark;

	@Column(column = "enremark")
	private String enremark;

	/**
	 * 场馆编号
	 */
	@Column(column = "zoneid")
	private String zoneid;

	@Column(column = "logo")
	private String logo;

	@Column(column = "typeid")
	private String typeid;

	@Column(column = "isfav")
	private int isfav;

	@Column(column = "commentcount")
	private Integer commentcount;
	
	@Column(column = "searchtxt")
	private String searchtxt;
	
	@Column(column = "sorting")
	private int sorting;
	
	@Column(column = "batchid")
	private Integer batchid;
	
	@Transient
	private String standreferenceNew;
	
	@Transient
	private List<Map<String, String>> standreferences = new ArrayList<Map<String, String>>();
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the enname
	 */
	public String getEnname() {
		return enname;
	}

	/**
	 * @param enname
	 *            the enname to set
	 */
	public void setEnname(String enname) {
		this.enname = enname;
	}

	/**
	 * @return the standreference
	 */
//	public String getStandreference() {
//		return standreference;
//	}

	/**
	 * @param standreference
	 *            the standreference to set
	 */
	public void setStandreference(String standreference) {
		this.standreference = standreference;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the enremark
	 */
	public String getEnremark() {
		return enremark;
	}

	/**
	 * @param enremark
	 *            the enremark to set
	 */
	public void setEnremark(String enremark) {
		this.enremark = enremark;
	}

	/**
	 * @return the zoneid
	 */
	public String getZoneid() {
		return zoneid;
	}

	/**
	 * @param zoneid
	 *            the zoneid to set
	 */
	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}

	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return the typeid
	 */
	public String getTypeid() {
		return typeid;
	}

	/**
	 * @param typeid
	 *            the typeid to set
	 */
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	/**
	 * @return the isfav
	 */
	public int getIsfav() {
		return isfav;
	}

	/**
	 * @param isfav
	 *            the isfav to set
	 */
	public void setIsfav(int isfav) {
		this.isfav = isfav;
	}

	/**
	 * @return the commentcount
	 */
	public Integer getCommentcount() {
		return commentcount;
	}

	/**
	 * @param commentcount the commentcount to set
	 */
	public void setCommentcount(Integer commentcount) {
		this.commentcount = commentcount;
	}

	public String getSearchtxt() {
		return searchtxt;
	}

	public void setSearchtxt(String searchtxt) {
		this.searchtxt = searchtxt;
	}

	public String getStandreferenceNew() {
		if(StringTools.isNotBlank(standreference)) {
			int length = 12;
			if(standreference.length() < length) {
				length = standreference.length();
				return standreference.replace("#", " ");
			} else {
				return standreference.replace("#", " ").substring(0, length) + "...";
			}
			
		}
		return "";
	}

	public List<Map<String, String>> getStandreferences() {
		standreferences.clear();
		if(StringTools.isNotBlank(standreference)) {
			String[] args = standreference.split("#");
			for(String arg : args) {
				Map<String, String> map = new HashMap<String, String>();
				if(StringTools.isBlank(arg)) {
					continue;
				}
				String[] as = arg.split("-");
				if(as.length == 1) {
					map.put("zone", as[0]);
				} else if(as.length >= 2) {
					map.put("zone", as[0]);
					if(as[1].contains(",")) {
						String[] ass = as[1].split(",");
						map.put("no", ass[0]);
					} else if(as[1].contains(".")) {
						String[] ass = as[1].split(".");
						map.put("no", ass[0]);
					} else if(as[1].contains("/")) {
						String[] ass = as[1].split("/");
						map.put("no", ass[0]);
					} else {
						map.put("no", as[1]);
					}
				}
				map.put("all", arg);
				standreferences.add(map);
			}
		}
		return standreferences;
	}

	public int getSorting() {
		return sorting;
	}

	public void setSorting(int sorting) {
		this.sorting = sorting;
	}

	/**
	 * @return the batchid
	 */
	public Integer getBatchid() {
		return batchid;
	}

	/**
	 * @param batchid the batchid to set
	 */
	public void setBatchid(Integer batchid) {
		this.batchid = batchid;
	}

}
