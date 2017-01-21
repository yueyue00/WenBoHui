package com.hebg3.mxy.utils;

/**
 * 室内定位 场景实体类
 */
public class ChangJingListPojo{
	public int type;//0：会场  1宴会厅   2 酒店
	public String roomname="";//场景名称
	public String roomnamezh="";//场景名称 中文  搜索时使用
	public String roomid="";//场景id
	public int ischose=0;//当前item是否被选中 0未选中  1 选中
	public int drawableid=0;//程序内图片id
	public int floor=1;//场景所在地图楼层
}
