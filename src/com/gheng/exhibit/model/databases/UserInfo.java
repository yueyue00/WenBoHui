package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "userinfo")
public class UserInfo extends BaseModel
{
	@Column(column = "SURNAME")
	private String surname;

	@Column(column = "NAME")
	private String name;
	@Column(column = "MOBILE")
	private String mobile;

	@Column(column = "SNUM")
	private String snum;
	
	@Column(column = "UNAME")
	private String uname;

	@Column(column = "VERIFIED")
	private int verified;

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getSnum()
	{
		return snum;
	}

	public void setSnum(String snum)
	{
		this.snum = snum;
	}

	public int getVerified()
	{
		return verified;
	}

	public void setVerified(int verified)
	{
		this.verified = verified;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

}
