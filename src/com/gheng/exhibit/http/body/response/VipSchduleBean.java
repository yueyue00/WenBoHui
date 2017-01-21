package com.gheng.exhibit.http.body.response;

import java.io.Serializable;
import java.util.List;

public class VipSchduleBean {
	  /**
     * message : vip人员行程列表!
     * bigPorurl : http://172.20.30.2:8080/wenbo2/upload/member/20160628021556438742.png
     * smallPorurl : http://172.20.30.2:8080/wenbo2/upload/member/20160704041305654634.jpg
     * code : 200
     * info : [{"journey":[{"title":"会议名称测试数据","time":"14:00-16:00","location":"负1F宴会厅1","description":"三中全会","date":"07-01","type":"1"}],"date":"07-01","week":"星期五"},{"journey":[{"title":"行程1","time":"18:00-20:30","location":"慧点1","description":"行程1行程1","date":"07-04","type":"2"},{"title":"行程2","time":"12:00-13:00","location":"慧点2","description":"行程2行程2行程2行程2","date":"07-04","type":"2"}],"date":"07-04","week":"星期一"}]
     */

    private String message;
    private String bigPorurl;
    private String smallPorurl;
    private String code;
    /**
     * journey : [{"title":"会议名称测试数据","time":"14:00-16:00","location":"负1F宴会厅1","description":"三中全会","date":"07-01","type":"1"}]
     * date : 07-01
     * week : 星期五
     */

    private List<InfoBean> info;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBigPorurl() {
        return bigPorurl;
    }

    public void setBigPorurl(String bigPorurl) {
        this.bigPorurl = bigPorurl;
    }

    public String getSmallPorurl() {
        return smallPorurl;
    }

    public void setSmallPorurl(String smallPorurl) {
        this.smallPorurl = smallPorurl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
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
    }
}
