package com.gheng.exhibit.http.body.response;

import java.io.Serializable;
import java.util.List;

import com.gheng.exhibit.http.body.response.VipSchduleBean.InfoBean.JourneyBean;

public class VipScheduleInfoBean {
//	 /**
//     * journey : [{"title":"会议名称测试数据","time":"14:00-16:00","location":"负1F宴会厅1","description":"三中全会","date":"07-01","type":"1"}]
//     * date : 07-01
//     * week : 星期五
//     */
//
//    private List<InfoBean> info;
//
//    public List<InfoBean> getInfo() {
//        return info;
//    }
//
//    public void setInfo(List<InfoBean> info) {
//        this.info = info;
//    }

//    public static class InfoBean {
	    
        private String date;
        private String week;
        /**
         * title : 会议名称测试数据
         * time : 14:00-16:00
         * location : 负1F宴会厅1
         * description : 三中全会
         * date : 07-01
         * type : 1
         */

        private List<JourneyBean> journey;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public List<JourneyBean> getJourney() {
            return journey;
        }

        public void setJourney(List<JourneyBean> journey) {
            this.journey = journey;
        }

        public static class JourneyBean implements Serializable{
            private String title;
            private String time;
            private String location;
            private String description;
            private String date;
            private String type;
            private String meetingId;
            
            

            public String getMeetingId() {
				return meetingId;
			}

			public void setMeetingId(String meetingId) {
				this.meetingId = meetingId;
			}

			public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
//    }
}
