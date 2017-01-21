package com.hebg3.mxy.utils;

/**
 * 室内定位 场景中的设施实体类
 */
public class SheShiListPojo {
	public int type;//0会场设施  1 宴会厅设施 2 酒店设施
	public String sheshiname="";//设施名称
	public String sheshinamezh="";//设施名称 中文 搜索用
	public String sheshiid="";//设施id
	public String sheshilogourl="";//设施logo图url地址
	public int drawableid;//如果写死在程序内，这个字段保存设施的drawableid
}
