package com.vbh.virtualboathouse;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Timer {

	public static MilliChrono milli_chrono;
	public static Button stop_button;
	public static Button start_button;
	public static Button clear_button;
	public static TextView boat_name;
	private Context context;
	private String name;
	private TimerHandler th;
	private LinearLayout ll;
	
	public Timer(Context c, LinearLayout ll, String name) {
		this.context = c;
		this.ll = ll;
		this.name = name;
		addComponents();
		setHandlers();
	}
	
	private void setHandlers() {
		th = new TimerHandler(start_button, stop_button, clear_button, milli_chrono);
		start_button.setOnClickListener(th);
		stop_button.setOnClickListener(th);
		clear_button.setOnClickListener(th);
	}
	
	private void addComponents() {
		LinearLayout whole_timer = new LinearLayout(context);
		whole_timer.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout timer_and_buttons = new LinearLayout(context);
		LinearLayout timer_buttons = new LinearLayout(context);
		boat_name = new TextView(context);
		boat_name.setText(this.name);
		boat_name.setTextSize(30);
		boat_name.setGravity(Gravity.CENTER_HORIZONTAL |Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams lp_tb = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
		lp_tb.weight = 3;
		lp_tb.setMargins(20, 5, 10, 5);
		LinearLayout.LayoutParams lp_bn = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
		lp_bn.weight = 2;
		lp_bn.setMargins(0, 5, 15, 5);
		whole_timer.addView(timer_and_buttons, lp_tb);
		whole_timer.addView(boat_name, lp_bn);
		milli_chrono = new MilliChrono(context);
		milli_chrono.setTextSize(50);
		milli_chrono.setGravity(Gravity.CENTER_HORIZONTAL);
		timer_and_buttons.setOrientation(LinearLayout.VERTICAL);
		timer_and_buttons.addView(milli_chrono);
		timer_and_buttons.addView(timer_buttons);
		timer_buttons.setOrientation(LinearLayout.HORIZONTAL);
		start_button = new Button(context);
		clear_button = new Button(context);
		stop_button = new Button(context);
		start_button.setText(context.getResources().getString(R.string.button_start));
		clear_button.setText(context.getResources().getString(R.string.button_clear));
		stop_button.setText(context.getResources().getString(R.string.button_stop));
		LinearLayout.LayoutParams lp_start = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
		lp_start.weight = 1;
		LinearLayout.LayoutParams lp_clear = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
		lp_clear.weight = 1;
		LinearLayout.LayoutParams lp_stop = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
		lp_stop.weight = 1;
		timer_buttons.addView(start_button, lp_start);
		//timer_buttons.addView(clear_button, lp_clear);
		timer_buttons.addView(stop_button, lp_stop);
		this.ll.addView(whole_timer);
	}
	
}
