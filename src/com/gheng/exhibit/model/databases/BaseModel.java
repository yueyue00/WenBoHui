package com.gheng.exhibit.model.databases;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;

/**
 *
 * @author lileixing
 */
public abstract class BaseModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6938524795351648107L;

	@Id
	@NoAutoIncrement
	protected long id;
	
	@Column(column = "state")
	protected Integer state;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the state
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(Integer state) {
		this.state = state;
	}
}
