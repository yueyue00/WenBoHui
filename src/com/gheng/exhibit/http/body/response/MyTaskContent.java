package com.gheng.exhibit.http.body.response;

import java.util.List;


import com.google.gson.annotations.Expose;

public class MyTaskContent {
	 /**
     * groupname : 9月19日  星期三
     * groupcontent : [{"content":"参加文博会开幕式"},{"content":"参加敦煌文化年展高峰论坛会议"},{"content":"参加探索丝绸之路沿线国家文化交流合作途径"},{"content":"观看《丝路花雨》"},{"content":"参加中外领导人招待晚宴"}]
     */
    
    private String groupname;//按日期分组
    /**
     * content : 参加文博会开幕式
     */
    
    private List<GroupcontentBean> groupcontent;//按日期分组对应的数据

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
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return "groupname="+groupname+",groupcontent="+groupcontent.toString();
    }
    public static class GroupcontentBean {
    	private String id;
        public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
		private String content;//具体的每一项内容
        private boolean isChecked;

        public boolean isChecked() {
			return isChecked;
		}

		public void setChecked(boolean isChecked) {
			this.isChecked = isChecked;
		}

		public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
        @Override
        public String toString() {
        	// TODO Auto-generated method stub
        	return "content:"+content;
        }
    }
}
