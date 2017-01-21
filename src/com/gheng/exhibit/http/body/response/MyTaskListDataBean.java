package com.gheng.exhibit.http.body.response;

import java.util.List;

import com.google.gson.annotations.Expose;

public class MyTaskListDataBean {

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
        private List<JiabinBean> jiabin;

        public List<CommTaskBean> getCommTask() {
            return commTask;
        }

        public void setCommTask(List<CommTaskBean> commTask) {
            this.commTask = commTask;
        }

        public List<JiabinBean> getJiabin() {
            return jiabin;
        }

        public void setJiabin(List<JiabinBean> jiabin) {
            this.jiabin = jiabin;
        }

        public static class CommTaskBean {
        	@Expose
            private int ID;
        	@Expose
            private String NAME;
        	@Expose
            private String UNIQUE_CODE;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getNAME() {
                return NAME;
            }

            public void setNAME(String NAME) {
                this.NAME = NAME;
            }

            public String getUNIQUE_CODE() {
                return UNIQUE_CODE;
            }

            public void setUNIQUE_CODE(String UNIQUE_CODE) {
                this.UNIQUE_CODE = UNIQUE_CODE;
            }
        }

        public static class JiabinBean {
        	@Expose
            private String songji;
        	@Expose
            private String small_icon;
        	@Expose
            private String kanzhanlan;
        	@Expose
            private String qianbaodao;
        	@Expose
            private String tel;
        	@Expose
            private String name;
        	@Expose
            private String lingziliao;
        	@Expose
            private String jieji;
        	@Expose
            private String banruzhu;
        	@Expose
        	private String yuzhuce;
        	
        	

            public String getYuzhuce() {
				return yuzhuce;
			}

			public void setYuzhuce(String yuzhuce) {
				this.yuzhuce = yuzhuce;
			}

			public String getSongji() {
                return songji;
            }

            public void setSongji(String songji) {
                this.songji = songji;
            }

            public String getSmall_icon() {
                return small_icon;
            }

            public void setSmall_icon(String small_icon) {
                this.small_icon = small_icon;
            }

            public String getKanzhanlan() {
                return kanzhanlan;
            }

            public void setKanzhanlan(String kanzhanlan) {
                this.kanzhanlan = kanzhanlan;
            }

            public String getQianbaodao() {
                return qianbaodao;
            }

            public void setQianbaodao(String qianbaodao) {
                this.qianbaodao = qianbaodao;
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

            public String getLingziliao() {
                return lingziliao;
            }

            public void setLingziliao(String lingziliao) {
                this.lingziliao = lingziliao;
            }

            public String getJieji() {
                return jieji;
            }

            public void setJieji(String jieji) {
                this.jieji = jieji;
            }

            public String getBanruzhu() {
                return banruzhu;
            }

            public void setBanruzhu(String banruzhu) {
                this.banruzhu = banruzhu;
            }
        }
    }
}
