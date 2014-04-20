package com.vbh.virtualboathouse;

import android.app.Activity;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.vbh.virtualboathouse.MilliChrono;


public class TimerHandler extends Activity implements OnClickListener {
	
	private boolean stopped = true;
	private Button start;
	private Button stop;
	private Button clear;
	private MilliChrono clock;
	
	public TimerHandler(Button start, Button stop, Button clear, MilliChrono clock) {
		this.start = start;
		this.stop  = stop;
		this.clear = clear;
		this.clock = clock;
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
		    	clock.setBase(SystemClock.elapsedRealtime());
		    	clock.start();
		    	//set stop button text to stop
		    	stop.setText("Stop");
				stop.setTextColor(Color.parseColor("white"));
        	}
        }
        else if(stop == v){
        	System.out.println("in TH stopping");
		    if (!stopped) {
		    	System.out.println("not stopped, stopping...");
			    clock.stop();
		    	stopped = true;
		    	//set stop button text to clear
		    	stop.setText("Clear");
				stop.setTextColor(Color.parseColor("white"));
		    }
		    else {
		    	System.out.println("stopped, clearing...");
		    	clock.clear();
		    	//set stop button text to stop
		    	stop.setText("Stop");
				stop.setTextColor(Color.parseColor("white"));
		    }
        }
        /*else if(clear == v){
        	if (stopped) {
        		clock.clear();
        	}
        }*/
    }

}
