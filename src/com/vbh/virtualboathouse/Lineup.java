package com.vbh.virtualboathouse;

import java.io.Serializable;

import android.util.SparseArray;

import com.vbh.virtualboathouse.PracticeLineupsModel.PracticeLineupsFields;

public class Lineup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2384016072304205878L;
	
	private Boat boat;
	@SuppressWarnings("unused")
	private Athlete coxswain;
	private String[] athleteNames;
	private int[] athleteID;
	private String position;
	private final int lineupID;
	
	public Lineup(PracticeLineupsModel plm, Roster roster, SparseArray<Boat> boats){
		this.lineupID = plm.getPrivateKey();
		PracticeLineupsFields plmf = plm.getPLFields();
		int[] tempAthletes = plmf.getAthleteIDs();
		this.position = plmf.getPosition();
		this.boat = boats.get(plmf.getBoatID());
		if (boat.isCoxed()) {
			this.coxswain = roster.getAthlete(tempAthletes[0]);
			this.athleteNames = new String[tempAthletes.length - 1];
		    for (int i = 1; i < tempAthletes.length; i++) {
		    	this.athleteID[i-1] = tempAthletes[i];
		    	this.athleteNames[i-1] = roster.getAthlete(tempAthletes[i]).getFirstInitLastName();
		    }
		}
		else {
			coxswain = null;
			int i = 0;
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
		return athleteID[seat];
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
	
}
