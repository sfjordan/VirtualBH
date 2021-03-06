package com.vbh.virtualboathouse;

import com.google.gson.annotations.SerializedName;

public class ReturnedResultsModel {

	@SerializedName("athletes")
	private int[] athleteIDs;
	@SerializedName("distance")
	private int distance;
	@SerializedName("time")
	private long time;
	@SerializedName("piece")
	private int piece;
	@SerializedName("datetime")
	private long dateTime;
	
	
	public ReturnedResultsModel(int[] athleteIDs, int distance, long dateTime, long time, int piece) {
		this.athleteIDs = athleteIDs;
		this.distance = distance;
		this.dateTime = dateTime;
		this.piece = piece;
		this.time = time;
	}
	
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int[] getAthleteIDs() {
		return athleteIDs;
	}
	public void setAthleteIDs(int[] athleteIDs) {
		this.athleteIDs = athleteIDs;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public long getDateTime() {
		return dateTime;
	}
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}
	public int getPiece() {
		return piece;
	}
	public void setPiece(int piece) {
		this.piece = piece;
	}
	
	
}
