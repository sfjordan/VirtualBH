package com.vbh.virtualboathouse;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class PracticeModel implements Serializable {

	/**
	 * Constant for serialization
	 */
	private static final long serialVersionUID = 3010218776321427340L;


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
	private PracticeFields fields;
	
	public class PracticeFields {
		@SerializedName("workout")
		private String workout;
		@SerializedName("name")
		private String name;
		@SerializedName("datetime")
		private String datetime;
		
		public String getWorkout() {
			return workout;
		}
		public String getName() {
			return name;
		}
		public String getDateTime() {
			return datetime;
		}

	}
}
