package com.vbh.virtualboathouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Practice implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1118024584163974421L;
	private ArrayList<Piece> pieces;
	private ArrayList<String> notes;
	private int windSpeed;
	private String windNotes;
	private String weather;
	private int temperature;
	private final long practiceID;
	private final long msTime;
	private final Date date;
	
	public Practice() {
		practiceID = 10L;
		msTime = System.currentTimeMillis();
		date = new Date(msTime);
	}
	
	public long getMsTime() {
		return msTime;
	}

	public Date getDate() {
		return date;
	}

	public long getPracticeID() {
		return practiceID;
	}
	
	public void addPiece(Piece piece) {
		pieces.add(piece);
	}
	
	public void addNote(String note) {
		notes.add(note);
	}
	
	public int getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getWindNotes() {
		return windNotes;
	}

	public void setWindNotes(String windNotes) {
		this.windNotes = windNotes;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	public ArrayList<String> getNotes() {
		return notes;
	}
	
	
}
