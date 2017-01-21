package com.gheng.exhibit.model.databases;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 
 * @author lileixing
 */
@Table(name = "LanguageStrings")
public class Language extends BaseModel {

	public static final int ZH = 1; // 中文
	public static final int EN = 2; // 英语
	public static final int TW = 3; // 繁体中文

	@Column(column = "LanguageStringZh")
	private String zhName;

	@Column(column = "LanguageStringEn")
	private String enName;

	@Column(column = "LanguageStringTW")
	private String twName;

	/**
	 * @return the chName
	 */
	public String getZhName() {
		return zhName;
	}

	/**
	 * @param chName
	 *            the chName to set
	 */
	public void setZhName(String zhName) {
		this.zhName = zhName;
	}

	/**
	 * @return the enName
	 */
	public String getEnName() {
		return enName;
	}

	/**
	 * @param enName
	 *            the enName to set
	 */
	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getTwName() {
		return twName;
	}

	public void setTwName(String twName) {
		this.twName = twName;
	}

	@Override
	public String toString() {
		return "Language [zhName=" + zhName + ", enName=" + enName + ", id="
				+ id + "]";
	}

}
