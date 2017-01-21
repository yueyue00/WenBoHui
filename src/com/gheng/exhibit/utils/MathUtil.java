package com.gheng.exhibit.utils;

import com.gheng.indoormap.result.RoutingItem;

public class MathUtil
{
	/**
	 * 单例模式
	 */
	private static MathUtil mathUtil = new MathUtil();

	public static MathUtil getInstance()
	{
		if (null == mathUtil)
		{
			mathUtil = new MathUtil();
		}
		return mathUtil;
	}

	/**
	 * 计算向角度angle 行进 meter米后的魔卡托坐标（米）
	 * 
	 * @param mectorx
	 * @param mectory
	 * @param angle
	 * @param meter
	 */
	public double[] calCoor(double mectorx, double mectory, double angle, double meter)
	{
		double mx = mectorx + meter * Math.sin((angle * Math.PI) / 180);
		double my = mectory + meter * Math.cos((angle * Math.PI) / 180);
		return new double[]
		{ mx, my };
	}

	/**
	 * 2点之间的距离
	 * 
	 * @param mectorx
	 * @param mectory
	 * @param mx
	 * @param my
	 * @return
	 */
	public static double len(double mectorx, double mectory, double mx, double my)
	{
		return Math.sqrt(Math.pow((mectorx - mx), 2) + Math.pow((mectory - my), 2));
	}

	/**
	 * 计算角度
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double getAngle(double x1, double y1, double x2, double y2)
	{
		if (y2 - y1 == 0)
			return 0;
		double angle = Math.atan(Math.abs((x2 - x1) / (y2 - y1))) * 180 / Math.PI;
		double dy = y2 - y1;
		double dx = x2 - x1;
		if (dx > 0 && dy <= 0)
		{
			angle = (90. - angle) + 90;
		} else if (dx <= 0 && dy < 0)
		{
			angle = angle + 180;
		} else if (dx < 0 && dy >= 0)
		{
			angle = (90. - angle) + 270;
		}
		return angle;
	}

	/**
	 * 功能：已知一点，一条直线求垂足
	 * 
	 * @param xy1
	 *            直线起始点
	 * @param xy2
	 *            直线终止点
	 * @param xy3
	 *            直线外一点
	 * @return 输出的垂足
	 */
	public static double[] getPlumb(double[] xy1, double[] xy2, double[] xy3)
	{
		double dba, dbb;
		double x, y;
		// a = sqr( (x2 - x1)^2 +(y2 - y1)^2 )
		dba = Math.sqrt((double) ((xy2[0] - xy1[0]) * (xy2[0] - xy1[0]) + (xy2[1] - xy1[1]) * (xy2[1] - xy1[1])));
		// b = (x2-x1) * (x3-x1) +(y2 -y1) * (y3 -y1)
		dbb = ((xy2[0] - xy1[0]) * (xy3[0] - xy1[0]) + (xy2[1] - xy1[1]) * (xy3[1] - xy1[1]));
		// a = b / (a*a)
		dba = dbb / (dba * dba);
		// x4 = x1 +(x2 - x1)*a
		x = xy1[0] + (xy2[0] - xy1[0]) * dba;
		// y4 = y1 +(y2 - y1)*a
		y = xy1[1] + (xy2[1] - xy1[1]) * dba;

		return new double[]
		{ x, y };
	}

	public static boolean onLine(RoutingItem item1, RoutingItem item2, double[] cxy)
	{

		double maxx = item1.x > item2.x ? item1.x : item2.x;
		double minx = item1.x < item2.x ? item1.x : item2.x;

		double maxy = item1.y > item2.y ? item1.y : item2.y;
		double miny = item1.y < item2.y ? item1.y : item2.y;
		if (cxy[0] >= minx && cxy[0] <= maxx && cxy[1] >= miny && cxy[1] <= maxy)
		{
			return true;
		} else
		{
			return false;
		}
	}
}
