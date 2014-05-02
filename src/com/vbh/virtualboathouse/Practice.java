package com.vbh.virtualboathouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;

public class Practice implements Serializable {
	
	/**
	 * constant for serialization
	 */
	private static final long serialVersionUID = 1118024584163974421L;
	private Map<Long, Piece> pieces;
	private ArrayList<String> notes;
	private int windSpeed;
	private String windNotes;
	private String weather;
	private int temperature;
	private final int practiceID;
	private final long msTime;
	private final Date date;
	private Map<Long, Lineup> practiceLineups; // all the lineups that have been used during this practice
	private Map<Long, Lineup> currentLineups; // the current lineups on the water
	
	private SharedPreferences sharedPref;
	
	public Practice(int practiceID) {
		this.practiceID = practiceID;
		msTime = System.currentTimeMillis();
		date = new Date(msTime);
		pieces = new HashMap<Long, Piece>();
		practiceLineups = new HashMap<Long, Lineup>();
		currentLineups = new HashMap<Long, Lineup>();
	}
	
	// these are for adjusting the lineups for the whole practice 
	public void addLineup(Lineup l) {
		practiceLineups.put(l.getLineupID(), l);
		notifyDataChange();
	}
	
	public Map<Long, Lineup> getLineups() {
		return practiceLineups;
	}
	
	public Lineup getLineup(long id) {
		return practiceLineups.get(id);
	}
	
	// these are for adjusting the current Lineups on the water
	public void clearCurrentLineups() {
		currentLineups = new HashMap<Long, Lineup>();
	}
	public void addCurrentLineup(Lineup l) {
		practiceLineups.put(l.getLineupID(), l);
		currentLineups.put(l.getLineupID(), l);
		notifyDataChange();
	}
	public Map<Long, Lineup> getCurrentLineups() {
		return currentLineups;
	}
	public Lineup getCurrentLineup(long id) {
		return currentLineups.get(id);
	}
	
	
	public long getMsTime() {
		return msTime;
	}

	public Date getDate() {
		return date;
	}

	public int getPracticeID() {
		return practiceID;
	}
	
	public void addPiece(Piece piece) {
		pieces.put(piece.getPieceID(), piece);
		notifyDataChange();
	}
	public Piece getPiece(long pieceID) {
		return pieces.get(pieceID);
	}
	
	public void addNote(String note) {
		notes.add(note);
		notifyDataChange();
	}
	
	public int getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
		notifyDataChange();
	}

	public String getWindNotes() {
		return windNotes;
	}

	public void setWindNotes(String windNotes) {
		this.windNotes = windNotes;
		notifyDataChange();
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
		notifyDataChange();
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
		notifyDataChange();
	}

	public Map<Long, Piece> getPieces() {
		return pieces;
	}

	public ArrayList<String> getNotes() {
		return notes;
	}
	
	public void notifyDataChange(){
		sharedPref.edit().putBoolean("DATA_SET_CHANGED", true).apply();
	}
	
	
}
