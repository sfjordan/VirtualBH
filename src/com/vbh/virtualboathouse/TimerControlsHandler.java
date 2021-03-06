package com.vbh.virtualboathouse;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class TimerControlsHandler extends Activity implements OnClickListener {
	
	private Timer[] timers;
	private Context context;
	private Button stop_all;
	private Boolean genericMode;
	
	private long currentPieceID;
	private Piece currentPiece;
	private Practice currentPractice;
	private int currentPracticeID;
	
	public TimerControlsHandler(Timer[] timers, Button stop_all, Context context, Boolean genericMode) {
		this.timers = timers;
		this.context = context;
		this.stop_all = stop_all;
		this.genericMode = genericMode;
	}
	
	@Override
	//note: something seems to have changed here. t.start/stop gives null pointers now :(
    public void onClick(View v) {
        if(DisplayTimersActivity.start_all == v) {
        	//set text to 'Stop All'
        	stop_all.setText("Stop All");
			stop_all.setTextColor(Color.parseColor("white"));
        	for(Timer t : timers) {
        		t.start();
        	}
        }
        else if(DisplayTimersActivity.stop_all == v){
        	for(Timer t : timers) {
        		if (!t.isStopped())
        			t.stop();
        	}
        }
        //Note: this chunk adds 'clear all' functionality, still buggy
        /*else if(DisplayTimersActivity.stop_all == v){
        	if (allStopped()){
        		//set text to 'Stop All'
        		stop_all.setText("Stop All");
    			stop_all.setTextColor(Color.parseColor("white"));
	        	for(Timer t : timers) {
	    			t.clear();
	    		}
        	}
        	else {
        		//set text to 'Clear All'
        		stop_all.setText("Clear All");
    			stop_all.setTextColor(Color.parseColor("white"));
        		for(Timer t : timers) {
	    			t.stop();
	    		}
        	}
        }*/
        else if(DisplayTimersActivity.save_times == v){
        	if (allStopped() && genericMode){
        		AlertDialog.Builder alert = new AlertDialog.Builder(context);
        		alert.setTitle("Generic Timers");
        		alert.setMessage(R.string.error_save_times_generic);
        		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        		    public void onClick(DialogInterface dialog, int whichButton) {
        		     //Do something here where "ok" clicked
        		    	Intent splashscreenIntent = new Intent(context, SplashscreenActivity.class);
        				context.startActivity(splashscreenIntent);
        		    }
        		});
        		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        		    public void onClick(DialogInterface dialog, int whichButton) {
        		    //Do something here when "cancel" clicked.
        		    }
        		});
        		alert.show();
        		/*AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        	builder.setMessage(R.string.error_save_times_generic);
	        	AlertDialog genericModeDialog = builder.create();
	        	genericModeDialog.show();*/
        	}
        	else if(allStopped()){
        		// get data 
	        	SharedPreferences sharedPref = context.getSharedPreferences(
				        context.getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
				currentPracticeID = sharedPref.getInt(context.getString(R.string.CURRENT_PRACTICE_ID), 8);
				currentPractice = DataSaver.readObject(context.getString(R.string.PRACTICE_FILE) + currentPracticeID, context);
				currentPieceID = sharedPref.getLong(context.getString(R.string.CURRENT_PIECE_ID), 8);
				currentPiece = currentPractice.getPiece(currentPieceID);
	        	// save times to current piece
				ArrayList<Long> lineups = currentPiece.getLineups();
	        	int j = 0;
	    		for (Long lineupID : lineups) {
	    			currentPiece.setTime(lineupID, timers[j].getElapsedTime());
	    			StringBuilder strokeRateNote = new StringBuilder();
		    		boolean first = true;
		    		for (String note : timers[j].getStrokeNotes()) {
		    			if (!first)
		        			strokeRateNote.append(", ");
		    			else 
		    				strokeRateNote.append(timers[j].getBoatNameString() + ": ");
	    				first = false;
	    				strokeRateNote.append(note);
	    			}
	    			Log.i("TimerControlsHandler", strokeRateNote.toString());
	    			currentPiece.addStrokeRatingNotes(strokeRateNote.toString());
	    			
	    			j++;
	    		}
	        	// save piece to practice
	        	currentPractice.addPiece(currentPiece);
	        	// write practice to file
	        	DataSaver.writeObject(currentPractice, context.getString(R.string.PRACTICE_FILE) + currentPracticeID, context);
	        	//TODO add redirect to new piece screen
	        	pickNewPiece();
	        	
	        	// display saved message
	//        	AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
	//        	builder.setMessage(R.string.dialog_message);
	//        	AlertDialog saveDialog = builder.create();
	//        	saveDialog.show();
        	}
        	
        	
        }
    }
	
	private void pickNewPiece(){
		Intent pickNewPieceIntent = new Intent(context, PickNewPieceActivity.class);
		pickNewPieceIntent.putExtra("FROM","timers"); 
		context.startActivity(pickNewPieceIntent);
	}
	
	private boolean allStopped(){
		boolean allstopped = true;
		for (Timer t : timers){
			if (!t.isStopped())
				allstopped = false;
		}
		return allstopped;
	}

}

