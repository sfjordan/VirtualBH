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

	private Context context;
	
	private Roster roster;
	private Map<Integer, Boat> boatList;
	private Map<Integer, Lineup> lineups;
	private CheckBox[] lineupBoxes;
	
	private int currentPracticeID;
	
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
			// currently, just get the most recent practice
			LineupModel lm[] = DataSaver.readObjectArray(getString(R.string.RECENT_LINEUP_FILE), this);
			Log.i("CrewSelector", "lm is currently null: " + (lm==null));
			DataRetriever dr = new DataRetriever(this);
			PracticeLineupsModel plm[] = DataSaver.readObjectArray(dr.RECENT_PRACTICE_DATA_FILENAME + currentPracticeID , this);
			System.out.println("plm is null: "+(plm==null));
			this.roster = DataSaver.readObject(getString(R.string.ROSTER_FILE), this);
			Log.i("CrewSelector", "roster is currently null: " + (roster==null));
			this.boatList = DataSaver.readObject(getString(R.string.BOATS_FILE), this);
			Log.i("CrewSelector", "boatList is currently null: " + (boatList==null));

			lineups = new HashMap<Integer, Lineup>(lm.length);
			String[] lineupNames = new String[lm.length];
			int[] lineupIDs = new int[lm.length];
			int i = 0;
			lineupBoxes = new CheckBox[lineupNames.length];
			for (LineupModel lineupModel : lm) {
				Lineup l = new Lineup(lineupModel, roster, boatList);
				lineups.put(l.getLineupID(), l);
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
			// reinstantiate data structures
			roster      = (Roster) savedInstanceState.getSerializable(DataSaver.STATE_ROSTER);
	        int[] boatIDs = savedInstanceState.getIntArray(DataSaver.STATE_BOAT_ID_ARRAY);
	       // boatList = savedInstanceState.getSparseParcelableArray(DataSaver.STATE_BOATS);
	      //  lineups     = savedInstanceState.getSparseParcelableArray(DataSaver.STATE_LINEUPS);
	        lineupBoxes = (CheckBox[]) savedInstanceState.getSerializable(DataSaver.STATE_LINEUPS_CHECKBOXES);
			// TODO ensure check boxes are kept in the right state
		}
		
		Bundle b = getIntent().getExtras();
		if (b!=null){
			String fromstr = b.getString("FROM");
			System.out.println("fromStr: "+fromstr);
		}

	    
	    go_button = (Button) findViewById(R.id.go_button);
	    go_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.go_button)){
					// get the selected IDs
					ArrayList<Lineup> listLineups = new ArrayList<Lineup>();
					for (int i = 0; i < lineupBoxes.length; i++) {
						if (lineupBoxes[i].isChecked()) 
							listLineups.add((Lineup) lineupBoxes[i].getTag());
					}
					int numOfLineups = 3;
					Lineup[] lineups = new Lineup[numOfLineups];
					
					//TODO
					//NOTE: this block generates a null pointer atm 
					// get the practice ID
					SharedPreferences sharedPref = context.getSharedPreferences(
					        getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
					int currentPracticeID = sharedPref.getInt(getString(R.string.CURRENT_PRACTICE_ID), 8);
					// create the practice
					Practice currentPractice = new Practice(currentPracticeID);
					// add a piece
					Piece firstPiece = new Piece(listLineups, roster, boatList);
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
					launchPickDistTime();
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
