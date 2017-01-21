package com.gheng.exhibit.http.body.response;

import java.util.List;

import com.google.gson.annotations.Expose;

public class VipBeanForVipList {

    /**
     * code : 200
     * info : [{"sign":1,"vipJiabin":[{"invitationCode":"kfp4s824","small_icon":"","name":"李双珠","mobile":"18201146331"},{"invitationCode":"manshuang","small_icon":"http://wuzhen.smartdot.com:8088/wenbo2/upload/member/20160707102021707237.png","name":"满爽","mobile":"18515834121"}]}]
     * message : 所有VIP嘉宾信息!
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
     * sign : 1
     * vipJiabin : [{"invitationCode":"kfp4s824","small_icon":"","name":"李双珠","mobile":"18201146331"},{"invitationCode":"manshuang","small_icon":"http://wuzhen.smartdot.com:8088/wenbo2/upload/member/20160707102021707237.png","name":"满爽","mobile":"18515834121"}]
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
        private int sign;
        /**
         * invitationCode : kfp4s824
         * small_icon : 
         * name : 李双珠
         * mobile : 18201146331
         */
        @Expose
        private List<VipJiabinBean> vipJiabin;

        public int getSign() {
            return sign;
        }

        public void setSign(int sign) {
            this.sign = sign;
        }

        public List<VipJiabinBean> getVipJiabin() {
            return vipJiabin;
        }

        public void setVipJiabin(List<VipJiabinBean> vipJiabin) {
            this.vipJiabin = vipJiabin;
        }

        public static class VipJiabinBean {
        	@Expose
            private String invitationCode;
        	@Expose
            private String small_icon;
        	@Expose
            private String name;
        	@Expose
            private String mobile;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }
        }
    }
	
	

}
