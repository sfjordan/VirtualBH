package com.vbh.virtualboathouse;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.vbh.virtualboathouse.PracticeLineupsModel.PracticeLineupsFields;

public class LineupModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8225395081254697034L;

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
	private LineupFields fields;
	
	public LineupFields getLineupFields() {
		return fields;
	}
	
	public class LineupFields implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7080499643087789500L;
		
		@SerializedName("position")
		private String position;
		@SerializedName("piece")
		private int piece;

		@SerializedName("boat")
		private int boatID;
		
		public String getPosition() {
			return position;
		}
		/**
		 * This should not be accessed. The piece id sent over JSON is just a Sentinel and does not
		 * represent anything important.
		 * @return
		 */
		private int getPiece() {
			return piece;
		}

		public int getBoatID() {
			return boatID;
		}

		
		
	}
}
