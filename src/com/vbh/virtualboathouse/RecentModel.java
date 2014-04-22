package com.vbh.virtualboathouse;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class RecentModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2358978964496258979L;
	
	@SerializedName("id")
	private int id;
	
	public int getPracticeID() {
		return id;
	}

}
