package com.vbh.virtualboathouse;

import java.io.Serializable;

import android.util.SparseArray;

public class Roster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 208984644974264063L;
	
	private SparseArray<Athlete> roster; // ID, Athlete
	public Roster(AthleteModel[] am) {
		roster = new SparseArray<Athlete>();
		for(AthleteModel athlete : am) {
			Athlete a = new Athlete(athlete);
			// put in roster
			roster.put(a.getAthleteID(), a);
		}
	}
	
	public Athlete getAthlete(int athleteID) {
		return roster.get(athleteID);
	}
}
