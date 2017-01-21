package com.gheng.exhibit.utils;

public class Projection {

	private static final double originShift = 2.0f * Math.PI * 6378137.0f / 2.0f;
	public static final int SCALE = 50;
	// 左上角坐标
	public static final double MMINX;
	public static final double MMAXY;

	static {
		// 114.514431 37.995950
		// 114.514466 37.995898
//		double lon = 114.514431;
//		double lat = 37.995950;
//		116.510622 39.905111
//		116.510805  39.904873
		double lon = 114.276010;
		double lat = 30.573191;
		double[] mll = lonLatToMeters(lon, lat);
		MMINX = mll[0];
		MMAXY = mll[1];
	}

	public static double[] lonLatToMeters(double lon, double lat) {
		double mx = lon * originShift / 180.0;
		double my = Math.log(Math.tan((90 + lat) * Math.PI / 360.0))
				/ (Math.PI / 180.0);

		my = my * originShift / 180.0;
		return new double[] { mx, my };
	}

	public static double[] convert(double[] xy) {

		double mllx = (double) (xy[0] / SCALE + Projection.MMINX);
		double mlly = (double) (Projection.MMAXY - xy[1] / SCALE);

		return new double[] { mllx, mlly };

	}

	public static double[] convert2Map(double x, double y) {

		float mx = (float) (x * SCALE - Projection.MMINX * SCALE);
		float my = (float) (Projection.MMAXY * SCALE - y * SCALE);

		return new double[] { mx, my };

	}

	/**
	 * Converts XY point from Spherical Mercator EPSG:900913 to lat/lon in WGS84
	 * Datum
	 */
	public static double[] metersToLonLat(double mx, double my) {

		double lon = (mx / originShift) * 180.0;
		double lat = (my / originShift) * 180.0;

		lat = 180
				/ Math.PI
				* (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2.0);
		return new double[] { lon, lat };
	}

}
