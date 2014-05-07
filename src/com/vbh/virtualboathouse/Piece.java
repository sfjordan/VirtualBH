package com.vbh.virtualboathouse;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.SparseArray;

@SuppressLint("UseSparseArrays")
public class Piece implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6044365957538890403L;
	
	private final long pieceID;
	private boolean countdown; //timed piece
	private boolean timed; //distance piece
	private int distance;
	private String direction;
	private final ArrayList<Long> lineups;
	private Map<Long, Long> times;
	private ArrayList<String> notes;
	private ArrayList<String> strokeRatingNotes;
	private String margin;
	private long msCountdownTime;
	private final long msTime;
	private final Date date;
	private boolean hasDirection = false;
	private String name;
	
	public Piece (Lineup[] lineups, Roster roster, Map<Integer, Boat> boats) {
		pieceID = UUID.randomUUID().getLeastSignificantBits();
		this.lineups = new ArrayList<Long>(lineups.length);
		for (Lineup l : lineups) {
			this.lineups.add(l.getLineupID());
		}
		times = new HashMap<Long, Long>(lineups.length);
		msTime = System.currentTimeMillis();
		date = new Date(msTime);
		notes = new ArrayList<String>();
		strokeRatingNotes = new ArrayList<String>();
		generateName();
		
	}
	public Piece (ArrayList<Long> lineups, Roster roster, Map<Integer, Boat> boats) {
		pieceID = UUID.randomUUID().getLeastSignificantBits();
		this.lineups = new ArrayList<Long>(lineups.size());
		for (Long id : lineups) {
			this.lineups.add(id);
		}
		times = new HashMap<Long, Long>(lineups.size());
		msTime = System.currentTimeMillis();
		date = new Date(msTime);
		notes = new ArrayList<String>();
		strokeRatingNotes = new ArrayList<String>();
		generateName();
	}
	
	
	public Piece (Piece oldPiece) {
		pieceID = UUID.randomUUID().getLeastSignificantBits();
		this.lineups = oldPiece.getLineups();
		times = new HashMap<Long, Long>(lineups.size());
		msTime = System.currentTimeMillis();
		date = new Date(msTime);
		notes = oldPiece.getNotes();
		strokeRatingNotes = oldPiece.getStrokeRatingNotes();
		hasDirection = oldPiece.hasDirection();
		direction = oldPiece.getDirection();
		name = oldPiece.getName();
		
	}
	
	public static String msTimeToString(long time, boolean countdown) {
		if (time < 0) {
			return null;
		}
		else {
			int hours = (int)(time / (3600 * 1000));
		    int remaining = (int)(time % (3600 * 1000));
			int minutes = (int)(remaining / (60 * 1000));
		    remaining = (int)(remaining % (60 * 1000));
		    int seconds = (int)(remaining / 1000);
		    remaining = (int)(remaining % (1000));
	        int hundredths = (int)(((int)time % 1000) / 10);

		    DecimalFormat df = new DecimalFormat("00");
		    String countdownStr = "";
		    
		    if (hours != 0) countdownStr += hours + ":";
		    countdownStr += df.format(minutes) + ":" + df.format(seconds); 
		    if (!countdown) countdownStr += "." + df.format(hundredths);
		    
		    return countdownStr;
		}
	}
	
	private void generateName() {
		String name = "";
		if (countdown) {
			name += msTimeToString(this.msCountdownTime, countdown) + " Piece";
		}
		else {
			name += distance + "m Piece";
		}
		this.name = name;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public long getMsTime() {
		return msTime;
	}
	public Date getDate() {
		return date;
	}
	public long getDateSeconds() {
		return msTime/1000;
	}
	public String getName() {
		return name;
	}
	
	public boolean hasDirection() {
		return hasDirection;
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

	public Map<Long, Long> getTimes() {
		return times;
	}
	public long getTime(long lineupID) {
		Log.i("piece","times is null: "+(times==null));
		Log.i("piece","lineupID: "+(lineupID));
		Log.i("piece","times.get(lineupID) is null: "+(times.get(lineupID)==null));
		Iterator<Long> allTimes = times.keySet().iterator();
    	while(allTimes.hasNext()){
    		Long l = allTimes.next();
    		Log.i("piece","times map contains long: "+l);
    	}
    	if (times.get(lineupID) == null) return 0L;
    	else return times.get(lineupID);
	}

	public void setTime(long lineupID, long time) {
		times.put(lineupID, time);
	}

	public ArrayList<String> getNotes() {
		return notes;
	}
	public ArrayList<String> getStrokeRatingNotes() {
		return strokeRatingNotes;
	}

	public void addNotes(String note) {
		this.notes.add(note);
	}
	public void addStrokeRatingNotes(String note) {
		this.strokeRatingNotes.add(note);
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public ArrayList<Long> getLineups() {
		return lineups;
	}
	
	
}
