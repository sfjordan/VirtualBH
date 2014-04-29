package com.vbh.virtualboathouse;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;

import com.vbh.virtualboathouse.LineupModel.LineupFields;
import com.vbh.virtualboathouse.PracticeLineupsModel.PracticeLineupsFields;

public class Lineup implements Serializable, Parcelable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2384016072304205878L;
	
	private Boat boat;
	private Athlete coxswain;
	private String[] athleteNames;
	private int[] athleteID; // from bow to stern (0=bow, 1=2-seat, etc.)
	private int[] allAthleteID; // includes the coxswain and is from stern to bow (0=cox, 1=stroke, 2=7-seat, etc.)
	private int lineupHash;
	private String position;
	private final long lineupID;
	
	public Lineup(LineupModel lm, Roster roster, Map<Integer, Boat> boats){
		//this.lineupID = lm.getPrivateKey();
		this.lineupID = UUID.randomUUID().getLeastSignificantBits();
		LineupFields lmf = lm.getLineupFields();
		allAthleteID = lmf.getAthleteIDs();
		this.position = lmf.getPosition();
		this.boat = boats.get(lmf.getBoatID());
		if (boat.isCoxed()) {
			this.coxswain = roster.getAthlete(allAthleteID[0]);
			this.athleteNames = new String[allAthleteID.length - 1];
			this.athleteID = new int[allAthleteID.length - 1];
		    for (int i = allAthleteID.length-1; i > 0; i--) {
		    	this.athleteID[i-1] = allAthleteID[i];
		    	this.athleteNames[i-1] = roster.getAthlete(allAthleteID[i]).getFirstInitLastName();
		    }
		}
		else {
			coxswain = null;
			int i = allAthleteID.length - 1;
			this.athleteNames = new String[allAthleteID.length];
			this.athleteID = new int[allAthleteID.length];
			for (int athleteID : allAthleteID) {
		    	this.athleteID[i] = athleteID;
		    	this.athleteNames[i] = roster.getAthlete(athleteID).getFirstInitLastName();
		    	i--;
		    }
		}
		lineupHash = athleteID.hashCode();
	}
	
	public int getBoatID() {
		return boat.getBoatID();
	}
	public long getLineupID() {
		return lineupID;
	}
	public int getNumOfSeats() {
		return athleteNames.length;
	}
	
	public String getNameForSeat(int seat) {
		return athleteNames[seat -1];
	}
	public String getPosition(){
		return position;
	}
	public String getBoatName() {
		return boat.getName();
	}
	public String getCoxswainName(){
		if (coxswain == null) return null;
		else return coxswain.getName();
	}
	public int getAthleteIDFromSeat(int seat) {
		if (seat < 1 || seat > boat.getNumSeats()) {
			return -1;
		}
		return athleteID[seat-1];
	}
	
	public int[] getAthleteIDs(){
		return athleteID;
	}
	public int[] getAllAthleteIDs() {
		return allAthleteID;
	}
	
	public Athlete getAthleteFromSeat(int seat, Roster roster) {
		int id = getAthleteIDFromSeat(seat);
		return roster.getAthlete(id);
	}
	/**
	 * This will eventually use the current settings to display the name of boat based on the user's preferences
	 * It will be able to show the first initial, last name of the stroke or the coxswain, the name of the boat
	 * or the name of the lineup. 
	 * @return The identifier for the lineup, based on the setting preference of the user. 
	 */
	public String getName() {
		//TODO add settings preference logic here!!!
		return getNameForSeat(athleteNames.length);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeSerializable(boat); // TODO use parcelable for boat
		dest.writeSerializable(coxswain); // TODO make athlete parcelable
		dest.writeStringArray(athleteNames);
		dest.writeIntArray(athleteID);
		dest.writeString(position);
		dest.writeLong(lineupID);
	}
	public static final Parcelable.Creator<Lineup> CREATOR = new Parcelable.Creator<Lineup>() {
		public Lineup createFromParcel(Parcel pc) {
			return new Lineup(pc);
		}
		public Lineup[] newArray(int size) {
			return new Lineup[size];
		}
	};
	public Lineup(Parcel pc) {
		boat = (Boat) pc.readSerializable();
		// pc.readParcelable(Boat.getClass());
		coxswain = (Athlete) pc.readSerializable();
		pc.readStringArray(athleteNames);
		pc.readIntArray(athleteID);
		position = pc.readString();
		lineupID = pc.readLong();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
