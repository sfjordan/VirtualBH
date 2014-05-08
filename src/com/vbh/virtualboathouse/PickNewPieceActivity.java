package com.vbh.virtualboathouse;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class PickNewPieceActivity extends Activity {

	private long currentPieceID;
	private Piece currentPiece;
	private Practice currentPractice;
	private int currentPracticeID;
	private SharedPreferences sharedPref;
	private Roster roster;
	private Map<Integer, Boat> boatList;
	private EditText notes;
	private EditText directionText;
	private String noteString = null;
	private String directionString = null;
	
	public final static String PICK_NEW_PIECE_ACTIVITY = "PickNewPieceActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_new_piece);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		RelativeLayout rl = (RelativeLayout)findViewById(R.id.container);
		Button changeLineups = (Button) findViewById(R.id.change_lineups_button);
		Button newPiece = (Button) findViewById(R.id.new_piece_button);
		Button finishPractice = (Button) findViewById(R.id.finish_practice_button);
		notes = (EditText) findViewById(R.id.notes_text);
		directionText = (EditText) findViewById(R.id.direction_text);
		
		
		// pull information about the current practice/last piece
		sharedPref = this.getSharedPreferences(getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE); // TODO make sure this is saved on flip or recreated
		currentPracticeID = sharedPref.getInt(getString(R.string.CURRENT_PRACTICE_ID), 8);
		currentPractice = DataSaver.readObject(getString(R.string.PRACTICE_FILE) + currentPracticeID, this);
		currentPieceID = sharedPref.getLong(getString(R.string.CURRENT_PIECE_ID), 8);
		currentPiece = currentPractice.getPiece(currentPieceID);
		
		notes.setText(sharedPref.getString("NOTES", ""));
		directionText.setText(sharedPref.getString("DIRECTION",""));
		
		boatList = DataSaver.readObject(getContext().getString(R.string.BOATS_FILE), getContext());
		// TODO check for null
		Log.i("picknewpiece", "boatList is currently null: " + (boatList==null));
		roster = DataSaver.readObject(getContext().getString(R.string.ROSTER_FILE), getContext());
		// TODO check for null
		Log.i("picknewpiece", "roster is currently null: " + (roster==null));
	
        if (currentPiece.isTimed()) {
        	//nothing, apparently
        }
        /*else if (currentPiece.isCountdown()){
        	//create margin text field
        	EditText marginText = new EditText(this);
        	marginText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        	marginText.setGravity(Gravity.CENTER_HORIZONTAL |Gravity.TOP);
        	marginText.setHint("Enter Margin...");
        	rl.addView(marginText);
        	marginText.requestFocus();
        	
        }*/
        //set onclicks
        changeLineups.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v==findViewById(R.id.change_lineups_button))
					ChangeLineups();
			}
		});
        newPiece.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v==findViewById(R.id.new_piece_button))
					NewPiece();
			}
		});
        finishPractice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v==findViewById(R.id.finish_practice_button))
					FinishPractice();
			}
		});
	}
	
	private void ChangeLineups() {
		if(!notes.getText().toString().isEmpty())
    		noteString = notes.getText().toString();
		if(!directionText.getText().toString().isEmpty())
    		directionString = directionText.getText().toString();
		sharedPref.edit().putString("NOTES", noteString).apply();
		sharedPref.edit().putString("DIRECTION", directionString).apply();
		Intent changeLineupsIntent = new Intent(this, ChangeLineupsList.class);
		changeLineupsIntent.putExtra(getString(R.string.ACTIVITY_FROM), PICK_NEW_PIECE_ACTIVITY);
		startActivity(changeLineupsIntent);
	}
	
	private void NewPiece() {
		if(!notes.getText().toString().isEmpty())
    		currentPiece.addNotes(notes.getText().toString());
		if(!directionText.getText().toString().isEmpty())
    		currentPiece.setDirection(directionText.getText().toString());
		noteString = null;
		directionString = null;
		sharedPref.edit().putString("NOTES", "").apply();
		sharedPref.edit().putString("DIRECTION", "").apply();
		// save current piece to practice
    	currentPractice.addPiece(currentPiece);
    	// make new piece
    	//Piece newPiece = new Piece(currentPiece);
    	Piece newPiece = new Piece(currentPractice.getCurrentLineupIDList(), roster, boatList);
    	currentPieceID = newPiece.getPieceID();
    	currentPractice.addPiece(newPiece);
    	// write practice to file
    	DataSaver.writeObject(currentPractice, getString(R.string.PRACTICE_FILE) + currentPracticeID, this);
    	// update sharedPrefs - note the apply() at the end, not saved otherwise.
    	sharedPref.edit().putLong(getString(R.string.CURRENT_PIECE_ID), currentPieceID).apply();
    	sharedPref.edit().putBoolean("DATA_SET_CHANGED", true).apply();
    	// new activity
		Intent newPieceIntent = new Intent(this, PickDistTimeActivity.class);
		newPieceIntent.putExtra(getString(R.string.ACTIVITY_FROM), PICK_NEW_PIECE_ACTIVITY);
		startActivity(newPieceIntent);
	}
	
	private void FinishPractice() {
		if(!notes.getText().toString().isEmpty())
    		currentPiece.addNotes(notes.getText().toString());
		if(!directionText.getText().toString().isEmpty())
    		currentPiece.setDirection(directionText.getText().toString());
		noteString = null;
		directionString = null;
		sharedPref.edit().putString("NOTES", "").apply();
		sharedPref.edit().putString("DIRECTION", "").apply();
		// save current piece to practice
    	currentPractice.addPiece(currentPiece);
    	// write practice to file
    	DataSaver.writeObject(currentPractice, getString(R.string.PRACTICE_FILE) + currentPracticeID, this);
    	sharedPref.edit().putInt(this.getString(R.string.PRACTICE_TO_UPLOAD_ID), currentPracticeID).apply();
    	sharedPref.edit().putBoolean("DATA_SET_CHANGED", true).apply();
		//TODO confirmation dialog
    	AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
		alert.setTitle("Are you sure?");
		alert.setMessage(R.string.confirm_finish_practice);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		     //Do something here where "ok" clicked
		    	updateData();
		    	SplashscreenActivity.updateSyncTextInProgress();
		    	Intent finishPracticeIntent = new Intent(getContext(), PracticeViewActivity.class); //SplashscreenActivity.class);
				startActivity(finishPracticeIntent);
		    }
		});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    //Do something here when "cancel" clicked.
		    }
		});
		alert.show();
		
	}
	
	private boolean updateData() {
		boolean success = true;
		DataRetriever dr = new DataRetriever(this);
		dr.getAthletesAndBoats();

		return success;
	}
	
	private Context getContext(){
		return this;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pick_new_piece, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_pick_new_piece,
					container, false);
			return rootView;
		}
	}

}
