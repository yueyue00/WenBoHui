package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class QianDaoBackInfo {
	@Expose
	public String sign;// 签到的标识(1 已完成 0 任务提交成功)
}
