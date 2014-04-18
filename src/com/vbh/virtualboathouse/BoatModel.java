package com.vbh.virtualboathouse;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class BoatModel implements Serializable {

	/**
	 * Constant for serialization
	 */
	private static final long serialVersionUID = -276679995740520294L;
	

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
	
	public BoatFields getBoatFields() {
		return fields;
	}
	
	@SerializedName("fields")
	private BoatFields fields;
	
	public class BoatFields implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5909140212880311212L;
		@SerializedName("coxed")
		private boolean coxed;
		@SerializedName("name")
		private String name;
		@SerializedName("seats")
		private int seats;
		
		public boolean isCoxed() {
			return coxed;
		}
		public String getName() {
			return name;
		}
		public int getSeats() {
			return seats;
		}
		
	}

	
}
