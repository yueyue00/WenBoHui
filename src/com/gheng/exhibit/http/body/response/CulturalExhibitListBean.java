package com.gheng.exhibit.http.body.response;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

public class CulturalExhibitListBean {

    /**
     * code : 200
     * info : [{"picUrl":"/upload/culturePictures/20160830103259785110.jpg","address":"五号厅B001","remark":"敦煌市是甘肃省酒泉市代管的一个县级市","name":"飞天佛","uniqueCode":"feitian"}]
     * message : 展商数据列表!
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
     * picUrl : /upload/culturePictures/20160830103259785110.jpg
     * address : 五号厅B001
     * remark : 敦煌市是甘肃省酒泉市代管的一个县级市
     * name : 飞天佛
     * uniqueCode : feitian
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

    public static class InfoBean implements Serializable{
    	@Expose
        private String picUrl;
    	@Expose
        private String address;
    	@Expose
        private String remark;
    	@Expose
        private String name;
    	@Expose
        private String uniqueCode;

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUniqueCode() {
            return uniqueCode;
        }

        public void setUniqueCode(String uniqueCode) {
            this.uniqueCode = uniqueCode;
        }
    }

}
