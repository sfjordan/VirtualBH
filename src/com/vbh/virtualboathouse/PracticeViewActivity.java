package com.vbh.virtualboathouse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Build;

public class PracticeViewActivity extends Activity {
	
	private SharedPreferences sharedPref;
	private int currentPracticeID;
	private Practice currentPractice;
	private Roster roster;
	private Map<Integer, Boat> boatList;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice_view);

		if (savedInstanceState == null) {
			
		}
		context = this;
		//get data
		getData();
		// put the title in
		TextView practice_name = (TextView) findViewById(R.id.practiceName);
		TextView practice_date_time = (TextView) findViewById(R.id.practiceDateView);
		practice_name.setText("Today's Workout");
		Date d = currentPractice.getDate();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
		practice_date_time.setText(sdf.format(d));
		
		//for each piece, display pieceview
		LinearLayout piece_list = (LinearLayout)findViewById(R.id.piece_list);
		int j = 0;
		Iterator<Entry<Long, Piece>> allPieces = currentPractice.getAllPieces().entrySet().iterator();
    	while(allPieces.hasNext()){
    		Piece p = allPieces.next().getValue();
    		if (p.getDistance()==0) continue;
    		PieceView pv = new PieceView(context, piece_list, currentPractice, p);
    		View view = new View(this);
    	    view.setLayoutParams(new LayoutParams(2,LayoutParams.MATCH_PARENT));
    		view.setBackgroundColor(Color.BLACK);
    		piece_list.addView(view);
    		j++;
    	}
		//done button tries to update data, returns to splashscreen
    	Button done_button = (Button) findViewById(R.id.button_done);
    	done_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.button_done)){
					goSplashscreen();
				}
			}
		});
	}
	
	private void goSplashscreen(){
		Intent splashscreenIntent = new Intent(this, SplashscreenActivity.class);
		startActivity(splashscreenIntent);
	}
	private void getData(){
    	// get the practice ID
		sharedPref = context.getSharedPreferences(
		        getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
		currentPracticeID = sharedPref.getInt(getString(R.string.CURRENT_PRACTICE_ID), 8);
		// get the current practice from a file
		currentPractice = DataSaver.readObject(getString(R.string.PRACTICE_FILE) + currentPracticeID, context);
		// get the roster and boatlist
		boatList = DataSaver.readObject(context.getString(R.string.BOATS_FILE), context);
		// TODO check for null
		Log.i("practiceView", "boatList is currently null: " + (boatList==null));
		roster = DataSaver.readObject(context.getString(R.string.ROSTER_FILE), context);
		// TODO check for null
		Log.i("practiceView", "roster is currently null: " + (roster==null));
    }
	
	private boolean updateData() {
		boolean success = true;
		DataRetriever dr = new DataRetriever(this);
		dr.getAthletesAndBoats();
		//TODO but it needs to upload too???
		return success;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practice_view, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_practice_view,
					container, false);
			return rootView;
		}
	}

}
