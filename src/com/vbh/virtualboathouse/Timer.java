package com.vbh.virtualboathouse;

import android.widget.Button;

public class Timer {

	public static MilliChrono milli_chrono;
	public static Button stop_button;
	public static Button start_button;
	public static Button clear_button;
	
	public Timer() {
		
		TimerHandler th = new TimerHandler();
		
		
		start_button.setOnClickListener(th);
		stop_button.setOnClickListener(th);
		clear_button.setOnClickListener(th);
	}
	
	private void addComponents() {
		
	}
	
}
