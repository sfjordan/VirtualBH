package com.vbh.virtualboathouse;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PieceView {

	private Context context;
	private Piece piece;
	private LinearLayout mainLayout;
	private Practice practice;
	
	public PieceView(Context c, LinearLayout ll, Practice prac, Piece p) {
		this.context = c;
		this.piece = p;
		this.practice = prac;
		mainLayout = new LinearLayout(c);
		mainLayout.setPadding(10, 10, 10, 10);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		addTitle();
		// for each boat
		for (long l : piece.getLineups()) {
			addBoat(l);
		}
		addStrokeRatings();
		addNotes();
		ll.addView(mainLayout);
	}
	
	private void addTitle() {
		TextView pieceName = new TextView(context);
		LinearLayout llH = new LinearLayout(context);
		TextView typeOfPiece = new TextView(context);
		TextView distanceOrTime = new TextView(context);
		pieceName.setText(piece.getName());
		mainLayout.addView(pieceName);
		if (piece.isCountdown()) 
			typeOfPiece.setText("Timed ");
		else typeOfPiece.setText("Distance ");
		llH.addView(typeOfPiece);
		if (piece.isCountdown()) 
			distanceOrTime.setText(Piece.msTimeToString(piece.getCountdownTime(), piece.isCountdown()));
		else distanceOrTime.setText(piece.getDistance() +  " m");
		llH.addView(distanceOrTime);
		mainLayout.addView(llH);
		TextView direction;
		if (piece.hasDirection()) {
			direction = new TextView(context);
			direction.setText(piece.getDirection());
			mainLayout.addView(direction);
		}
	}
	
	private void addBoat(long lineupID) {
		// add name
		// TODO potential spot for settings
		TextView boatName = new TextView(context);
		TextView underText = new TextView(context);
		boatName.setText(practice.getLineup(lineupID).getBoatName()); // name of boat
		boatName.setTextSize(R.dimen.boat_name_text_size);
		underText.setText(practice.getLineup(lineupID).getStrokeInitLast()); //stroke
		LinearLayout llH = new LinearLayout(context);
		LinearLayout llV = new LinearLayout(context);
		llV.addView(boatName);
		llV.addView(underText);
		llH.addView(llV);
		// add result if there is one
		if(piece.isCountdown()) {
			
		}
		else if (piece.getTime(lineupID) != 0L) {
			TextView result = new TextView(context);
			result.setTextSize(R.dimen.result_text_size);
			result.setText(Piece.msTimeToString(piece.getTime(lineupID), false));
			llH.addView(result);
		}
		mainLayout.addView(llH);
	}
	
	private void addStrokeRatings() {
		if (piece.getStrokeRatingNotes().size() == 0) { //if stroke ratings
			TextView strokeRatingsTitle = new TextView(context);
			strokeRatingsTitle.setText("Stroke Ratings:");
			mainLayout.addView(strokeRatingsTitle);
			TextView strokeRatingsText = new TextView(context);
			String strokeRatingNotes = "";
			for (String note : piece.getStrokeRatingNotes()) {
				strokeRatingNotes += note + "\n";
			}
			if (strokeRatingNotes.length()!=0)
				strokeRatingNotes = strokeRatingNotes.substring(0, strokeRatingNotes.length()-1);
			strokeRatingsText.setText(strokeRatingNotes);
			mainLayout.addView(strokeRatingsText);
		}
	}
	
	private void addNotes() {
		if (piece.getNotes().size() == 0) { //if stroke ratings
			TextView notesTitle = new TextView(context);
			notesTitle.setText("Coach's Notes:");
			mainLayout.addView(notesTitle);
			TextView notesText = new TextView(context);
			String notes = "";
			for (String note : piece.getStrokeRatingNotes()) {
				notes += note + "\n";
			}
			if (notes.length() !=0)
				notes = notes.substring(0, notes.length()-1);
			notesText.setText(notes);
			mainLayout.addView(notesText);
		}
	}
	
	
}
