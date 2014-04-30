package com.vbh.virtualboathouse;

import com.google.gson.annotations.SerializedName;

public class PieceModel {

	@SerializedName("practice")
	private int practiceID;
	@SerializedName("name")
	private String name;
	
	@SerializedName("datetime")
	private long dateTime;

	
	public PieceModel(int practiceID, long pieceID, long dateTime) {
		this.practiceID = practiceID;
		name = Long.toString(pieceID);
		this.dateTime = dateTime;
	}
	public int getPracticeID() {
		return practiceID;
	}

	public void setPracticeID(int practiceID) {
		this.practiceID = practiceID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	
}
