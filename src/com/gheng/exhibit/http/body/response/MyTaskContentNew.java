package com.gheng.exhibit.http.body.response;

import java.util.List;

import com.google.gson.annotations.Expose;

public class MyTaskContentNew {

    /**
     * code : 200
     * info : [{"journey":[{"sign":"0","title":"入住酒店","time":"16:00-20:30","xcid":60650,"location":"敦煌","description":"","date":"08-03","type":"2"}],"date":"08-03","week":"星期三"},{"journey":[{"sign":"0","title":"推动文化交流、共谋合作发展","time":"14:00-15:00","location":"会场01","description":"针对文博会的筹备情况，梁言顺介绍，每届文博会都设有一个主题，首届文博会以\u201c推动文化交流、共谋合作发展\u201d为主题，坚持共商共建共享，积极探索合作对话机制，在走出去中加深沟通、倾听心声、回应关切，在请进来中增进了解、达成共识、实现共赢，迈向人类命运共同体。","meetingId":59100,"date":"08-17","type":"1"}],"date":"08-17","week":"星期三"}]
     * message : vip嘉宾的所有日程!
     * pagecount : 0
     * pagenum : 0
     */
    @Expose
    private String code;
    @Expose
    private String message;
    @Expose
    private int pagecount;
    @Expose
    private int pagenum;
    /**
     * journey : [{"sign":"0","title":"入住酒店","time":"16:00-20:30","xcid":60650,"location":"敦煌","description":"","date":"08-03","type":"2"}]
     * date : 08-03
     * week : 星期三
     */
    @Expose
    private List<InfoBean> info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPagecount() {
        return pagecount;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
    	@Expose
        private String date;
    	@Expose
        private String week;
        /**
         * sign : 0
         * title : 入住酒店
         * time : 16:00-20:30
         * xcid : 60650
         * location : 敦煌
         * description : 
         * date : 08-03
         * type : 2
         */
    	@Expose
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

        public static class JourneyBean {
        	@Expose
            private String sign;
        	@Expose
            private String title;
        	@Expose
            private String time;
        	@Expose
            private int xcid;
        	@Expose
            private int meetingId;
        	@Expose
            private String location;
        	@Expose
            private String description;
        	@Expose
            private String date;
        	@Expose
            private String type;
        	
        	

           

			public int getMeetingId() {
				return meetingId;
			}

			public void setMeetingId(int meetingId) {
				this.meetingId = meetingId;
			}

			public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
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

            public int getXcid() {
                return xcid;
            }

            public void setXcid(int xcid) {
                this.xcid = xcid;
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
    }
}
