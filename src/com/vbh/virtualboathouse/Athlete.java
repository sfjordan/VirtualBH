package com.vbh.virtualboathouse;

import java.io.Serializable;

import com.vbh.virtualboathouse.AthleteModel.AthleteFields;

public class Athlete implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7530572379516212709L;
	
	private final String name;
	private final String firstInitLastName;
	private final String year;
	private final String side;
	private final String status;
	private final int athleteID;
	private final int height;
	private final String role;

	public Athlete(AthleteModel am) {
		AthleteFields fields = am.getAthleteFields();
		this.name      = fields.getName();
		this.side      = fields.getSide();
		this.role      = fields.getRole();
		this.athleteID = fields.getUserID();
		this.height    = fields.getHeight();
		this.year      = fields.getYear();
		this.status    = fields.getStatus();
		if (name.contains(" ")) {
			String firstInit = name.substring(0, 1);
			String lastName = name.substring(name.lastIndexOf(" ")+1);
			firstInitLastName = firstInit + ". " + lastName;
		}
		else {
			firstInitLastName = name;
		}
		
	}
	
	public String getName() {
		return name;
	}

	public String getFirstInitLastName() {
		return firstInitLastName;
	}

	public String getYear() {
		return year;
	}

	public String getSide() {
		return side;
	}

	public String getStatus() {
		return status;
	}

	public int getAthleteID() {
		return athleteID;
	}

	public int getHeight() {
		return height;
	}	
	
	
	
}
