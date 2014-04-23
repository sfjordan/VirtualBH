package com.vbh.virtualboathouse;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.util.SparseArray;

public class Roster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 208984644974264063L;
	
	private Map<Integer, Athlete> roster; // ID, Athlete
	public Roster(AthleteModel[] am) {
		roster = new HashMap<Integer, Athlete>();
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
