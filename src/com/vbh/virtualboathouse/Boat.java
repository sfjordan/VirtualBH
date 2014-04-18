package com.vbh.virtualboathouse;

import java.io.Serializable;

import com.vbh.virtualboathouse.BoatModel.BoatFields;

public class Boat implements Serializable {
	
	/**
	 * constant fo serialization
	 */
	private static final long serialVersionUID = 3645111220055307714L;
	
	private final boolean coxed;
	private final int numSeats;
	private final String name;
	private final int boatID;
	
	public Boat(BoatModel bm) {
		BoatFields bf = bm.getBoatFields();
		this.coxed = bf.isCoxed();
		this.numSeats = bf.getSeats();
		this.name = bf.getName();
		this.boatID = bm.getPrivateKey();
	}

	public int getBoatID() {
		return boatID;
	}
	public boolean isCoxed() {
		return coxed;
	}

	public int getNumSeats() {
		return numSeats;
	}

	public String getName() {
		return name;
	}
	

}
