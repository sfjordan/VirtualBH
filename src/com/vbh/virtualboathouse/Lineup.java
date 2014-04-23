package com.vbh.virtualboathouse;

import java.io.Serializable;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
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
	private int[] athleteID;
	private String position;
	private final int lineupID;
	
	public Lineup(LineupModel lm, Roster roster, Map<Integer, Boat> boats){
		this.lineupID = lm.getPrivateKey();
		LineupFields lmf = lm.getLineupFields();
		int[] tempAthletes = lmf.getAthleteIDs();
		this.position = lmf.getPosition();
		this.boat = boats.get(lmf.getBoatID());
		if (boat.isCoxed()) {
			this.coxswain = roster.getAthlete(tempAthletes[0]);
			this.athleteNames = new String[tempAthletes.length - 1];
			this.athleteID = new int[tempAthletes.length - 1];
		    for (int i = 1; i < tempAthletes.length; i++) {
		    	this.athleteID[i-1] = tempAthletes[i];
		    	this.athleteNames[i-1] = roster.getAthlete(tempAthletes[i]).getFirstInitLastName();
		    }
		}
		else {
			coxswain = null;
			int i = 0;
			this.athleteNames = new String[tempAthletes.length];
			this.athleteID = new int[tempAthletes.length];
			for (int athleteID : tempAthletes) {
		    	this.athleteID[i] = athleteID;
		    	this.athleteNames[i] = roster.getAthlete(athleteID).getFirstInitLastName();
		    	i++;
		    }
		}
	}
	
	public int getLineupID() {
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
	public int getAthleteIDFromSeat(int seat) {
		if (seat < 1 || seat > boat.getNumSeats()) {
			return -1;
		}
		return athleteID[seat-1];
	}
	
	public Athlete getAthleteFromSeat(int seat, Roster roster) {
		return roster.getAthlete(getAthleteIDFromSeat(seat));
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
		dest.writeInt(lineupID);
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
		lineupID = pc.readInt();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
