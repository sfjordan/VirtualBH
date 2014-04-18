package com.vbh.virtualboathouse;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class AthleteModel implements Serializable  {
	
	/**
	 * Constant for serialization
	 */
	private static final long serialVersionUID = 4484245226273940400L;
	
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
	private AthleteFields fields;
	
	public AthleteFields getAthleteFields() {
		return fields;
	}
	public class AthleteFields implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4032030594628423875L;
		
		@SerializedName("name")
		private String name;
		@SerializedName("year")
		private String year;
		@SerializedName("side")
		private String side;
		@SerializedName("api_key")
		private String apiKey;
		@SerializedName("role")
		private String role;
		@SerializedName("status")
		private String status;
		@SerializedName("user")
		private int userID;
		@SerializedName("height")
		private int height;
		
		public String getName() {
			return name;
		}
		public String getYear() {
			return year;
		}
		public String getSide() {
			return side;
		}
		public String getApiKey() {
			return apiKey;
		}
		public String getRole() {
			return role;
		}
		public String getStatus() {
			return status;
		}
		public int getUserID() {
			return userID;
		}
		public int getHeight() {
			return height;
		}
		
	}

}
