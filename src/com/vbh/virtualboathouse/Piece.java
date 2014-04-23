package com.vbh.virtualboathouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.util.SparseArray;

@SuppressLint("UseSparseArrays")
public class Piece implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6044365957538890403L;
	
	private final long pieceID;
	private boolean countdown;
	private boolean timed;
	private int distance;
	private String direction;
	private final Map<Integer, Lineup> lineups;
	private Map<Integer, Long> times;
	private ArrayList<String> notes;
	private String margin;
	private long msCountdownTime;
	
	public Piece (Lineup[] lineups, Roster roster, Map<Integer, Boat> boats) {
		pieceID = UUID.randomUUID().getLeastSignificantBits();
		this.lineups = new HashMap<Integer, Lineup>(lineups.length);
		for (Lineup l : lineups) {
			this.lineups.put(l.getLineupID(), l);
		}
		times = new HashMap<Integer, Long>(lineups.length);
	}
	public Piece (ArrayList<Lineup> lineups, Roster roster, Map<Integer, Boat> boats) {
		pieceID = UUID.randomUUID().getLeastSignificantBits();
		this.lineups = new HashMap<Integer, Lineup>(lineups.size());
		for (Lineup l : lineups) {
			this.lineups.put(l.getLineupID(), l);
		}
		times = new HashMap<Integer, Long>(lineups.size());
	}
	
	
	public Piece (Piece oldPiece) {
		pieceID = UUID.randomUUID().getLeastSignificantBits();
		this.lineups = oldPiece.getLineups();
		times = new HashMap<Integer, Long>(lineups.size());
	}
	public int getNumBoats() {
		return lineups.size();
	}
	public long getCountdownTime() {
		return msCountdownTime;
	}
	public void setCountdownTime(int[] times) {
		long time = times[1];
		time += times[0]*60;
		time = time*1000;
		msCountdownTime = time;
	}
	
	public long getPieceID() {
		return pieceID;
	}
	public boolean isCountdown() {
		return countdown;
	}

	public void setCountdown(boolean countdown) {
		this.countdown = countdown;
		this.timed = !countdown;
	}

	public boolean isTimed() {
		return timed;
	}

	public void setTimed(boolean timed) {
		this.timed = timed;
		this.countdown = !timed;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Map<Integer, Long> getTimes() {
		return times;
	}

	public void setTime(int lineupID, long time) {
		times.put(lineupID, time);
	}

	public ArrayList<String> getNotes() {
		return notes;
	}

	public void addNotes(String note) {
		this.notes.add(note);
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public Map<Integer, Lineup> getLineups() {
		return lineups;
	}
	
	
}
