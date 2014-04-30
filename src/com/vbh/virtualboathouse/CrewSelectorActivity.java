package com.vbh.virtualboathouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.os.Build;

public class CrewSelectorActivity extends Activity {
	
	private Button go_button;
	private LinearLayout lineups_checklist;
	private String fromstr;

	private Context context;
	
	private Roster roster;
	private Map<Integer, Boat> boatList;
	private Map<Long, Lineup> lineups;
	private CheckBox[] lineupBoxes;
	
	private int currentPracticeID;
	private Practice currentPractice;	
	public final static String CREW_SELECTOR_ACTIVITY = "CrewSelectorActivity";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crew_selector);
		context = this;
		lineups_checklist = (LinearLayout) findViewById(R.id.lineup_checklist);
		if (savedInstanceState == null) {
			// get the practice ID
			SharedPreferences sharedPref = context.getSharedPreferences(
			        getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
			currentPracticeID = sharedPref.getInt(getString(R.string.CURRENT_PRACTICE_ID), 8);
			// get the current practice from a file
			currentPractice = DataSaver.readObject(getString(R.string.PRACTICE_FILE) + currentPracticeID, context);
			// get the roster and boatlist
			boatList = DataSaver.readObject(context.getString(R.string.BOATS_FILE), context);
			// TODO check for null
    		roster = DataSaver.readObject(context.getString(R.string.ROSTER_FILE), context);
    		// TODO check for null
			
			lineups = currentPractice.getLineups();
			String[] lineupNames = new String[lineups.size()];
			int[] lineupIDs = new int[lineups.size()];
			int i = 0;
			lineupBoxes = new CheckBox[lineupNames.length];
			for (Long id : lineups.keySet()) {
				Lineup l = lineups.get(id);
				// uses stroke seat by default here
				// TODO implement adjustable setting
				int numSeats = l.getNumOfSeats();
				Log.i("CrewSelector", "number of seats is " + numSeats);
				Athlete stroke = l.getAthleteFromSeat(numSeats, roster);
				Log.i("CrewSelector", "Athlete is null " + (stroke == null));
				String name = stroke.getFirstInitLastName();
				Log.i("CrewSelector", "Stroke's name is " + name);
				lineupBoxes[i] = new CheckBox(this);
				lineupBoxes[i].setText(name);
				//lineupBoxes[i].setText(l.getAthleteFromSeat(l.getNumOfSeats(), roster).getFirstInitLastName());
				lineupBoxes[i].setTag(l);
				lineups_checklist.addView(lineupBoxes[i]);
				i++;
			}
		}
		else {
			// TODO bundling!!!!
			// reinstantiate data structures
			roster      = (Roster) savedInstanceState.getSerializable(DataSaver.STATE_ROSTER);
	        boatList = (HashMap<Integer, Boat>) savedInstanceState.getSerializable(DataSaver.STATE_BOATS);
	      //  lineups     = savedInstanceState.getSparseParcelableArray(DataSaver.STATE_LINEUPS);
	        lineupBoxes = (CheckBox[]) savedInstanceState.getSerializable(DataSaver.STATE_LINEUPS_CHECKBOXES);
			// TODO ensure check boxes are kept in the right state
		}
		
		Bundle b = getIntent().getExtras();
		if (b!=null){
			fromstr = b.getString(getString(R.string.ACTIVITY_FROM));
		}

	    
	    go_button = (Button) findViewById(R.id.go_button);
	    go_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.go_button)){
					// get the selected IDs
					ArrayList<Long> listLineupIDs = new ArrayList<Long>();
					for (int i = 0; i < lineupBoxes.length; i++) {
						if (lineupBoxes[i].isChecked()) {
							Lineup l = (Lineup) lineupBoxes[i].getTag();
							currentPractice.addCurrentLineup(l);
							listLineupIDs.add(l.getLineupID());
						}
					}
					SharedPreferences sharedPref = context.getSharedPreferences(
					        getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
					// add a piece
					Piece firstPiece = new Piece(listLineupIDs, roster, boatList);
					currentPractice.addPiece(firstPiece);
					// write the practice to a file
					DataSaver.writeObject(currentPractice, getString(R.string.PRACTICE_FILE) + currentPracticeID, context);
					// save the piece in shared prefs
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putLong(getString(R.string.CURRENT_PIECE_ID), firstPiece.getPieceID());
					editor.apply();
					ArrayList<Long> pieceIDs = new ArrayList<Long>();
					pieceIDs.add(firstPiece.getPieceID());
					DataSaver.writeObject(pieceIDs, getString(R.string.PIECE_ID_FILE), context);
					if (fromstr.equals("recordTimes"))
						launchPickDistTime();
					else if (fromstr.equals("changeLineups"))
						changeLineups();
				}
			}
		});
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the current practice state TODO
		savedInstanceState.putSerializable(DataSaver.STATE_ROSTER, roster);
        //savedInstanceState.putSparseParcelableArray(DataSaver.STATE_BOATS, boatList);
        //savedInstanceState.putSparseParcelableArray(DataSaver.STATE_LINEUPS_ARRAY, lineups);
        //savedInstanceState.putSerializable(DataSaver.STATE_LINEUPS_CHECKBOXES, lineupBoxes);
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	       // TODO logic to decide on which text box
	    }
	}
	
	public void changeLineups(){
		Intent changeLineupsIntent = new Intent(this, ChangeLineupsList.class);
		changeLineupsIntent.putExtra(getString(R.string.ACTIVITY_FROM), CREW_SELECTOR_ACTIVITY);
		startActivity(changeLineupsIntent);
		
	}
	
	private void launchPickDistTime(){
		Intent pickDistTimeIntent = new Intent(this, PickDistTimeActivity.class);
		pickDistTimeIntent.putExtra(getString(R.string.ACTIVITY_FROM), CREW_SELECTOR_ACTIVITY);
		startActivity(pickDistTimeIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crew_selector, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_crew_selector,
					container, false);
			return rootView;
		}
	}

}
