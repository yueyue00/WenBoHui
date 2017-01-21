package com.gheng.exhibit.http.body.response;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.annotations.Expose;

public class BaiDuLocations {
//	@Expose
//	public List<Locations> hotel = new ArrayList<Locations>();// 酒店
	@Expose
	public List<HotelLocations> hotel = new ArrayList<HotelLocations>();// 嘉宾酒店
	@Expose
	public List<Locations> confHall = new ArrayList<Locations>();// 会场
	@Expose
	public List<Locations> scenicSpot = new ArrayList<Locations>();// 景点

	public class Locations {
		@Expose
		public String latitude = "";
		@Expose
		public String longitude = "";
		@Expose
		public String name;
		@Expose
		public String type;
		@Expose
		public LatLng ll;

		public void setLl(LatLng ll) {
			this.ll = ll;
		}

	}
	public class HotelLocations {
//		@Expose
//		public String invitation_code = "";
		@Expose
		public String id = "";
		@Expose
		public String member_id = "";//嘉宾酒店标识
		@Expose
		public String hotel_name = "";
		@Expose
		public String en_hotel_name = "";
		@Expose
		public String room_number = "";
//		@Expose
//		public String zh_jdfj_no = "";//房间号
		@Expose
		public String address = "";
		@Expose
		public String en_address = "";
		@Expose
		public String lat_itude = "";
		@Expose
		public String long_itude = "";
		@Expose
		public LatLng ll;

		public void setLl(LatLng ll) {
			this.ll = ll;
		}

	}
}
