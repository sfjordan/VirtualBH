package com.vbh.virtualboathouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import android.util.SparseArray;

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
	private final SparseArray<Lineup> lineups;
	private long[] times;
	private ArrayList<String> notes;
	private String margin;
	
	public Piece(PracticeLineupsModel[] plm, Roster roster, SparseArray<Boat> boats) {
		pieceID = UUID.randomUUID().getLeastSignificantBits();
		lineups = new SparseArray<Lineup>(plm.length);
		for (PracticeLineupsModel lineup : plm) {
			Lineup l = new Lineup(lineup, roster, boats);
			lineups.append(l.getLineupID(), l);
		}
		times = new long[plm.length];
	}

	
	public long getPieceID() {
		return pieceID;
	}
	public boolean isCountdown() {
		return countdown;
	}

	public void setCountdown(boolean countdown) {
		this.countdown = countdown;
	}

	public boolean isTimed() {
		return timed;
	}

	public void setTimed(boolean timed) {
		this.timed = timed;
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

	public long[] getTimes() {
		return times;
	}

	public void setTimes(long[] times) {
		this.times = times;
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

	public SparseArray<Lineup> getLineups() {
		return lineups;
	}
	
	
}