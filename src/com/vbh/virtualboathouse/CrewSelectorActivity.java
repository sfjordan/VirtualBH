package com.vbh.virtualboathouse;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.os.Build;

public class CrewSelectorActivity extends Activity {
	
	private Button go_button;

	private Context context;
	private Roster roster;
	private SparseArray<Boat> boatList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crew_selector);
		context = this;
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			
			// currently, just get the most recent practice
			DataRetriever dr = new DataRetriever(this);
			PracticeLineupsModel plm[] = DataSaver.readObjectArray(dr.RECENT_PRACTICE_DATA_FILENAME, this);
			this.roster = DataSaver.readObject(getString(R.string.ROSTER_FILE), this);
			this.boatList = DataSaver.readObject(getString(R.string.BOATS_FILE), this);
			SparseArray<Lineup> lineups = new SparseArray<Lineup>(plm.length);
			String[] lineupNames = new String[plm.length];
			int[] lineupIDs = new int[plm.length];
			int i = 0;
			for (PracticeLineupsModel lineupModel : plm) {
				Lineup l = new Lineup(lineupModel, roster, boatList);
				lineups.append(l.getLineupID(), l);
				lineupNames[i] = l.getAthleteFromSeat(l.getAthleteIDFromSeat(l.getNumOfSeats()), roster).getFirstInitLastName();
				lineupIDs[i] = l.getLineupID();
				i++;
			}
			// display the stroke seats in the selector. 
			
			// get practiceID from model... currently just default to 9
			int practiceID = 9;
			// implement getting all and choosing later
			// save current practice id to shared prefs
			SharedPreferences sharedPref = this.getSharedPreferences(
			        getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putInt(getString(R.string.CURRENT_PRACTICE_ID), practiceID);
			editor.apply();
			 
		}
		
		
		
		
		/*LinearLayout layout = (LinearLayout) findViewById(R.id.spinner_layout);
		
		Spinner boat_picker = new Spinner(this);
		ArrayList<String> spinnerArray = new ArrayList<String>();
	    spinnerArray.add("Boat 1");
	    spinnerArray.add("Boat 2");
	    spinnerArray.add("Boat 3");
	    spinnerArray.add("Boat 4");
	    spinnerArray.add("Boat 5");
	    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
	    boat_picker.setAdapter(spinnerArrayAdapter);
		boat_picker.setAdapter(spinnerArrayAdapter);
		
		layout.addView(boat_picker);

	    setContentView(layout);*/
	    
	    go_button = (Button) findViewById(R.id.go_button);
	    go_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.go_button)){
					// get the selected IDs
					int numOfLineups = 3;
					Lineup[] lineups = new Lineup[numOfLineups];
					
					// get the practice ID
					SharedPreferences sharedPref = context.getSharedPreferences(
					        getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
					int currentPracticeID = sharedPref.getInt(getString(R.string.CURRENT_PRACTICE_ID), 8);
					// create the practice
					Practice currentPractice = new Practice(currentPracticeID);
					// add a piece
					Piece firstPiece = new Piece(lineups, roster, boatList);
					currentPractice.addPiece(firstPiece);
					// write the practice to a file
					DataSaver.writeObject(currentPractice, getString(R.string.PIECE_ID_FILE) + currentPracticeID, context);
					// save the piece in shared prefs
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putLong(getString(R.string.CURRENT_PIECE_ID), firstPiece.getPieceID());
					editor.apply();
					ArrayList<Long> pieceIDs = new ArrayList<Long>();
					pieceIDs.add(firstPiece.getPieceID());
					DataSaver.writeObject(pieceIDs, getString(R.string.PIECE_ID_FILE), context);
					launchPickDistTime();
				}
			}
		});
	}
	
	private void launchPickDistTime(){
		Intent pickDistTimeIntent = new Intent(this, PickDistTimeActivity.class);
		startActivity(pickDistTimeIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crew_selector, menu);
		return true;
	}
	
	private Context getContext(){
		return this;
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
			View rootView = inflater.inflate(R.layout.fragment_crew_selector,
					container, false);
			return rootView;
		}
	}

}
