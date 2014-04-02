package com.vbh.virtualboathouse;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayTimersActivity extends Activity {

	public static TextView field;
	public static MilliChrono milli_chrono;
	public static Button stop_button;
	public static Button start_button;
	public static Button clear_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_timers);
		
		//field = (TextView)findViewById(R.id.field);
		Intent intent = getIntent();
		//field.setText(intent.getStringExtra(MainActivity.NUM_TIMERS));
		int numTimers = Integer.parseInt(intent.getStringExtra(MainActivity.NUM_TIMERS));
		Timer[] timers = new Timer[numTimers];
		LinearLayout timer_list = (LinearLayout)findViewById(R.id.timers_list);
		for (int i = 0; i < 40; i++) {
			TextView hello = new TextView(this);
			hello.setText("This is row " + i);
			timer_list.addView(hello);
		}
//		milli_chrono = (MilliChrono)findViewById(R.id.millichrono);
//		stop_button=(Button)findViewById(R.id.stop_button);
//		start_button=(Button)findViewById(R.id.start_button);
//		clear_button=(Button)findViewById(R.id.clear_button);
//		TimerHandler th = new TimerHandler();
//		start_button.setOnClickListener(th);
//		stop_button.setOnClickListener(th);
//		clear_button.setOnClickListener(th);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_timers, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_display_timers,
					container, false);
			return rootView;
		}
	}

}
