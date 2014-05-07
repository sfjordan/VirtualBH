package com.vbh.virtualboathouse;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class LineupArrayModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1700055254511707996L;
	
	@SerializedName("athletes")
	private int[] athleteIDs;
	
	public int[] getAthleteIDs() {
		return athleteIDs;
	}
}
