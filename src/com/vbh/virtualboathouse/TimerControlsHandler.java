package com.vbh.virtualboathouse;

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
        	SharedPreferences sharedPref = this.getSharedPreferences(
			        getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
			currentPracticeID = sharedPref.getInt(getString(R.string.CURRENT_PRACTICE_ID), 8);
			currentPractice = DataSaver.readObject(getString(R.string.PRACTICE_FILE) + currentPracticeID, context);
			currentPieceID = sharedPref.getLong(getString(R.string.CURRENT_PIECE_ID), 8);
			currentPiece = currentPractice.getPiece(currentPieceID);
        	// save times to current piece
			SparseArray<Lineup> lineups = currentPiece.getLineups();
			int keyIndex = 0;
        	for(Timer t : timers) {
        		currentPiece.setTime(lineups.valueAt(keyIndex).getLineupID(), t.getElapsedTime());
        	}
        	// save piece to practice
        	currentPractice.addPiece(currentPiece);
        	// write practice to file
        	DataSaver.writeObject(currentPractice, getString(R.string.PRACTICE_FILE) + currentPracticeID, context);
        	//TODO add redirect to new piece screen
        	PickNewPiece();
        	
        	// display saved message
        	AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        	builder.setMessage(R.string.dialog_message);
        	AlertDialog saveDialog = builder.create();
        	saveDialog.show();
        	
        	
        }
    }
	
	private void PickNewPiece(){
		Intent pickNewPieceIntent = new Intent(this, PickNewPieceActivity.class);
		pickNewPieceIntent.putExtra("FROM","timers"); 
		startActivity(pickNewPieceIntent);
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

