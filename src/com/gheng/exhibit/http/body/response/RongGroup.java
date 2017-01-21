package com.gheng.exhibit.http.body.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class RongGroup {
	@Expose
	public List<RenyuanBean> qunzu = new ArrayList<RenyuanBean>();
	@Expose
	public List<RenyuanBean> renyuan = new ArrayList<RenyuanBean>();

	public class RenyuanBean {
		@Expose
		public String shortname;
		@Expose
		public String truename;
		@Expose
		public String maindeptid;
		@Expose
		public String mobile;
		@Expose
		public String type;
		@Expose
		public String USER_ID;
		// 下面是群组的信息
		@Expose
		public String name;
		@Expose
		public String id;
		@Expose
		public String group_id;
	}

}
