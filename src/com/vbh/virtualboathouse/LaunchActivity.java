package com.vbh.virtualboathouse;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class LaunchActivity extends Activity {
	private static int LAUNCH_TIME_OUT = 3000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
	    new Handler().postDelayed(new Runnable() {	  
	            /*
	             * Showing splash screen with a timer. 
	             */
	            @Override
	            public void run() {
	        		CurrentUser cu = CurrentUser.readObject(CurrentUser.USER_DATA_FILE, getApplicationContext());
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
	        			Intent splashscreenIntent = new Intent(getApplicationContext(), Splashscreenactivity.class);
	        			startActivity(splashscreenIntent);
	        			
	        		}
	                finish();  // close this activity
	            }
	        }, LAUNCH_TIME_OUT);
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
