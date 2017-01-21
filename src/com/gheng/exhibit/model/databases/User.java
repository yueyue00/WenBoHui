package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 用户信息
 * **/
@Table(name = "User")
public class User extends BaseModel {
	@Id(column = "id")
	private int id;
	@Column(column = "userid")
	private String userId;
	@Column(column = "userjuese")
	private String userjuese;// 1.嘉宾 2.记者 3.
	@Column(column = "name")
	private String name;
	@Column(column = "zhiwei")
	private String zhiWei;
	@Column(column = "smallphotourl")
	private String smallPhotoUrl;
	@Column(column = "password")
	private String password;
	@Column(column = "vipid")
	private String vipid;
	@Column(column = "workplace")
	private String workplace;
	@Column(column = "ry_userId")
	private String ry_userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserjuese() {
		return userjuese;
	}

	public void setUserjuese(String userjuese) {
		this.userjuese = userjuese;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZhiWei() {
		return zhiWei;
	}

	public void setZhiWei(String zhiWei) {
		this.zhiWei = zhiWei;
	}

	public String getSmallPhotoUrl() {
		return smallPhotoUrl;
	}

	public String getRy_userId() {
		return ry_userId;
	}

	public void setRy_userId(String ry_userId) {
		this.ry_userId = ry_userId;
	}

	public void setSmallPhotoUrl(String smallPhotoUrl) {
		this.smallPhotoUrl = smallPhotoUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVipid() {
		return vipid;
	}

	public void setVipid(String vipid) {
		this.vipid = vipid;
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

}
