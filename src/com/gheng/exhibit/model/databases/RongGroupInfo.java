package com.gheng.exhibit.model.databases;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 群组信息
 * **/
@Table(name = "table_ronggroupinfo")
public class RongGroupInfo implements Serializable {
	@Id(column = "id")
	private int id;
	@Column(column = "name")
	public String name;
	@Column(column = "groupId")
	public String groupId; // 群组id

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
