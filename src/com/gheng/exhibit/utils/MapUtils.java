package com.gheng.exhibit.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.gheng.indoormap.MapView;

public class MapUtils {

	private static final int DEFAULT_DIV_SCALE = 2;
	// # 20037508.342789244
	private static final double originShift = 2.0f * Math.PI * 6378137.0f / 2.0f;
	private static double X_PI = 3.14159265358979324 * 3000.0 / 180.0;
	// 左上角坐标
	public static final double MMINX;
	public static final double MMAXY;
	public static final int SCALE = 10;

	static {
//		114.514431  37.995950
//		114.514466  37.995898
		double lon = 114.514431;
		double lat = 37.995950;
		double[] mll = lonLatToMeters(lon, lat);
		MMINX = mll[0];
		MMAXY = mll[1];
	}
	
	public static double[] gcj2BD(double lon, double lat) {
		double x = lon, y = lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		return new double[] { bd_lon, bd_lat };
	}

	public static double[] bd2Gcj(double lon, double lat) {
		double x = lon - 0.0065, y = lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		return new double[] { gg_lon, gg_lat };
	}

	public static String convertDistance(double distance) {
		if (distance < 1000)
			return (int) distance + "M";
		return (int) (distance / 1000) + "KM";
	}

	public static double[] lonLatToMeters(double lon, double lat) {
		double mx = lon * originShift / 180.0;
		double my = Math.log(Math.tan((90 + lat) * Math.PI / 360.0))
				/ (Math.PI / 180.0);

		my = my * originShift / 180.0;
		return new double[] { mx, my };
	}

	/**
	 * Converts XY point from Spherical Mercator EPSG:900913 to lat/lon in WGS84
	 * Datum
	 */
	public static double[] metersToLonLat(double mx, double my) {

		double lon = (mx / originShift) * 180.0;
		double lat = (my / originShift) * 180.0;

		lat = 180/ Math.PI *(2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2.0);
		return new double[] { lon, lat };
	}

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static String formatNum(double num, Integer digist) {
		NumberFormat format = NumberFormat.getInstance();
		format.format(num);
		format.setMaximumFractionDigits(digist);
		return format.format(num);
	}

	public static int convertFloor(int fid) {
		int a = fid / 10000;// 1 地下 2地上
		int b = fid % 10000;
		int c = b / 10;
		if (a == 1)
			return -c;
		return c;
	}

	/**
	 * 转换到世界地图 墨卡托坐标
	 * @param xy
	 * @return
	 */
	public static float[] convert(double[] xy) {
		float mllx = (float) (xy[0] / SCALE + MMINX);
		float mlly = (float) (MMAXY - xy[1] / SCALE);

		return new float[] { mllx, mlly };
	}

	/**
	 * 转换到室内地图 墨卡托坐标
	 * @param noWifi
	 * @param locx
	 * @param locy
	 * @param floor
	 * @param mv
	 * @return
	 */
	public static float[] convertMeters(boolean noWifi, double lon,double lat, int floor, MapView mv) {
		
//		double[] mll = lonLatToMeters(lon, lat);

		System.out.println("mll: " + lon + ", " + lat);

//		PositionResult positionResult = MathUtil.getInstance().cal(new double[] {lon, lat }, noWifi);

//		System.out.println(positionResult.getX() + ":" + positionResult.getY());

		float x = (float) (lon*SCALE - MMINX*SCALE);
		float y = (float) (MMAXY*SCALE - lat*SCALE);
		System.out.println(x + ", " + y);

		// return new double[]{positionResult.getX(),positionResult.getY()};
		return new float[] { x, y };
	}

	/**
	 * 路径规划
	 * 
	 * @param sGeoPoint
	 * @param eGeoPoint
	 * @return
	 */
	// public GeoPoint route(GeoPoint sGeoPoint, GeoPoint eGeoPoint) {
	//
	// // mapView.setSelected(false);
	// GeoPoint slonLatPoint = Projection.transformMercatorToLonlat(sGeoPoint.x,
	// sGeoPoint.y);
	// GeoPoint elonLatPoint = Projection.transformMercatorToLonlat(eGeoPoint.x,
	// eGeoPoint.y);
	//
	// Resources res = PositionActivity2.this.getResources();
	// // Drawable drawable = res.getDrawable(R.drawable.start);
	// OverLayer m_OverLayer = mapView.getOverLayer();
	//
	// Mark endMark = new Mark();
	// endMark.setGeoPoint(eGeoPoint);
	// Drawable drawable2 = res.getDrawable(R.drawable.end);
	// endMark.setFID(2);
	// endMark.setImageName(String.valueOf(R.drawable.end));// 图片名称是底层记录的唯一标识符
	// endMark.setIcon(drawable2);
	// m_OverLayer.addGeometry(endMark);
	//
	// GeoPointIndoor st = new GeoPointIndoor();
	// // st.setX(116.510665);
	// // st.setY(39.904998);
	// st.setX(slonLatPoint.getX());
	// st.setY(slonLatPoint.getY());
	// st.setFloorIndex(nBuilingCur.getCurFloorIndex());
	//
	// GeoPointIndoor en = new GeoPointIndoor();
	// // en.setX(116.510712);
	// // en.setY(39.904912);
	// en.setX(elonLatPoint.getX());
	// en.setY(elonLatPoint.getY());
	// en.setFloorIndex(nBuilingCur.getCurFloorIndex());
	//
	// List<RouteItem> list = route.customizeRoute(st, en);
	//
	// // LineString line = new LineString();
	// // line.setFillColor(255, 0, 0);
	// // line.setLineColor(0, 255, 0);
	//
	// for (int i = 0; i < list.size() - 1; i++) {
	// RouteItem item1 = list.get(i);
	// RouteItem item2 = list.get(i + 1);
	//
	// double x1 = item1.getX();
	// double y1 = item1.getY();
	// double x2 = item2.getX();
	// double y2 = item2.getY();
	//
	// double x = sGeoPoint.getX();
	// double y = sGeoPoint.getY();
	// double[] cxy = MathUtil.getPlumb(new double[]{x1, y1},
	// new double[]{x2, y2}, new double[]{x, y});
	//
	// double maxx = item1.getX() > item2.getX() ? item1.getX() : item2.getX();
	// double minx = item1.getX() < item2.getX() ? item1.getX() : item2.getX();
	//
	// double maxy = item1.getY() > item2.getY() ? item1.getY() : item2.getY();
	// double miny = item1.getY() < item2.getY() ? item1.getY() : item2.getY();
	//
	// if (cxy[0] >= minx && cxy[0] <= maxx && cxy[1] >= miny && cxy[1] <= maxy)
	// {
	// sGeoPoint.setX(cxy[0]);
	// sGeoPoint.setY(cxy[1]);
	//
	// break;
	// }
	// }
	// System.out.println("垂足点：" + sGeoPoint.getX() + ":" + sGeoPoint.getY());
	// // m_OverLayer.addGeometry(line);
	//
	// return sGeoPoint;
	// }

}
