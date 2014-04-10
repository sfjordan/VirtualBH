package com.vbh.virtualboathouse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Splashscreenactivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreenactivity);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			}
		Button recordTimes = (Button) findViewById(R.id.record_times_button);
		Button changeLinups = (Button) findViewById(R.id.change_lineups_button);
		//icons are 35dp, 5dp padding
		recordTimes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.record_times_button)){
					launchBoatPickers();
				}
			}
		});
		FileInputStream fis;
		try {
			fis = this.openFileInput("userData");
		} catch (FileNotFoundException e) {
			return;
		}
		ObjectInputStream is;
		try {
			is = new ObjectInputStream(fis);
		} catch (Exception e) {
			return;
		}
		CurrentUser cu;
		try {
			cu = (CurrentUser) is.readObject();
		} catch (Exception e) {
			return;
		}
		try {
			is.close();
		} catch (IOException e) {
			return;
		}
		String username = cu.getUsername();
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText(username);
		TextView tv1 = (TextView) findViewById(R.id.textView2);
		tv1.setText(cu.getAPIKey());
	}

	private void launchBoatPickers() {
		Intent displayBoatPickersIntent = new Intent(this, PickDistTimeActivity.class);
		startActivity(displayBoatPickersIntent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splashscreenactivity, menu);
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
			View rootView = inflater.inflate(
					R.layout.fragment_splashscreenactivity, container, false);
			return rootView;
		}
	}

}
