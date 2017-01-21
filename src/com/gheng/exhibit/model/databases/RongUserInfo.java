package com.gheng.exhibit.model.databases;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 用户信息
 * **/
@Table(name = "table_ronguserinfo")
public class RongUserInfo implements Serializable {
	@Id
	private int id;
	@Column(column = "userId")
	public String userId;
	@Column(column = "name")
	public String name; // 用户名

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "RongUserInfo [id=" + id + ", userId=" + userId + ", name="
				+ name + "]";
	}
}
