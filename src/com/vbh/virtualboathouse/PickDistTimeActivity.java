package com.vbh.virtualboathouse;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.os.Build;

public class PickDistTimeActivity extends Activity {
	
	private EditText distanceEdit;
	private EditText timeEdit;
	private Button goPickPiece;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_dist_time);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		goPickPiece = (Button) findViewById(R.id.goPickPiece);
		distanceEdit = (EditText) findViewById(R.id.enter_distance);
		timeEdit = (EditText) findViewById(R.id.enter_time);
		goPickPiece.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v == findViewById(R.id.goPickPiece)) 
					if(!distanceEdit.getText().toString().isEmpty() && timeEdit.getText().toString().isEmpty()) 
						displayDistance();
					else if(!timeEdit.getText().toString().isEmpty() && distanceEdit.getText().toString().isEmpty())
						displayTime();
					else {
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			        	builder.setMessage(R.string.pick_disttime_error_message);
			        	AlertDialog saveDialog = builder.create();
			        	saveDialog.show();
					}
				}
			});
		
	}
	
	private void displayDistance(){
		Intent displayMainIntent = new Intent(this, MainActivity.class);
		startActivity(displayMainIntent);
	}
	
	private void displayTime(){
		Intent countdownActivity = new Intent(this, CountdownActivity.class);
		startActivity(countdownActivity);
	}
	
	private Context getContext(){
		return this;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pick_dist_time, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_pick_dist_time,
					container, false);
			return rootView;
		}
	}

}
