package com.vbh.virtualboathouse;


import java.text.DecimalFormat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Splashscreenactivity extends Activity {
	
	private TextView lastUpdated;
	private Context context;
	
	public final static int SPLASH_SCREEN_ACTIVITY = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreenactivity);
		context = this;
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			}
		Button recordTimes = (Button) findViewById(R.id.record_times_button);
		Button changeLineups = (Button) findViewById(R.id.change_lineups_button);
		Button updateData = (Button) findViewById(R.id.update_data_button);
		Button stroke = (Button) findViewById(R.id.stroke_button);
		
		lastUpdated = (TextView) findViewById(R.id.date_textstub);
		//icons are 35dp, 5dp padding
		recordTimes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.record_times_button)){
					launchBoatPickers();
					//testTimers();
				}
			}
		});
		changeLineups.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.change_lineups_button)){
					//launchChangeLineups();
					testChangeLineups();
				}
			}
		});
		updateData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.update_data_button)){
					//launchDataUpdater();
					boolean success = updateData();
					if (success) {
						lastUpdated.setText(currentDateString());
						System.out.println("date: "+currentDateString());
					}
				}
			}
		});
		
		stroke.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v==findViewById(R.id.stroke_button)) {
					Intent displayTimersIntent = new Intent(context, DisplayTimersActivity.class);
					displayTimersIntent.putExtra(getString(R.string.ACTIVITY_FROM), SPLASH_SCREEN_ACTIVITY);
					displayTimersIntent.putExtra(getString(R.string.CURRENT_NUM_BOATS), 3); 
					startActivity(displayTimersIntent);
				}
			}
		});
		
		
		SharedPreferences sp = getSharedPreferences(CurrentUser.USER_DATA_PREFS, Context.MODE_PRIVATE);
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText(sp.getString(CurrentUser.USERNAME, "admin"));
		TextView tv1 = (TextView) findViewById(R.id.textView2);
		tv1.setText(sp.getString(CurrentUser.API_KEY, "failed"));
	}

	private boolean updateData() {
		boolean success = true;
		DataRetriever dr = new DataRetriever(this);
		//dr.getAthletes();
		//dr.getBoats();
		dr.getAthletesAndBoats();

		// get the id of most recent practice then get practice
		// save id to sharedPrefs
//		SharedPreferences sharedPref = this.getSharedPreferences(
//		        getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
//		SharedPreferences.Editor editor = sharedPref.edit();
//		editor.putInt(getString(R.string.CURRENT_PRACTICE_ID), rm.getPracticeID());
//		editor.apply();
		
		//PracticeLineupsModel[] plm = DataSaver.readObjectArray(dr.LINEUP_DATA_FILENAME + rm.getPracticeID(), this);

		return success;
	}
	
	private void launchBoatPickers() {
		Intent displayBoatPickersIntent = new Intent(this, CrewSelectorActivity.class);
		displayBoatPickersIntent.putExtra("FROM","recordTimes");
		startActivity(displayBoatPickersIntent);
	}
	private void testTimers(){
		System.out.println("testing timers...");
		Intent testTimersIntent = new Intent(this, DisplayTimersActivity.class);
		startActivity(testTimersIntent);
	}
	
	private void testChangeLineups(){
		System.out.println("testing changeLineups...");
		Intent testChangeLineups = new Intent(this, ListViewDraggingAnimation.class);
		startActivity(testChangeLineups);
	}
	
	@SuppressWarnings("unused")
	private void testPickNewPiece(){
		Intent displayPickNewPiece = new Intent(this, PickNewPieceActivity.class);
		displayPickNewPiece.putExtra("FROM","timers");
		startActivity(displayPickNewPiece);
	}
	
	private void launchChangeLineups(){
		Intent changeLineupsIntent = new Intent(this, CrewSelectorActivity.class);
		changeLineupsIntent.putExtra("FROM","changeLineups");
		startActivity(changeLineupsIntent);
		
	}
	
	
	private String currentDateString(){
		Time now = new Time();
		now.setToNow();
		return now.format("%l:%M%P %a, %d %b");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splashscreenactivity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            return true;
	        case R.id.action_logout:
	            if (CurrentUser.deleteUserDataFile(this)) {
	            	finish();
	            	return true;
	            }
	            	
	            else return false;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
			View rootView = inflater.inflate(
					R.layout.fragment_splashscreenactivity, container, false);
			return rootView;
		}
	}

}
