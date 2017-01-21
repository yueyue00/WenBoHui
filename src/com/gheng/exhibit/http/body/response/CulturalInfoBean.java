package com.gheng.exhibit.http.body.response;

import com.google.gson.annotations.Expose;

public class CulturalInfoBean {
	/**
     * code : 200
     * info : {"voice":"/upload/cultureVoice/20160830053420697026.mp3","remark":"敦煌市是甘肃省酒泉市代管的一个县级市","address":"五号厅B001","name":"飞天佛","pic":"/upload/culturePictures/2016083005090338265.jpg"}
     * message : 文化年展详情!
     * pagecount : 0
     * pagenum : 0
     */
    @Expose
    private String code;
    /**
     * voice : /upload/cultureVoice/20160830053420697026.mp3
     * remark : 敦煌市是甘肃省酒泉市代管的一个县级市
     * address : 五号厅B001
     * name : 飞天佛
     * pic : /upload/culturePictures/2016083005090338265.jpg
     */
    @Expose
    private InfoBean info;
    @Expose
    private String message;
    @Expose
    private int pagecount;
    @Expose
    private int pagenum;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
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

    public static class InfoBean {
    	@Expose
        private String voice;
    	@Expose
        private String remark;
    	@Expose
        private String address;
    	@Expose
        private String name;
    	@Expose
        private String pic;

        public String getVoice() {
            return voice;
        }

        public void setVoice(String voice) {
            this.voice = voice;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
