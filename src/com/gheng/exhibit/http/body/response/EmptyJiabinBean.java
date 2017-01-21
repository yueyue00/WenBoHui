package com.gheng.exhibit.http.body.response;

import java.util.List;

import com.gheng.exhibit.http.body.response.MyTaskListDataBean.InfoBean;
import com.gheng.exhibit.http.body.response.MyTaskListDataBean.InfoBean.CommTaskBean;
import com.gheng.exhibit.http.body.response.MyTaskListDataBean.InfoBean.JiabinBean;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

public class EmptyJiabinBean {
	 /**
     * code : 200
     * info : [{"commTask":[{"ID":42151,"NAME":"接机","UNIQUE_CODE":"jieji"},{"ID":42152,"NAME":"签报到","UNIQUE_CODE":"qianbaodao"},{"ID":42350,"NAME":"办入住","UNIQUE_CODE":"banruzhu"},{"ID":56250,"NAME":"看展览","UNIQUE_CODE":"kanzhanlan"},{"ID":58300,"NAME":"领资料","UNIQUE_CODE":"lingziliao"},{"ID":58301,"NAME":"送机","UNIQUE_CODE":"songji"},{"ID":58302,"NAME":"VIP","UNIQUE_CODE":"vip"}],"jiabin":[{"songji":"0","small_icon":"http://wuzhen.smartdot.com:8088/wenbo2/upload/member/20160707102021707237.png","kanzhanlan":"0","qianbaodao":"0","tel":"18515834121","name":"满爽","lingziliao":"0","jieji":"0","banruzhu":"0"}]}]
     * message : 公共任务以及嘉宾信息!
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
        /**
         * ID : 42151
         * NAME : 接机
         * UNIQUE_CODE : jieji
         */
    	@Expose
        private List<CommTaskBean> commTask;
        /**
         * songji : 0
         * small_icon : http://wuzhen.smartdot.com:8088/wenbo2/upload/member/20160707102021707237.png
         * kanzhanlan : 0
         * qianbaodao : 0
         * tel : 18515834121
         * name : 满爽
         * lingziliao : 0
         * jieji : 0
         * banruzhu : 0
         */
    	@Expose
        private JsonElement jiabin;
        
    	
        public JsonElement getJiabin() {
			return jiabin;
		}

		public void setJiabin(JsonElement jiabin) {
			this.jiabin = jiabin;
		}

		public List<CommTaskBean> getCommTask() {
            return commTask;
        }

        public void setCommTask(List<CommTaskBean> commTask) {
            this.commTask = commTask;
        }

        
//        public static class CommTaskBean {
//        	@Expose
//            private int ID;
//        	@Expose
//            private String NAME;
//        	@Expose
//            private String UNIQUE_CODE;
//
//            public int getID() {
//                return ID;
//            }
//
//            public void setID(int ID) {
//                this.ID = ID;
//            }
//
//            public String getNAME() {
//                return NAME;
//            }
//
//            public void setNAME(String NAME) {
//                this.NAME = NAME;
//            }
//
//            public String getUNIQUE_CODE() {
//                return UNIQUE_CODE;
//            }
//
//            public void setUNIQUE_CODE(String UNIQUE_CODE) {
//                this.UNIQUE_CODE = UNIQUE_CODE;
//            }
//        }
        
       
    }
}
