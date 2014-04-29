package com.vbh.virtualboathouse;

import com.google.gson.annotations.SerializedName;

public class ReturnedLineupModel {

	@SerializedName("athletes")
	private int[] athleteIDs;
	@SerializedName("position")
	private String position;
	@SerializedName("boat")
	private int boatID;
	@SerializedName("piece")
	private int piece;
	
	
	public ReturnedLineupModel(int[] athleteIDs, String position, int boatID, int piece) {
		this.athleteIDs = athleteIDs;
		this.position = position;
		this.boatID = boatID;
		this.piece = piece;
	}
	
	public String getPosition() {
		return position;
	}
	private int getPiece() {
		return piece;
	}
	public int[] getAthleteIDs() {
		return athleteIDs;
	}
	public int getBoatID() {
		return boatID;
	}
	public void setAthleteIDs(int[] athleteIDs) {
		this.athleteIDs = athleteIDs;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public void setBoatID(int boatID) {
		this.boatID = boatID;
	}
	public void setPiece(int piece) {
		this.piece = piece;
	}
	
}
