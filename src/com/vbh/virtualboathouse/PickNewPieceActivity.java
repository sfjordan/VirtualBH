package com.vbh.virtualboathouse;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
		
		// pull information about the current practice/last piece
		sharedPref = this.getSharedPreferences(getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
		currentPracticeID = sharedPref.getInt(getString(R.string.CURRENT_PRACTICE_ID), 8);
		currentPractice = DataSaver.readObject(getString(R.string.PRACTICE_FILE) + currentPracticeID, this);
		currentPieceID = sharedPref.getLong(getString(R.string.CURRENT_PIECE_ID), 8);
		currentPiece = currentPractice.getPiece(currentPieceID);
	
        if (currentPiece.isTimed()) {
        	//nothing, apparently
        }
        else if (currentPiece.isCountdown()){
        	//create margin text field
        	EditText marginText = new EditText(this);
        	marginText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        	marginText.setGravity(Gravity.CENTER_HORIZONTAL |Gravity.TOP);
        	marginText.setHint("Enter Margin...");
        	rl.addView(marginText);
        	marginText.requestFocus();
        	
        }
        else System.out.println("Error in picknewpiece, shouldn't be here");
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
		//TODO: make actually do something other than return to splashscreen
		Intent changeLineupsIntent = new Intent(this, SplashscreenActivity.class);
		startActivity(changeLineupsIntent);
	}
	
	private void NewPiece() {
		// save current piece to practice
    	currentPractice.addPiece(currentPiece);
    	// make new piece
    	Piece newPiece = new Piece(currentPiece);
    	currentPieceID = newPiece.getPieceID();
    	currentPractice.addPiece(newPiece);
    	// write practice to file
    	DataSaver.writeObject(currentPractice, getString(R.string.PRACTICE_FILE) + currentPracticeID, this);
    	// update sharedPrefs - note the apply() at the end, not saved otherwise.
    	sharedPref.edit().putLong(getString(R.string.CURRENT_PIECE_ID), currentPieceID).apply();
    	// new activity
		Intent newPieceIntent = new Intent(this, PickDistTimeActivity.class);
		newPieceIntent.putExtra(getString(R.string.ACTIVITY_FROM), PICK_NEW_PIECE_ACTIVITY);
		startActivity(newPieceIntent);
	}
	
	private void FinishPractice() {
		// save current piece to practice
    	currentPractice.addPiece(currentPiece);
    	// write practice to file
    	DataSaver.writeObject(currentPractice, getString(R.string.PRACTICE_FILE) + currentPracticeID, this);
		//TODO confirmation dialog
	
		Intent finishPracticeIntent = new Intent(this, SplashscreenActivity.class);
		startActivity(finishPracticeIntent);
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
