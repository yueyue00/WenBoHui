package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class JiuDianList {
	@Expose
	public String startTime;
	@Expose
	public String endTime;
	// @Expose
	// public List<JiuDianData> hotelInfo = new ArrayList<JiuDianData>();
	@Expose
	public JiuDianData hotelInfo;
}
