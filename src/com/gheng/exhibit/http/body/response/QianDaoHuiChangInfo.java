package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

/**
 * 大会日期分类实体类
 */
public class QianDaoHuiChangInfo {
	@Expose
	public RenWu renwu;// 任务标识
	@Expose
	public UserMap userMap;// 司机信息

	public class RenWu {
		@Expose
		public String chufa;
		@Expose
		public String daoda;
	}

	public class UserMap {
		@Expose
		public String sign;//车辆状态
		@Expose
		public String sjchexing;//车型
		@Expose
		public String sjcarryNo;//载客量
		@Expose
		public String sjcarNumber;//车牌号
		@Expose
		public String name;//
		@Expose
		public String sjshortName;//司机的userid
		@Expose
		public String sjimg;//头像
		@Expose
		public String sjname;//司机名字
	}
}
