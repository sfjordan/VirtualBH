package com.vbh.virtualboathouse;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vbh.virtualboathouse.MilliChrono;


public class TimerHandler extends Activity implements OnClickListener {
	
	// instance variables for timer
	public boolean stopped = true;
	private Button start;
	private Button stop;
	private Button clear;
	private MilliChrono clock;
	private Timer t;
	
	// instance variables for stroke rating
	private TextView boatName;
	private int numClicks = 0;
	private int strokeCount = 2;
	private long times[] = new long[strokeCount+1];
	private Handler h;
	private String boatNameString;
	private Context c;
	private ArrayList<String> notes;
	
	public TimerHandler(Timer t, Button start, Button stop, Button clear, MilliChrono clock, TextView boatName, String boatNameString, Context c) {
		this.start = start;
		this.stop  = stop;
		this.clear = clear;
		this.clock = clock;
		this.t = t;
		this.boatName = boatName;
		this.boatNameString = boatNameString;
		this.c = c;
		h = new Handler();
		notes = new ArrayList<String>();
	}
	
	public String getBoatNameString() {
		return boatNameString;
	}
	
	public void setStoppedStatus(boolean stopped) {
		this.stopped = stopped;
	}
	@Override
    public void onClick(View v) {
        if(start == v)
        {
        	if (stopped) {
        		stopped = false;
        		t.setStopped(false);
;		    	clock.setBase(SystemClock.elapsedRealtime());
		    	clock.start();
		    	//set stop button text to stop
		    	stop.setText("Stop");
				stop.setTextColor(Color.parseColor("white"));
        	}
        }
        else if(stop == v){
		    if (!stopped) {
			    clock.stop();
		    	stopped = true;
		    	t.setStopped(true);
		    	//set stop button text to clear 
		    	stop.setText("Clear");
				stop.setTextColor(Color.parseColor("white"));
		    }
		    else {
		    	clock.clear();
		    	//set stop button text to stop
		    	stop.setText("Stop");
				stop.setTextColor(Color.parseColor("white"));
		    }
        }
        else if (boatName == v) {
        	times[numClicks] = System.currentTimeMillis();
			numClicks++;
			boatName.setBackgroundColor(c.getResources().getColor(R.color.apptheme_color));
			Runnable flash = new Runnable () {
				public void run() {
					boatName.setBackgroundResource(Color.TRANSPARENT);
				}
			};
			h.postDelayed(flash, 50);
			if (numClicks == strokeCount + 1) {
				double strokeRate = 0.0;
				for (int i = 0; i < strokeCount; i++) {
					strokeRate += 60000.0/(times[i+1] - times[i]);
				}
				strokeRate = strokeRate/strokeCount;
				DecimalFormat df = new DecimalFormat("###0.0");
				String strStrokeRate = df.format(strokeRate);
				boatName.setText(
						Html.fromHtml("<big>"+strStrokeRate+"</big>"
						+"<br>"+"<small><font color=\"gray\">"
						+c.getResources().getString(R.string.strokes_per_minute)+"</font></small>"));
				numClicks = 0;
				for(long l : times)
					l = 0;
				h.removeCallbacksAndMessages(null);
				h.postDelayed(flash, 25);
				Runnable name = new Runnable () {
					public void run() {
						boatName.setText(boatNameString);
						boatName.setTextSize(30);
						
					}
				};
				h.postDelayed(name, 4000);
				long timeElapsed = clock.getElapsedTime();
				String note = strStrokeRate +"spm @ " + timeToString(timeElapsed) ;
				notes.add(note);
			}
        }
        /*else if(clear == v){
        	if (stopped) {
        		clock.clear();
        	}
        }*/
    }
    public String timeToString(long time) {
    	DecimalFormat df = new DecimalFormat("00");
        
        int hours = (int)(time / (3600 * 1000));
        int remaining = (int)(time % (3600 * 1000));
        
        int minutes = (int)(remaining / (60 * 1000));
        remaining = (int)(remaining % (60 * 1000));
        
        int seconds = (int)(remaining / 1000);
        
        String text = "";
        
        if (hours > 0) {
        	text += df.format(hours) + ":";
        }
        
       	text += df.format(minutes) + ":";
       	text += df.format(seconds);
       	return text;
    }
    
    public ArrayList<String> getNotes() {
    	return notes;
    }

}
