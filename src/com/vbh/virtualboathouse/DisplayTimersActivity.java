package com.vbh.virtualboathouse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayTimersActivity extends Activity {

	public static Button start_all;
	public static Button stop_all;
	public static Button save_times;
	static final String TIMER_BASES = "timerBases";
	static final String TIMER_STATES = "timerStates";
	private int numTimers;
	private Timer[] timers;
	
	private long currentPieceID;
	private Piece currentPiece;
	private Practice currentPractice;
	private int currentPracticeID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_timers);
		Intent intent = getIntent();
		numTimers = intent.getIntExtra(getString(R.string.CURRENT_NUM_BOATS), 3);
		timers = new Timer[numTimers];
		// get data 
    	SharedPreferences sharedPref = this.getSharedPreferences(
		        getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
		currentPracticeID = sharedPref.getInt(getString(R.string.CURRENT_PRACTICE_ID), 8);
		currentPractice = DataSaver.readObject(getString(R.string.PRACTICE_FILE) + currentPracticeID, this);
		currentPieceID = sharedPref.getLong(getString(R.string.CURRENT_PIECE_ID), 8);
		currentPiece = currentPractice.getPiece(currentPieceID);
		SparseArray<Lineup> lineups = currentPiece.getLineups();
		LinearLayout timer_list = (LinearLayout)findViewById(R.id.timers_list);
		for (int i = 0; i < numTimers; i++) {
			timers[i] = new Timer(this, timer_list, lineups.valueAt(i).getName());
		}
		TimerControlsHandler tch = new TimerControlsHandler(timers, this);
		start_all = (Button)findViewById(R.id.start_all_button);
		stop_all = (Button)findViewById(R.id.stop_all_button);
		save_times = (Button)findViewById(R.id.save_times_button);
		start_all.setOnClickListener(tch);
		stop_all.setOnClickListener(tch);
		save_times.setOnClickListener(tch);
		if (savedInstanceState != null) {
			long[] timerBases = savedInstanceState.getLongArray(TIMER_BASES);
			boolean[] timerStates = savedInstanceState.getBooleanArray(TIMER_STATES);
			for (int i = 0; i < numTimers; i++) {
				timers[i].restart(timerBases[i], timerStates[i]);
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	    long[] timerBases = new long[numTimers];
		boolean[] timerStates = new boolean[numTimers];
	    for (int i = 0; i < numTimers; i++) {
			timerBases[i] = timers[i].getRestartBase();
			timerStates[i] = timers[i].getRestartState();
		}
	    savedInstanceState.putLongArray(TIMER_BASES, timerBases);
	    savedInstanceState.putBooleanArray(TIMER_STATES, timerStates);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_timers, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_display_timers,
					container, false);
			return rootView;
		}
	}

}
