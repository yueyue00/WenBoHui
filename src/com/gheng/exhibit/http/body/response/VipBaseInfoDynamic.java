package com.gheng.exhibit.http.body.response;

import java.util.Map;

public class VipBaseInfoDynamic {
    private String code;
    private String message;
    private FwInfoBean fwInfoBean;
    private JbjbInfoBean jbjbInfoBean;
    private Map<String, Object> jbxxInfoMap;
    private Map<String, Object> kvInfoMap;
    
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
	public FwInfoBean getFwInfoBean() {
		return fwInfoBean;
	}
	public void setFwInfoBean(FwInfoBean fwInfoBean) {
		this.fwInfoBean = fwInfoBean;
	}
	public JbjbInfoBean getJbjbInfoBean() {
		return jbjbInfoBean;
	}
	public void setJbjbInfoBean(JbjbInfoBean jbjbInfoBean) {
		this.jbjbInfoBean = jbjbInfoBean;
	}
	public Map<String, Object> getJbxxInfoMap() {
		return jbxxInfoMap;
	}
	public void setJbxxInfoMap(Map<String, Object> jbxxInfoMap) {
		this.jbxxInfoMap = jbxxInfoMap;
	}
	public Map<String, Object> getKvInfoMap() {
		return kvInfoMap;
	}
	public void setKvInfoMap(Map<String, Object> kvInfoMap) {
		this.kvInfoMap = kvInfoMap;
	}
	/**
     * 服务人员信息
     */
    public static class FwInfoBean {
        private String userjuese;
        private String name;
        private String userid;
        private String mobile;
        private String workplace;
        private String ry_userId;
        private String ry_token;
        private String ry_imgUrl;
        private String ry_name;

        public String getUserjuese() {
            return userjuese;
        }

        public void setUserjuese(String userjuese) {
            this.userjuese = userjuese;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getWorkplace() {
            return workplace;
        }

        public void setWorkplace(String workplace) {
            this.workplace = workplace;
        }

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

		@Override
		public String toString() {
			return "FwInfoBean [userjuese=" + userjuese + ", name=" + name
					+ ", userid=" + userid + ", mobile=" + mobile
					+ ", workplace=" + workplace + ", ry_userId=" + ry_userId
					+ ", ry_token=" + ry_token + ", ry_imgUrl=" + ry_imgUrl
					+ ", ry_name=" + ry_name + "]";
		}
        
       
    }
    /**
     * 嘉宾信息含头像信息
     */
    public static class JbjbInfoBean {
        private String userid;
        private String name;
        private String userjuese;
        private String workplace;
        private String job;
        private String mobile;
        private String photourl;
        private String photourlbig;
        private String ry_userId;
        private String ry_name;
        private String ry_token;
        private String ry_imgUrl;
        private String isScan="";
        
        
        public String getIsScan() {
			return isScan;
		}

		public void setIsScan(String isScan) {
			this.isScan = isScan;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserjuese() {
            return userjuese;
        }

        public void setUserjuese(String userjuese) {
            this.userjuese = userjuese;
        }

        public String getWorkplace() {
            return workplace;
        }

        public void setWorkplace(String workplace) {
            this.workplace = workplace;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getPhotourl() {
            return photourl;
        }

        public void setPhotourl(String photourl) {
            this.photourl = photourl;
        }

        public String getPhotourlbig() {
            return photourlbig;
        }

        public void setPhotourlbig(String photourlbig) {
            this.photourlbig = photourlbig;
        }

        public String getRy_userId() {
            return ry_userId;
        }

        public void setRy_userId(String ry_userId) {
            this.ry_userId = ry_userId;
        }

        public String getRy_name() {
            return ry_name;
        }

        public void setRy_name(String ry_name) {
            this.ry_name = ry_name;
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

		@Override
		public String toString() {
			return "JbjbInfoBean [userid=" + userid + ", name=" + name
					+ ", userjuese=" + userjuese + ", workplace=" + workplace
					+ ", job=" + job + ", mobile=" + mobile + ", photourl="
					+ photourl + ", photourlbig=" + photourlbig
					+ ", ry_userId=" + ry_userId + ", ry_name=" + ry_name
					+ ", ry_token=" + ry_token + ", ry_imgUrl=" + ry_imgUrl
					+ "]";
		}

		
        
        
    }
}
