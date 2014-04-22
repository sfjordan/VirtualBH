package com.vbh.virtualboathouse;

import com.vbh.virtualboathouse.DisplayTimersActivity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PickNumBoatsActivity extends Activity {

	public final static String NUM_TIMERS = "com.vbh.virtualboathouse.NUM_TIMERS";
	private EditText numBoatsEdit;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		Button goButton = (Button) findViewById(R.id.go_button);		

		numBoatsEdit = (EditText)findViewById(R.id.enter_num_field);
		numBoatsEdit.setGravity(Gravity.CENTER);
		goButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v == findViewById(R.id.go_button)) 
					if(!numBoatsEdit.getText().toString().isEmpty())
						if (Integer.parseInt(numBoatsEdit.getText().toString()) > 50
							|| Integer.parseInt(numBoatsEdit.getText().toString()) < 1) {
							AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				        	builder.setMessage(R.string.invalid_boats_error_message);
				        	AlertDialog invalidboatsDialog = builder.create();
				        	invalidboatsDialog.show();
						}
						else displayTimers(); }
			});
	}

	private void displayTimers() {
		Intent displayTimersIntent = new Intent(this, DisplayTimersActivity.class);
		String message = numBoatsEdit.getText().toString();
		displayTimersIntent.putExtra(NUM_TIMERS, message); 
		startActivity(displayTimersIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
