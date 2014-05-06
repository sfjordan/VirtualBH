package com.vbh.virtualboathouse;


import java.text.DecimalFormat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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

public class SplashscreenActivity extends Activity {
	
	private TextView lastUpdated;
	private Context context;
	private SharedPreferences sharedPref;
	
	public final static String SPLASH_SCREEN_ACTIVITY = "SplashscreenActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreenactivity);
		sharedPref = getSharedPreferences(CurrentUser.USER_DATA_PREFS, Context.MODE_PRIVATE);
		context = this;
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			}
		Button recordTimes = (Button) findViewById(R.id.record_times_button);
		Button changeLineups = (Button) findViewById(R.id.change_lineups_button);
		Button updateData = (Button) findViewById(R.id.update_data_button);
		//Button stroke = (Button) findViewById(R.id.stroke_button);
		
		Log.i("splashscreen","in splashscreen");
		
		lastUpdated = (TextView) findViewById(R.id.date_textstub);
		//if sharedpref is false, leave blank? else make red "unsynced data" or something
		Boolean dataChanged = sharedPref.getBoolean("DATA_SET_CHANGED", true);
		Log.i("splashscreen","datachanged: "+dataChanged);
		if (dataChanged){
			//make red and notify user
			lastUpdated.setTextColor(Color.RED);
			lastUpdated.setText(getString(R.string.need_to_sync));
		}
		else {
			lastUpdated.setTextColor(getResources().getColor(R.color.text_gray));
			lastUpdated.setText(getString(R.string.last_updated_text) + getString(R.string.just_now));
		}
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
					launchChangeLineups();
					//testChangeLineups();
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
						sharedPref.edit().putBoolean("DATA_SET_CHANGED", false).apply();
						//lastUpdatedStub.setText(getString(R.string.last_updated_text));
						lastUpdated.setTextColor(getResources().getColor(R.color.text_gray));
						lastUpdated.setText(getString(R.string.last_updated_text) + currentDateString());
					}
				}
			}
		});
		
		/*stroke.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v==findViewById(R.id.stroke_button)) {
					Intent displayTimersIntent = new Intent(context, DisplayTimersActivity.class);
					displayTimersIntent.putExtra(getString(R.string.ACTIVITY_FROM), SPLASH_SCREEN_ACTIVITY);
					displayTimersIntent.putExtra(getString(R.string.CURRENT_NUM_BOATS), 3); 
					startActivity(displayTimersIntent);
				}
			}
		});*/
		
		
		SharedPreferences sp = getSharedPreferences(CurrentUser.USER_DATA_PREFS, Context.MODE_PRIVATE);
		TextView tv = (TextView) findViewById(R.id.username_text);
		tv.setText(sp.getString(CurrentUser.USERNAME, "admin"));
		//enable for debugging only, displays API key
//		TextView tv1 = (TextView) findViewById(R.id.textView2);
//		tv1.setText(sp.getString(CurrentUser.API_KEY, "failed"));
	}

	private boolean updateData() {
		boolean success = true;
		DataRetriever dr = new DataRetriever(this);
		dr.getAthletesAndBoats();

		return success;
	}
	
	private void launchBoatPickers() {
		Intent displayBoatPickersIntent = new Intent(this, CrewSelectorActivity.class);
		displayBoatPickersIntent.putExtra(getString(R.string.ACTIVITY_FROM),"recordTimes");
		startActivity(displayBoatPickersIntent);
	}
	private void testTimers(){
		System.out.println("testing timers...");
		Intent testTimersIntent = new Intent(this, DisplayTimersActivity.class);
		startActivity(testTimersIntent);
	}
	
	private void testChangeLineups(){
		System.out.println("testing changeLineups...");
		Intent testChangeLineups = new Intent(this, ChangeLineupsList.class);
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
		changeLineupsIntent.putExtra(getString(R.string.ACTIVITY_FROM),"changeLineups");
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
