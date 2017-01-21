package com.gheng.exhibit.http.body.response;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;

public class VipInfoListBaen {

    /**
     * code : 200
     * info : [{"id":"wangsy","sex":"1","username":"王思羽","photourlbig":"http://wuzhen.smartdot.com:8088/wenbo2/upload/member/20160725032852741619.png","photourl":"http://wuzhen.smartdot.com:8088/wenbo2/upload/member/20160725032852741467.png","job":"职员","workplace":"慧点科技","whetherSeeTrip":"1"},{"id":"manshuang","sex":"1","username":"满爽","photourlbig":"http://wuzhen.smartdot.com:8088/wenbo2/upload/member/20160707102021711573.png","photourl":"http://wuzhen.smartdot.com:8088/wenbo2/upload/member/20160707102021707237.png","job":"总监","workplace":"慧点科技","whetherSeeTrip":"1"}]
     * message : vip人员列表!
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
     * id : wangsy
     * sex : 1
     * username : 王思羽
     * photourlbig : http://wuzhen.smartdot.com:8088/wenbo2/upload/member/20160725032852741619.png
     * photourl : http://wuzhen.smartdot.com:8088/wenbo2/upload/member/20160725032852741467.png
     * job : 职员
     * workplace : 慧点科技
     * whetherSeeTrip : 1
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
        private String vipId;
    	@Expose
        private String sex;
    	@Expose
        private String username;
    	@Expose
        private String photourlbig;
    	@Expose
        private String photourl;
    	@Expose
    	private String mobile;
        /**
         * ry_userId : c62fcfcc-45d8-4b16-8614-c2d38fc4a2c5
         * ry_token : /N59Ybflqpfqqyk5T9MdoYvUY9cEVagkRsg4Fdw11LwiDu2pMQbeDqejsRYFdPoeCMVfYbnHJ/d7EBb+lhUfTN74UpMCVhkkU4L0Zk2ysgQjOhv9Gqpnj79U5ExVl48aQEu6om6dT2U=
         * ry_imgUrl :
         * ry_name : 刘灵
         */
    	@Expose
        private MapBean map;
    	@Expose
        private String job;
    	@Expose
        private String workplace;
    	@Expose
        private String whetherSeeTrip;
        
        public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getvipId() {
            return vipId;
        }

        public void setvipId(String vipId) {
            this.vipId = vipId;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhotourlbig() {
            return photourlbig;
        }

        public void setPhotourlbig(String photourlbig) {
            this.photourlbig = photourlbig;
        }

        public String getPhotourl() {
            return photourl;
        }

        public void setPhotourl(String photourl) {
            this.photourl = photourl;
        }

        public MapBean getMap() {
            return map;
        }

        public void setMap(MapBean map) {
            this.map = map;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getWorkplace() {
            return workplace;
        }

        public void setWorkplace(String workplace) {
            this.workplace = workplace;
        }

        public String getWhetherSeeTrip() {
            return whetherSeeTrip;
        }

        public void setWhetherSeeTrip(String whetherSeeTrip) {
            this.whetherSeeTrip = whetherSeeTrip;
        }

        public static class MapBean {
            private String ry_userId;
            private String ry_token;
            private String ry_imgUrl;
            private String ry_name;

            public String getRy_userId() {
                return ry_userId;
            }

            public void setRy_userId(String ry_userId) {
                this.ry_userId = ry_userId;
            }

            public String getRy_token() {
                return ry_token;
            }

            public void setRy_token(String ry_token) {
                this.ry_token = ry_token;
            }

            public String getRy_imgUrl() {
                return ry_imgUrl;
            }

            public void setRy_imgUrl(String ry_imgUrl) {
                this.ry_imgUrl = ry_imgUrl;
            }

            public String getRy_name() {
                return ry_name;
            }

            public void setRy_name(String ry_name) {
                this.ry_name = ry_name;
            }
        }
    }
}
