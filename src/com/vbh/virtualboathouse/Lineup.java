package com.vbh.virtualboathouse;

import java.io.Serializable;

public class Lineup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2384016072304205878L;
	private Boat boat;
	private Athlete coxswain;
	private String[] athleteNames;
	private int[] athleteID;
	private String position;
	private final long lineupID;
	
	public Lineup(){
		lineupID = 10L;
		
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
	public int getAthleteIDFromSeat(int seat) {
		return athleteID[seat - 1];
	}
	
	public Athlete getAthleteFromSeat(int seat, Roster roster) {
		return roster.getAthlete(getAthleteIDFromSeat(seat - 1));
	}
	
}
