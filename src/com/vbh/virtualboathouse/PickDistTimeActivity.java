package com.vbh.virtualboathouse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PickDistTimeActivity extends Activity {
	
	private EditText distanceEdit;
	private EditText timeMinEdit;
	private EditText timeSecEdit;
	private Button goPickPiece;
	private int distance;
	private int timeMin;
	private int timeSec;
	
	private long currentPieceID;
	private Piece currentPiece;
	private Practice currentPractice;
	private int currentPracticeID;
	private String activityFrom;
	
	public final static String PICK_DIST_ACTIVITY = "PickDistTimeActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_dist_time);
		
		activityFrom = getIntent().getStringExtra(getString(R.string.ACTIVITY_FROM));

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();

			SharedPreferences sharedPref = this.getSharedPreferences(
			        getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
			currentPracticeID = sharedPref.getInt(getString(R.string.CURRENT_PRACTICE_ID), 8);
			currentPractice = DataSaver.readObject(getString(R.string.PRACTICE_FILE) + currentPracticeID, this);
			currentPieceID = sharedPref.getLong(getString(R.string.CURRENT_PIECE_ID), 8);
			currentPiece = currentPractice.getPiece(currentPieceID);
		}
		else {
			currentPracticeID = savedInstanceState.getInt(DataSaver.STATE_PRACTICE_ID);
	        currentPractice   = (Practice) savedInstanceState.getSerializable(DataSaver.STATE_PRACTICE);
	        currentPieceID    = savedInstanceState.getLong(DataSaver.STATE_PIECE_ID);
	        currentPiece      = (Piece) savedInstanceState.getSerializable(DataSaver.STATE_PIECE);
	        // TODO reinstantiate values in the fields for time or distance
		}
		
		goPickPiece = (Button) findViewById(R.id.goPickPiece);
		Button cancelButton = (Button) findViewById(R.id.cancel_button);
		distanceEdit = (EditText) findViewById(R.id.enter_distance);
		timeMinEdit = (EditText) findViewById(R.id.enter_min_time);
		timeSecEdit = (EditText) findViewById(R.id.enter_sec_time);
		distanceEdit.setGravity(Gravity.CENTER);
		timeMinEdit.setGravity(Gravity.CENTER);
		timeSecEdit.setGravity(Gravity.CENTER);
		
		goPickPiece.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v == findViewById(R.id.goPickPiece)) 
					if(!distanceEdit.getText().toString().isEmpty() 
							&& timeMinEdit.getText().toString().isEmpty() 
							&& timeSecEdit.getText().toString().isEmpty()) 
						displayDistance();
					else if((!timeMinEdit.getText().toString().isEmpty() 
							|| !timeSecEdit.getText().toString().isEmpty()) 
							&& distanceEdit.getText().toString().isEmpty())
						displayTime();
					else {
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			        	builder.setMessage(R.string.pick_disttime_error_message);
			        	AlertDialog disttimeDialog = builder.create();
			        	disttimeDialog.show();
					}
				}
			});
		cancelButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(v==findViewById(R.id.cancel_button)){
					if(activityFrom.equals("CrewSelectorActivity")){
						//go to crew selector
						selectCrews();
					}
					else if(activityFrom.equals("PickNewPieceActivity")){
						//go to picknewpiece
						pickNewPiece();
					}
				}
			}
		});
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the current practice state
	    savedInstanceState.putLong(DataSaver.STATE_PIECE_ID, currentPieceID);
	    savedInstanceState.putInt(DataSaver.STATE_PRACTICE_ID, currentPracticeID);
	    savedInstanceState.putSerializable(DataSaver.STATE_PRACTICE, currentPractice);
	    savedInstanceState.putSerializable(DataSaver.STATE_PIECE, currentPiece);
	    
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	private void displayDistance(){
		distance = Integer.parseInt(distanceEdit.getText().toString());
		if (distance < 1 || distance > 10000000) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        	builder.setMessage(R.string.invalid_distance_error_message);
        	AlertDialog invaliddistanceDialog = builder.create();
        	invaliddistanceDialog.show();
		}
		else {
			//TODO: currently generates null pointer
			currentPiece.setDistance(distance);
			currentPiece.setTimed(true);
			saveData();
			displayTimers();
		}
	}
	
	private void displayTime(){
		if (timeMinEdit.getText().toString().isEmpty())
			timeMin = 0;
		else timeMin = Integer.parseInt(timeMinEdit.getText().toString());
		if (timeSecEdit.getText().toString().isEmpty())
			timeSec = 0;
		else timeSec = Integer.parseInt(timeSecEdit.getText().toString());
		if (timeMin >= 60 || timeSec >= 60) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        	builder.setMessage(R.string.invalid_time_error_message);
        	AlertDialog invalidtimeDialog = builder.create();
        	invalidtimeDialog.show();
		}
		else {
			int[] array = {timeMin, timeSec};
			currentPiece.setCountdown(true);
			currentPiece.setCountdownTime(array);
			saveData();
			Intent countdownActivity = new Intent(this, CountdownActivity.class);
			countdownActivity.putExtra("numbers", array);
			startActivity(countdownActivity);
		}
	}
	private void saveData() {
		currentPractice.addPiece(currentPiece);
		DataSaver.writeObject(currentPractice, getString(R.string.PRACTICE_FILE) + currentPracticeID, this);
	}
	private void displayTimers() {
		Intent displayTimersIntent = new Intent(this, DisplayTimersActivity.class);
		displayTimersIntent.putExtra(getString(R.string.ACTIVITY_FROM), PICK_DIST_ACTIVITY);
		displayTimersIntent.putExtra(getString(R.string.CURRENT_NUM_BOATS), currentPiece.getNumBoats()); 
		startActivity(displayTimersIntent);
	}
	
	private void selectCrews(){
		Intent crewSelectorIntent = new Intent(this, CrewSelectorActivity.class);
		crewSelectorIntent.putExtra(getString(R.string.ACTIVITY_FROM),"recordTimes");
		startActivity(crewSelectorIntent);
		
	}
	
	private void pickNewPiece(){
		Intent pickNewPieceIntent = new Intent(this, PickNewPieceActivity.class);
		startActivity(pickNewPieceIntent);
		
	}
	
	private Context getContext(){
		return this;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pick_dist_time, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_pick_dist_time,
					container, false);
			return rootView;
		}
	}

}
