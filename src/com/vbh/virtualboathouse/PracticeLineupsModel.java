package com.vbh.virtualboathouse;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class PracticeLineupsModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7633689448845229342L;

	@SerializedName("pk")
	private int pk; // private key
	
	@SerializedName("model")
	private String model;
	
	public String getModel() {
		return model;
	}
	
	public int getPrivateKey() {
		return pk;
	}
	@SerializedName("fields")
	private PracticeLineupsFields fields;
	
	public PracticeLineupsFields getPLFields() {
		return fields;
	}
	public class PracticeLineupsFields implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3518434490680826559L;
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
