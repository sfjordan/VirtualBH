package com.vbh.virtualboathouse;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreenactivity);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			}
		Button recordTimes = (Button) findViewById(R.id.record_times_button);
		Button changeLineups = (Button) findViewById(R.id.change_lineups_button);
		Button updateData = (Button) findViewById(R.id.update_data_button);
		lastUpdated = (TextView) findViewById(R.id.date_textstub);
		//icons are 35dp, 5dp padding
		recordTimes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.record_times_button)){
					launchBoatPickers();
				}
			}
		});
		changeLineups.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.change_lineups_button)){
					launchChangeLineups();
				}
			}
		});
		updateData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.update_data_button)){
					boolean success = updateData();
					if (success) {
						lastUpdated.setText(currentDateString());
						System.out.println("date: "+currentDateString());
					}
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
		DataRetriever dr = new DataRetriever(this);
		dr.getAthletes();
		dr.getBoats();
		dr.getMostRecentPractice();
		AthleteModel am[] = DataSaver.readObjectArray(dr.ATHLETE_DATA_FILENAME, this);
		BoatModel bm[] = DataSaver.readObjectArray(dr.BOAT_DATA_FILENAME, this);
		// build boat list
		SparseArray<Boat> boatList = new SparseArray<Boat>(bm.length);
		for (BoatModel boatModel : bm) {
			Boat boat = new Boat(boatModel);
			boatList.append(boat.getBoatID(), boat);
		}
		// build roster
		Roster roster = new Roster(am);
		// currently, just get the most recent practice
		//PracticeLineupsModel plm[] = DataSaver.readObjectArray(dr.RECENT_PRACTICE_DATA_FILENAME, this);
		// 
		
		// implement getting all and choosing later
		// PracticeModel pm[] = DataSaver.readObject(dr.PRACTICE_DATA_FILENAME, this);
		// figure out most recent practice
		//int practice = 2;
		//dr.getPractice(practice);
		return true;
	}
	
	private void launchBoatPickers() {
		Intent displayBoatPickersIntent = new Intent(this, PickDistTimeActivity.class);
		startActivity(displayBoatPickersIntent);
	}
	
	private void launchChangeLineups(){
		Intent changeLineupsIntent = new Intent(this, ChangeLineupsActivity.class);
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
