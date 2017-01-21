package com.gheng.exhibit.http.body.response;

/**
 * Created by lixiaoming on 16/7/8.
 */
public class VipInfoBean {


    /**
     * code : 200
     * message : 服务人员嘉宾信息详情!
     * jbjbInfo : {"userid":"macl","name":"马春龙","userjuese":"1","workplace":"慧点科技","job":"总监","photourl":"嘉宾小头像","photourlbig":" 嘉宾大头像 ","ry_userId":"macl","ry_name":"马春龙","ry_token":"ssssssdfdff@##1123","ry_imgUrl":" http://wuz/00902407305.jpg "}
     * jbxxInfo : {"guoji":"国籍","contactPhone":"联络人手机号","sex":"性别","mobile":"18515834121","zhengjiancode":"证件号码","ethnic":"民族","jiguan":"籍贯","contactJob":"联络人职务","religion":"宗教信仰","photourlbig":" 嘉宾大头像 ","contactWorkPlace":"联络人单位","rating":"嘉宾级别","contactPerson":"联络人姓名","isvip":"联络人类别","oneTooneServe":"是否重要嘉宾","leadAttention":"是否领导关注","zifei":"是否自费"}
     * fwInfo : {"userjuese":"6","name":"张哲","userid":"fw_zhangzhe","mobile":"13223232323","workplace":"ceshi","ry_userId":"fw_zhangzhe","ry_token":"234ewrtt!ddd","ry_imgUrl":"","ry_name":"张哲"}
     * kvInfo : {"name":"姓名","mobile":"手机号","workplace":"工作单位","job":"职位","sex":"性别","zhengjiancode":"证件号码","guoji":"国籍","jiguan":"籍贯","ethnic":"民族","religion":"宗教信仰","contactPerson":"联络人姓名","contactPhone":"联络人电话","contactWorkPlace":"联络人单位","contactJob":"联络人职务","rating":"嘉宾级别","isvip":"嘉宾类别","oneTooneServe":"是否重要嘉宾","leadAttention":"是否领导关注","zifei":"是否自费"}
     */

    private String code;
    private String message;
    /**
     * userid : macl
     * name : 马春龙
     * userjuese : 1
     * workplace : 慧点科技
     * job : 总监
     * photourl : 嘉宾小头像
     * photourlbig :  嘉宾大头像
     * ry_userId : macl
     * ry_name : 马春龙
     * ry_token : ssssssdfdff@##1123
     * ry_imgUrl :  http://wuz/00902407305.jpg
     */

    private JbjbInfoBean jbjbInfo; //嘉宾基本信息含头像信息
    /**
     * guoji : 国籍
     * contactPhone : 联络人手机号
     * sex : 性别
     * mobile : 18515834121
     * zhengjiancode : 证件号码
     * ethnic : 民族
     * jiguan : 籍贯
     * contactJob : 联络人职务
     * religion : 宗教信仰
     * photourlbig :  嘉宾大头像
     * contactWorkPlace : 联络人单位
     * rating : 嘉宾级别
     * contactPerson : 联络人姓名
     * isvip : 联络人类别
     * oneTooneServe : 是否重要嘉宾
     * leadAttention : 是否领导关注
     * zifei : 是否自费
     */

    private JbxxInfoBean jbxxInfo;//嘉宾信息----》右侧
    /**
     * userjuese : 6
     * name : 张哲
     * userid : fw_zhangzhe
     * mobile : 13223232323
     * workplace : ceshi
     * ry_userId : fw_zhangzhe
     * ry_token : 234ewrtt!ddd
     * ry_imgUrl :
     * ry_name : 张哲
     */

    private FwInfoBean fwInfo;//服务人员信息
    /**
     * name : 姓名
     * mobile : 手机号
     * workplace : 工作单位
     * job : 职位
     * sex : 性别
     * zhengjiancode : 证件号码
     * guoji : 国籍
     * jiguan : 籍贯
     * ethnic : 民族
     * religion : 宗教信仰
     * contactPerson : 联络人姓名
     * contactPhone : 联络人电话
     * contactWorkPlace : 联络人单位
     * contactJob : 联络人职务
     * rating : 嘉宾级别
     * isvip : 嘉宾类别
     * oneTooneServe : 是否重要嘉宾
     * leadAttention : 是否领导关注
     * zifei : 是否自费
     */

    private KvInfoBean kvInfo;//嘉宾信息左侧

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

    public JbjbInfoBean getJbjbInfo() {
        return jbjbInfo;
    }

    public void setJbjbInfo(JbjbInfoBean jbjbInfo) {
        this.jbjbInfo = jbjbInfo;
    }

    public JbxxInfoBean getJbxxInfo() {
        return jbxxInfo;
    }

    public void setJbxxInfo(JbxxInfoBean jbxxInfo) {
        this.jbxxInfo = jbxxInfo;
    }

    public FwInfoBean getFwInfo() {
        return fwInfo;
    }

    public void setFwInfo(FwInfoBean fwInfo) {
        this.fwInfo = fwInfo;
    }

    public KvInfoBean getKvInfo() {
        return kvInfo;
    }

    public void setKvInfo(KvInfoBean kvInfo) {
        this.kvInfo = kvInfo;
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
        private String photourl;
        private String photourlbig;
        private String ry_userId;
        private String ry_name;
        private String ry_token;
        private String ry_imgUrl;

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
    }

    /**
     * 嘉宾信息---》右侧
     */
    public static class JbxxInfoBean {
        private String mobile;
        private String sex;
        private String zhengjiancode;
        private String guoji;
        private String jiguan;
        private String ethnic;
        private String religion;
        private String contactPerson;
        private String contactPhone;
        private String contactWorkPlace;
        private String contactJob;
        private String rating;
        private String isvip;
        private String oneTooneServe;
        private String leadAttention;
        private String photourlbig;//该字段在左侧信息没有
        private String zifei;

       
        public String getGuoji() {
            return guoji;
        }

        public void setGuoji(String guoji) {
            this.guoji = guoji;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getZhengjiancode() {
            return zhengjiancode;
        }

        public void setZhengjiancode(String zhengjiancode) {
            this.zhengjiancode = zhengjiancode;
        }

        public String getEthnic() {
            return ethnic;
        }

        public void setEthnic(String ethnic) {
            this.ethnic = ethnic;
        }

        public String getJiguan() {
            return jiguan;
        }

        public void setJiguan(String jiguan) {
            this.jiguan = jiguan;
        }

        public String getContactJob() {
            return contactJob;
        }

        public void setContactJob(String contactJob) {
            this.contactJob = contactJob;
        }

        public String getReligion() {
            return religion;
        }

        public void setReligion(String religion) {
            this.religion = religion;
        }

        public String getPhotourlbig() {
            return photourlbig;
        }

        public void setPhotourlbig(String photourlbig) {
            this.photourlbig = photourlbig;
        }

        public String getContactWorkPlace() {
            return contactWorkPlace;
        }

        public void setContactWorkPlace(String contactWorkPlace) {
            this.contactWorkPlace = contactWorkPlace;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getContactPerson() {
            return contactPerson;
        }

        public void setContactPerson(String contactPerson) {
            this.contactPerson = contactPerson;
        }

        public String getIsvip() {
            return isvip;
        }

        public void setIsvip(String isvip) {
            this.isvip = isvip;
        }

        public String getOneTooneServe() {
            return oneTooneServe;
        }

        public void setOneTooneServe(String oneTooneServe) {
            this.oneTooneServe = oneTooneServe;
        }

        public String getLeadAttention() {
            return leadAttention;
        }

        public void setLeadAttention(String leadAttention) {
            this.leadAttention = leadAttention;
        }

        public String getZifei() {
            return zifei;
        }

        public void setZifei(String zifei) {
            this.zifei = zifei;
        }
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
    }

    /**
     * 嘉宾信心---》左侧
     */
    public static class KvInfoBean {
        private String name;
        private String mobile;
        private String workplace;
        private String job;
        private String sex;
        private String zhengjiancode;
        private String guoji;
        private String jiguan;
        private String ethnic;
        private String religion;
        private String contactPerson;
        private String contactPhone;
        private String contactWorkPlace;
        private String contactJob;
        private String rating;
        private String isvip;
        private String oneTooneServe;
        private String leadAttention;
        private String zifei;

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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getZhengjiancode() {
            return zhengjiancode;
        }

        public void setZhengjiancode(String zhengjiancode) {
            this.zhengjiancode = zhengjiancode;
        }

        public String getGuoji() {
            return guoji;
        }

        public void setGuoji(String guoji) {
            this.guoji = guoji;
        }

        public String getJiguan() {
            return jiguan;
        }

        public void setJiguan(String jiguan) {
            this.jiguan = jiguan;
        }

        public String getEthnic() {
            return ethnic;
        }

        public void setEthnic(String ethnic) {
            this.ethnic = ethnic;
        }

        public String getReligion() {
            return religion;
        }

        public void setReligion(String religion) {
            this.religion = religion;
        }

        public String getContactPerson() {
            return contactPerson;
        }

        public void setContactPerson(String contactPerson) {
            this.contactPerson = contactPerson;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getContactWorkPlace() {
            return contactWorkPlace;
        }

        public void setContactWorkPlace(String contactWorkPlace) {
            this.contactWorkPlace = contactWorkPlace;
        }

        public String getContactJob() {
            return contactJob;
        }

        public void setContactJob(String contactJob) {
            this.contactJob = contactJob;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getIsvip() {
            return isvip;
        }

        public void setIsvip(String isvip) {
            this.isvip = isvip;
        }

        public String getOneTooneServe() {
            return oneTooneServe;
        }

        public void setOneTooneServe(String oneTooneServe) {
            this.oneTooneServe = oneTooneServe;
        }

        public String getLeadAttention() {
            return leadAttention;
        }

        public void setLeadAttention(String leadAttention) {
            this.leadAttention = leadAttention;
        }

        public String getZifei() {
            return zifei;
        }

        public void setZifei(String zifei) {
            this.zifei = zifei;
        }
    }
}

