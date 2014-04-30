package com.vbh.virtualboathouse;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
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
	private int[] athleteID; // from stern to bow (0=stroke, 1=7-seat, etc.)
	private int[] allAthleteID; // includes the coxswain and is from stern to bow (1=stroke, 2=7-seat, etc.)
	private String position;
	private Roster roster;
	private final long lineupID;
	
	public Lineup(LineupModel lm, Roster roster, Map<Integer, Boat> boats){
		//this.lineupID = lm.getPrivateKey();
		this.lineupID = UUID.randomUUID().getLeastSignificantBits();
		this.roster = roster;
		LineupFields lmf = lm.getLineupFields();
		this.allAthleteID = new int[lmf.getAthleteIDs().length];
		int k = 0;
		for(int id : lmf.getAthleteIDs()) {
			this.allAthleteID[k] = id;
			k++;
		}
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
	}
	
	public Lineup(LinkedList<AthleteListName> lineup, Roster roster,Map<Integer,Boat> boats){
		//new constructor
		this.lineupID = UUID.randomUUID().getLeastSignificantBits();
		this.roster = roster;
		AthleteListName title = lineup.peekFirst();
		this.boat = boats.get(title.getboatID());
		this.position = title.getPosition();
		//Log.i("constructor","lineup.size: "+lineup.size());
		//build allAthleteID
		int n = 0;
		if(boat.isCoxed()){
			allAthleteID = new int[lineup.size()];
			//Log.i("setup allAthleteID","allAthleteID["+n+"] = "+title.getathID());
			allAthleteID[n] = title.getathID();
			n++;
		}
		else allAthleteID = new int[lineup.size()-1];
		for(AthleteListName aln : lineup){
			if(aln.isAthlete()){
				//Log.i("setup allAthleteID","allAthleteID["+n+"] = "+aln.getathID());
				allAthleteID[n] = aln.getathID();
				n++;
			}
		}
		/*for(int i = 0; i < allAthleteID.length; i++){
			allAthleteID[i]=lineup.pop().getathID();
		}*/
		//build athleteID
		//build athleteNames
		//set coxwain
		if(boat.isCoxed()){
			this.coxswain = roster.getAthlete(allAthleteID[0]);
			this.athleteNames = new String[allAthleteID.length - 1];
			this.athleteID = new int[allAthleteID.length - 1];
		    for (int i = allAthleteID.length-1; i > 0; i--) {
		    	this.athleteID[i-1] = allAthleteID[i];
		    	//Log.i("setup names","allAthleteID["+i+"] is null: "+((Integer)allAthleteID[i]==null));
		    	//Log.i("setup names","allAthleteID["+i+"] is: "+((Integer)allAthleteID[i]));
		    	//Log.i("setup names","roster.getAthlete(allAthleteID["+i+"]) is null: "+(roster.getAthlete(allAthleteID[i])==null));
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
	}
	
	public void printAthleteIDs(){
		for(int i = 0; i < athleteID.length; i++)
		Log.i("printAthleteIDs","athleteID["+i+"] = "+athleteID[i]);
	}
	
	public void printLineup(){
		Log.i("printLineup","Boat: "+boat.getName());
		if(coxswain!=null)
			Log.i("printLineup","Cox: "+coxswain.getFirstInitLastName());
		int n = 0;
		for(int id : athleteID){
			/*Log.i("printLineup","athleteID: "+id);
			Log.i("printLineup","roster is null: "+(roster == null));
			Log.i("printLineup","roster.getAthlete("+id+") is null: "+(roster.getAthlete(id)==null));*/
			Log.i("printLineup","rower "+n+": "+roster.getAthlete(id).getFirstInitLastName()+", id: "+id);
			n++;
		}
	}
	
	public boolean isCoxed(){
		return boat.isCoxed();
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
	
	public String getStrokeInitLast(){
		if (athleteID.length == 0)
			return "";
		else return roster.getAthlete(athleteID[0]).getFirstInitLastName();
	}
	
	public int getCoxswainID(){
		return allAthleteID[0];
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
		return getStrokeInitLast();
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
