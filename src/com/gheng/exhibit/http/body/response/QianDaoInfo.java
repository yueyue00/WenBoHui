package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class QianDaoInfo {
	@Expose
	public RenWu renwu;// 任务标识
	@Expose
	public User user;// 用户信息

	public class RenWu {
		@Expose
		public String shanggang;
		@Expose
		public String xiaban;
	}

	public class User {
		@Expose
		public String sjshortName;
		@Expose
		public String sjname;
		@Expose
		public String sign;
	}
}
