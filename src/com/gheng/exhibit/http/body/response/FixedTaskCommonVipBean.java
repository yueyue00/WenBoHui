package com.gheng.exhibit.http.body.response;

import java.util.List;

import com.google.gson.annotations.Expose;

public class FixedTaskCommonVipBean {


    /**
     * code : 200
     * info : [{"invitationCode":"sunjundong","small_icon":"","tel":"18201146335","name":"孙俊东"},{"invitationCode":"sunjundong","small_icon":"","tel":"18201146335","name":"孙俊东"},{"invitationCode":"5tv654cy","small_icon":"http://wuzhen.smartdot.com:8088/wenbo2/upload/member/20160803032350450473.jpg","tel":"18201146335","name":"王自豪"},{"invitationCode":"977ncqrb","small_icon":"","tel":"18201146335","name":"李双珠000"},{"invitationCode":"y4biulb9","small_icon":"","tel":"18201146335","name":"李双珠002"},{"invitationCode":"1ubpqpw3","small_icon":"","tel":"18201146335","name":"李双珠001"},{"invitationCode":"manshuang","small_icon":"http://wuzhen.smartdot.com:8088/wenbo2/upload/member/20160707102021707237.png","tel":"18201146335","name":"满爽"},{"invitationCode":"ypu8s9b2","small_icon":"http://wuzhen.smartdot.com:8088/wenbo2/upload/member/2016062703031339766.png","tel":"18201146335","name":"马春龙2222"}]
     * message : 未完成的嘉宾信息!
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
     * invitationCode : sunjundong
     * small_icon :
     * tel : 18201146335
     * name : 孙俊东
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
        private String invitationCode;
    	@Expose
        private String small_icon;
    	@Expose
        private String tel;
    	@Expose
        private String name;
        @Expose
        private boolean checkState = false;

        public boolean isCheckState() {
            return checkState;
        }
        public void setCheckState(boolean checkState) {
            this.checkState = checkState;
        }


        public String getInvitationCode() {
            return invitationCode;
        }

        public void setInvitationCode(String invitationCode) {
            this.invitationCode = invitationCode;
        }

        public String getSmall_icon() {
            return small_icon;
        }

        public void setSmall_icon(String small_icon) {
            this.small_icon = small_icon;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
