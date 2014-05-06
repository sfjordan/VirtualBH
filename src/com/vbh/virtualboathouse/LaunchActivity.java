package com.vbh.virtualboathouse;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class LaunchActivity extends Activity {
	private static int LAUNCH_TIME_OUT = 2000;
	private SharedPreferences sharedPref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/MyriadPro-Semibold.ttf"); 
		TextView appTitle = (TextView)findViewById(R.id.app_title_header);
		appTitle.setTypeface(type);
		//appTitle.setGravity(Gravity.CENTER);
		appTitle.setPadding(25, 25, 40, 25);
		
	    new Handler().postDelayed(new Runnable() {	  
	            /*
	             * Showing splash screen with a timer. 
	             */
	            @Override
	            public void run() {
	        		CurrentUser cu = DataSaver.readObject(CurrentUser.USER_DATA_FILE, getApplicationContext());
	        		if (cu == null) { // if the file doesn't exist, or there was some problem with file IO 
	        			// launch the login activity
	        			Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
	        			startActivity(loginIntent);
	        		}
	        		else {
	        			// save the api key and the username for future accesses
	        			SharedPreferences sp = getSharedPreferences(CurrentUser.USER_DATA_PREFS, Context.MODE_PRIVATE);
	        			SharedPreferences.Editor spEditor = sp.edit();
	        			spEditor.putString(CurrentUser.API_KEY, cu.getAPIKey());
	        			spEditor.putString(CurrentUser.USERNAME, cu.getUsername());
	        			spEditor.commit();
	        			// launch the main menu activity
	        			Intent splashscreenIntent = new Intent(getApplicationContext(), SplashscreenActivity.class);
	        			//TODO check if user is already logged in/already have API key
	        			if(updateData()) {
	        				spEditor.putBoolean("DATA_SET_CHANGED", false).apply();
	        			}
	        			else {
	        				spEditor.putBoolean("DATA_SET_CHANGED", true).apply();
	        			}
	        			spEditor.putBoolean("DATA_SET_CHANGED", true).apply();
	        			startActivity(splashscreenIntent);
	        			
	        			
	        		}
	                finish();  // close this activity
	            }
	        }, LAUNCH_TIME_OUT);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launch, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_launch, container, false);
			return rootView;
		}
	}

}
