package com.vbh.virtualboathouse;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class TimerControlsHandler extends Activity implements OnClickListener {
	
	private Timer[] timers;
	private Context context;
	private Button stop_all;
	
	private long currentPieceID;
	private Piece currentPiece;
	private Practice currentPractice;
	private int currentPracticeID;
	
	public TimerControlsHandler(Timer[] timers, Button stop_all, Context context) {
		this.timers = timers;
		this.context = context;
		this.stop_all = stop_all;
	}
	
	@Override
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
        		if (!t.stopped)
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
        	// get data 
        	SharedPreferences sharedPref = context.getSharedPreferences(
			        context.getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
			currentPracticeID = sharedPref.getInt(context.getString(R.string.CURRENT_PRACTICE_ID), 8);
			currentPractice = DataSaver.readObject(context.getString(R.string.PRACTICE_FILE) + currentPracticeID, context);
			currentPieceID = sharedPref.getLong(context.getString(R.string.CURRENT_PIECE_ID), 8);
			currentPiece = currentPractice.getPiece(currentPieceID);
        	// save times to current piece
			Map<Integer, Lineup> lineups = currentPiece.getLineups();
        	int j = 0;
    		for (Integer lineupID : lineups.keySet()) {
    			currentPiece.setTime(lineups.get(lineupID).getLineupID(), timers[j].getElapsedTime());
    			j++;
    		}
        	// save piece to practice
        	currentPractice.addPiece(currentPiece);
        	// write practice to file
        	DataSaver.writeObject(currentPractice, context.getString(R.string.PRACTICE_FILE) + currentPracticeID, context);
        	//TODO add redirect to new piece screen
        	pickNewPiece();
        	
        	// display saved message
        	AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        	builder.setMessage(R.string.dialog_message);
        	AlertDialog saveDialog = builder.create();
        	saveDialog.show();
        	
        	
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
			if (!t.stopped)
				allstopped = false;
		}
		System.out.println("allstopped: "+allstopped);
		return allstopped;
	}

}

