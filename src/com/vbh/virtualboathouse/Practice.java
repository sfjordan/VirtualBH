package com.vbh.virtualboathouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.util.SparseArray;

public class Practice implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1118024584163974421L;
	private Map<Long, Piece> pieces;
	private ArrayList<String> notes;
	private int windSpeed;
	private String windNotes;
	private String weather;
	private int temperature;
	private final long practiceID;
	private final long msTime;
	private final Date date;
	
	public Practice(int practiceID) {
		this.practiceID = practiceID;
		msTime = System.currentTimeMillis();
		date = new Date(msTime);
		pieces = new HashMap<Long, Piece>();
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
		pieces.put(piece.getPieceID(), piece);
	}
	public Piece getPiece(long pieceID) {
		return pieces.get(pieceID);
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
