package com.vbh.virtualboathouse;

import android.R.string;
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
import android.widget.TextView;
import android.os.Build;

import java.util.concurrent.TimeUnit;

import android.os.CountDownTimer;

public class CountdownActivity extends Activity {
	
	private Button start, cancel;
	private CountDownTimer CountDownTimer;	
	private TextView countdownText;
	private static final String FORMAT = "%02d:%02d";
	private long timeInMillis;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countdown);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		Bundle extras = getIntent().getExtras();
		int[] array = extras.getIntArray("numbers");
		int minutes = array[0];
		int seconds = array[1];
		timeInMillis = convertTime(minutes, seconds);
		
		countdownText = (TextView)findViewById(R.id.countdown_text);
		start = (Button)findViewById(R.id.start_button);
		cancel = (Button)findViewById(R.id.cancel_button);
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.start_button)){
					StartTimer();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.cancel_button)){
					CancelTimer();
				}
			}
		});
		
		
		//set countdownText initially:
		countdownText.setText(formatTime(timeInMillis));
		
		CountDownTimer = new CountDownTimer(timeInMillis, 1000) { // adjust the milli seconds here
	        public void onTick(long millisUntilFinished) {
	            countdownText.setText(formatTime(millisUntilFinished));              
	        }

	        public void onFinish() {
	            countdownText.setText("Done!");
	        }
	     };
	}
	
	private void StartTimer(){
		CountDownTimer.start();
		
	}
	private void CancelTimer(){
		CountDownTimer.cancel();
		countdownText.setText(formatTime(timeInMillis));
	}
	
	private long convertTime(int minutes, int seconds){
		long timeInMillis = 0L;
		timeInMillis += seconds*1000;
		timeInMillis += minutes*60000;
		return timeInMillis;		
	}
	
	private String formatTime(long timeInMillis){
		String formattedTime = ""+String.format(FORMAT,
                TimeUnit.MILLISECONDS.toMinutes(timeInMillis) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(timeInMillis)),
                TimeUnit.MILLISECONDS.toSeconds(timeInMillis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(timeInMillis)));
		return formattedTime;
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.countdown, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_countdown,
					container, false);
			return rootView;
		}
	}

}
