package com.vbh.virtualboathouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
    		
    		Log.i("crewselector","current lineups are:");
    		Iterator<Entry<Long, Lineup>> currentLineups = currentPractice.getCurrentLineups().entrySet().iterator();
	    	while(currentLineups.hasNext()){
	    		currentLineups.next().getValue().printLineup();			        		    		
	    	}
    		
    		//currentPractice.clearCurrentLineups();
			
			lineups = currentPractice.getCurrentLineups();
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
				String name = l.getName();
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
					int numChecked = 0;
					for (int i = 0; i < lineupBoxes.length; i++) {
						Log.i("crewselector","lineupBoxes["+i+"].isChecked(): "+lineupBoxes[i].isChecked());
						if (lineupBoxes[i].isChecked()) {
							numChecked++;
							Lineup l = (Lineup) lineupBoxes[i].getTag();
							Log.i("crewselector","adding this lineup to currentlineups:");
							l.printLineup();
							currentPractice.addCurrentLineup(l);
							listLineupIDs.add(l.getLineupID());
							Log.i("crewselector", "l.getLineupID: "+l.getLineupID());
						}
					}
					if (numChecked == 0 && fromstr.equals("recordTimes")){
						//dialog to get number of boats, continue straight to timers
						showGenericDialog();
						
					}
					else {
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
			}
		});
	}
	
	private void showGenericDialog(){
		final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);

		final TextView titleBox = new TextView(getContext());
		titleBox.setGravity(Gravity.CENTER_HORIZONTAL);
		titleBox.setText(R.string.no_selected_crews);
		titleBox.setPadding(5, 5, 5, 5);
		layout.addView(titleBox);
		
		final EditText input = new EditText(getContext());
		input.setGravity(Gravity.CENTER);
	    input.setInputType(InputType.TYPE_CLASS_NUMBER);
	    input.setHint(R.string.num_generic_boats_hint);
	    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	    	     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    params.setMargins(230, 0, 230, 0);
	    input.setLayoutParams(params);
	    layout.addView(input);


		alert.setView(layout);
		alert.setCancelable(false);
	    
	    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            int value;
	            if (input.getText().length() == 0)
	            	value = 3;
	            else value = Integer.parseInt(input.getText().toString());
	            if (value > 0 && value < 51){
		            Intent displayTimersIntent = new Intent(getContext(), DisplayTimersActivity.class);
					displayTimersIntent.putExtra(getString(R.string.ACTIVITY_FROM), CREW_SELECTOR_ACTIVITY);
					displayTimersIntent.putExtra("GENERIC_MODE", true);
					displayTimersIntent.putExtra(getString(R.string.CURRENT_NUM_BOATS), value); 
					startActivity(displayTimersIntent);
	            }
	            else showGenericDialog();
	        }
	    });

	    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            dialog.cancel();
	            //validInput = true;
	        }
	    });
	    alert.show();
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
		changeLineupsIntent.putExtra("GENERIC_MODE", false);
		changeLineupsIntent.putExtra(getString(R.string.ACTIVITY_FROM), CREW_SELECTOR_ACTIVITY);
		startActivity(changeLineupsIntent);
		
	}
	
	private void launchPickDistTime(){
		Intent pickDistTimeIntent = new Intent(this, PickDistTimeActivity.class);
		pickDistTimeIntent.putExtra("GENERIC_MODE", false);
		pickDistTimeIntent.putExtra(getString(R.string.ACTIVITY_FROM), CREW_SELECTOR_ACTIVITY);
		startActivity(pickDistTimeIntent);
	}
	
	private Context getContext(){
		return this;
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
