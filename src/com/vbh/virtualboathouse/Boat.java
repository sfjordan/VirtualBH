package com.vbh.virtualboathouse;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.vbh.virtualboathouse.BoatModel.BoatFields;

public class Boat implements Serializable, Parcelable {
	
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(coxed ? 1 : 0);
		dest.writeInt(numSeats);
		dest.writeString(name);
		dest.writeInt(boatID);
	}
	public static final Parcelable.Creator<Boat> CREATOR = new Parcelable.Creator<Boat>() {
		public Boat createFromParcel(Parcel pc) {
			return new Boat(pc);
		}
		public Boat[] newArray(int size) {
			return new Boat[size];
		}
	};
	public Boat(Parcel pc) {
		coxed    = (pc.readInt() == 1);
		numSeats = pc.readInt();
		name     = pc.readString();
		boatID   = pc.readInt();
	}
	

}
