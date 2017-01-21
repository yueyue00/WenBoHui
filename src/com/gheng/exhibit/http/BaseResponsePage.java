package com.gheng.exhibit.http;



/**
 * 基本列表数据响应
 * 
 * @author lileixing
 */
public class BaseResponsePage<T> extends BaseResponse{

	public PageBody<T> body;

	@Override
	public String toString() {
		return "BaseResponsePage [body=" + body + ", action=" + action
				+ ", retcode=" + retcode + ", retmesg=" + retmesg + "]";
	}
}
