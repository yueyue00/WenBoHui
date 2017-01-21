package com.gheng.exhibit.http.body.response;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Driver_Vip implements Serializable {
	@Expose
	private String id;
	@Expose
	private String name;
	@Expose
	private String iconPath;
	@Expose
	private String workPlace;
	@Expose
	private String job;
	@Expose
	private String companyPerson;
	@Expose
	private String cpMobile;
	@Expose
	private String mobile;
	@Expose
	private boolean isCheck;
	
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCompanyPerson() {
		return companyPerson;
	}

	public void setCompanyPerson(String companyPerson) {
		this.companyPerson = companyPerson;
	}

	public String getCpMobile() {
		return cpMobile;
	}

	public void setCpMobile(String cpMobile) {
		this.cpMobile = cpMobile;
	}

	@Override
	public String toString() {
		return "Driver_Vip [name=" + name + ", iconPath=" + iconPath
				+ ", workPlace=" + workPlace + ", job=" + job
				+ ", companyPerson=" + companyPerson + ", cpMobile=" + cpMobile
				+ "]";
	}

}
