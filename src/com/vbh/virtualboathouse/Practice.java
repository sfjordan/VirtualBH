package com.vbh.virtualboathouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
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
	private boolean hasCurrentLineup;
	private Map<Long, Lineup> practiceLineups; // all the lineups that have been used during this practice
	private Map<Long, Lineup> currentLineups; // the current lineups on the water
	
	
	public Practice(int practiceID) {
		this.practiceID = practiceID;
		msTime = System.currentTimeMillis();
		date = new Date(msTime);
		pieces = new HashMap<Long, Piece>();
		hasCurrentLineup = false;
		practiceLineups = new HashMap<Long, Lineup>();
		currentLineups = new HashMap<Long, Lineup>();
	}
	
	// these are for adjusting the lineups for the whole practice 
	public void addLineup(Lineup l) {
		practiceLineups.put(l.getLineupID(), l);
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
		hasCurrentLineup = true;
		practiceLineups.put(l.getLineupID(), l);
		currentLineups.put(l.getLineupID(), l);
	}
	public Map<Long, Lineup> getCurrentLineups() {
		hasCurrentLineup = false;
		return currentLineups;
	}
	
	public boolean hasCurrentLineup(){
		return hasCurrentLineup;
	}
	public Lineup getCurrentLineup(long id) {
		return currentLineups.get(id);
	}
	
	public ArrayList<Lineup> getCurrentLineupsList(){
		ArrayList<Lineup> lineups = new ArrayList<Lineup>();
		Iterator<Entry<Long, Lineup>> currentLineups = getCurrentLineups().entrySet().iterator();
    	while(currentLineups.hasNext()){
    		lineups.add(currentLineups.next().getValue());			        		    		
    	}
    	return lineups;
	}
	
	public ArrayList<Long> getCurrentLineupIDList(){
		ArrayList<Long> lineupIDs = new ArrayList<Long>();
		for (Lineup l:getCurrentLineupsList()){
			lineupIDs.add(l.getLineupID());
		}
		return lineupIDs;
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
	}
	public Piece getPiece(long pieceID) {
		return pieces.get(pieceID);
	}
	public Map<Long, Piece> getAllPieces(){
		return pieces;
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

	public Map<Long, Piece> getPieces() {
		return pieces;
	}

	public ArrayList<String> getNotes() {
		return notes;
	}
	
	
}
