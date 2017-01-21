package com.gheng.exhibit.http.body.response;

import java.io.Serializable;
import java.util.List;

public class VipSchdule implements Serializable{
	 /**
     * groupname : 9月19日  星期三
     * groupcontent : [{"title":"参加文博会开幕式","loccation":"敦煌国籍会展中心主报告厅","meetingdate":"09:00 - 11:00","content":"早晨8:30由华夏国籍大酒店出发，乘车前往国际会展中心。您乘坐的是5号车辆，车牌号：甘A.88888,随车服务人员:李彪 18601040951","isschedule":"true"},{"title":"参加敦煌文化年展高峰论坛会议","loccation":"敦煌国籍会展中心主报告厅","meetingdate":"14:00 - 15:30","content":"早晨8:30由华夏国籍大酒店出发，乘车前往国际会展中心。您乘坐的是5号车辆，车牌号：甘A.88888,随车服务人员:李彪 18601040951","isschedule":"false"},{"title":"参加探索丝绸之路沿线国家文化交流合作的新途径峰会","loccation":"敦煌国籍会展中心主报告厅","meetingdate":"16:00 - 17:30","content":"早晨8:30由华夏国籍大酒店出发，乘车前往国际会展中心。您乘坐的是5号车辆，车牌号：甘A.88888,随车服务人员:李彪 18601040951","isschedule":"true"}]
     */

//    private List<DataBean> data;
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }

//    public static class DataBean {
        private String groupname;
        /**
         * title : 参加文博会开幕式
         * loccation : 敦煌国籍会展中心主报告厅
         * meetingdate : 09:00 - 11:00
         * content : 早晨8:30由华夏国籍大酒店出发，乘车前往国际会展中心。您乘坐的是5号车辆，车牌号：甘A.88888,随车服务人员:李彪 18601040951
         * isschedule : true
         */

        private List<GroupcontentBean> groupcontent;

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public List<GroupcontentBean> getGroupcontent() {
            return groupcontent;
        }

        public void setGroupcontent(List<GroupcontentBean> groupcontent) {
            this.groupcontent = groupcontent;
        }

        public static class GroupcontentBean implements Serializable{
            private String title;
            private String loccation;
            private String meetingdate;
            private String content;
            private boolean isschedule;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getLoccation() {
                return loccation;
            }

            public void setLoccation(String loccation) {
                this.loccation = loccation;
            }

            public String getMeetingdate() {
                return meetingdate;
            }

            public void setMeetingdate(String meetingdate) {
                this.meetingdate = meetingdate;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public boolean isschedule() {
                return isschedule;
            }

            public void setIsschedule(boolean isschedule) {
                this.isschedule = isschedule;
            }
        }
    }
//}
