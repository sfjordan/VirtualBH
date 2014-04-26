package com.vbh.virtualboathouse;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Timer {

	public MilliChrono milli_chrono;
	public Button stop_button;
	public Button start_button;
	public Button clear_button;
	public TextView boat_name;
	private boolean stopped;
	private Context context;
	private String name;
	private TimerHandler th;
	private LinearLayout ll;
	
	public Timer(Context c, LinearLayout ll, String name) {
		this.context = c;
		this.ll = ll;
		this.name = name;
		stopped = true;
		boat_name = new TextView(context);
		addComponents();
		setHandlers();
	}
	public long getElapsedTime() {
		return milli_chrono.getElapsedTime();
	}
	
	public Boolean isStopped(){
		return stopped;
	}
	
	public void setStopped(Boolean b){
		stopped = b;
	}
	
	public void start() {
		stopped = false;
		th.onClick(start_button);
	}
	
	public void stop() {
		stopped = true;
		th.onClick(stop_button);
	}
	
	public void clear() {
		stopped = true;
		th.onClick(clear_button);
	}
	
	private void setHandlers() {
		th = new TimerHandler(this, start_button, stop_button, clear_button, milli_chrono, boat_name, name, context);
		start_button.setOnClickListener(th);
		stop_button.setOnClickListener(th);
		clear_button.setOnClickListener(th);
		boat_name.setOnClickListener(th);
	}
	
	private void addComponents() {
		// set layout for entire timer components
		LinearLayout whole_timer = new LinearLayout(context);
		whole_timer.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout timer_and_buttons = new LinearLayout(context);
		LinearLayout timer_buttons = new LinearLayout(context);
		// set the TextView layout/text
		boat_name.setText(this.name);
		boat_name.setTextSize(30);
		boat_name.setGravity(Gravity.CENTER_HORIZONTAL |Gravity.CENTER_VERTICAL);
		// divide up space between name and timer functionality using weights
		LinearLayout.LayoutParams lp_tb = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
		lp_tb.weight = 3;
		lp_tb.setMargins(20, 5, 10, 5);
		LinearLayout.LayoutParams lp_bn = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
		lp_bn.weight = 2;
		lp_bn.setMargins(0, 5, 15, 5);
		whole_timer.addView(timer_and_buttons, lp_tb);
		whole_timer.addView(boat_name, lp_bn);
		// set the timer defaults
		milli_chrono = new MilliChrono(context);
		milli_chrono.setTextSize(50);
		milli_chrono.setGravity(Gravity.CENTER_HORIZONTAL);
		timer_and_buttons.setOrientation(LinearLayout.VERTICAL);
		timer_and_buttons.addView(milli_chrono);
		timer_and_buttons.addView(timer_buttons);
		timer_buttons.setOrientation(LinearLayout.HORIZONTAL);
		// adjust the buttons for control
		start_button = new Button(context);
		clear_button = new Button(context);
		stop_button = new Button(context);
		start_button.setText(context.getResources().getString(R.string.button_start));
		start_button.setTextColor(Color.parseColor("white"));
		clear_button.setText(context.getResources().getString(R.string.button_clear));
		clear_button.setTextColor(Color.parseColor("white"));
		stop_button.setText(context.getResources().getString(R.string.button_stop));
		stop_button.setTextColor(Color.parseColor("white"));
		// give equal weight
		LinearLayout.LayoutParams lp_start = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
		lp_start.weight = 1;
		LinearLayout.LayoutParams lp_clear = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
		lp_clear.weight = 1;
		LinearLayout.LayoutParams lp_stop = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
		lp_stop.weight = 1;
		// add to the view
		timer_buttons.addView(start_button, lp_start);
		//timer_buttons.addView(clear_button, lp_clear); NOTE: stop button now doubles as clear button
		timer_buttons.addView(stop_button, lp_stop);
		this.ll.addView(whole_timer);
	}

	public void restart(long time, boolean state) {
		th.setStoppedStatus(!state);
		milli_chrono.restartTimer(state, time);
		setStopped(!state);
		if (isStopped() && !milli_chrono.isZeroed()){
			stop_button.setText(context.getResources().getString(R.string.button_clear));
			stop_button.setTextColor(Color.parseColor("white"));
		}
	}

	public long getRestartBase() {
		return milli_chrono.saveTime();
	}

	public boolean getRestartState() {
		return milli_chrono.saveState();
	}
	
}
