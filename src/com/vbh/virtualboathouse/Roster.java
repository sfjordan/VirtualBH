package com.vbh.virtualboathouse;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Roster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 208984644974264063L;
	
	private Map<Integer, Athlete> roster;
	public Roster(AthleteModel[] am) {
		roster = new HashMap<Integer, Athlete>();
		for(AthleteModel athlete : am) {
			Athlete a = new Athlete(athlete);
			// put in roster
		}
	}
	
	public Athlete getAthlete(int athleteID) {
		return roster.get(athleteID);
	}
}
