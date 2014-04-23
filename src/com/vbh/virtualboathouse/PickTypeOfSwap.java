package com.vbh.virtualboathouse;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.os.Build;

public class PickTypeOfSwap extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_type_of_swap);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	
		//Button changeLineup = (Button) findViewById(R.id.change_lineup_button);
		//Button makeSwap = (Button) findViewById(R.id.make_swap_button);
		
		/*changeLineup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v==findViewById(R.id.new_piece_button))
					changeLineup();
			}
		});
		
		makeSwap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v==findViewById(R.id.new_piece_button))
					makeSwap();
			}
		});*/
	
	}
	
	private void changeLineup(){
		
	}
	
	private void makeSwap(){
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pick_type_of_swap, menu);
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
					R.layout.fragment_pick_type_of_swap, container, false);
			return rootView;
		}
	}

}
