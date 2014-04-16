package com.vbh.virtualboathouse;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class PracticeLineupsModel implements Serializable {

	@SerializedName("pk")
	private long pk; // private key
	
	@SerializedName("model")
	private String model;
	
	public String getModel() {
		return model;
	}
	
	public long getPrivateKey() {
		return pk;
	}
	@SerializedName("fields")
	private PracticeLineupsFields fields;
	
	public class PracticeLineupsFields {
		@SerializedName("position")
		private String position;
		@SerializedName("practice")
		private int practice;
		@SerializedName("athletes")
		private int[] athleteIDs;
		@SerializedName("boat")
		private int boatID;
		
		public String getPosition() {
			return position;
		}
		public int getPractice() {
			return practice;
		}
		public int[] getAthleteIDs() {
			return athleteIDs;
		}
		public int getBoatID() {
			return boatID;
		}

	}
}
