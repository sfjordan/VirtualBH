package com.vbh.virtualboathouse;

import android.app.Activity;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.vbh.virtualboathouse.MilliChrono;


public class TimerHandler extends Activity implements OnClickListener {
	
	public boolean stopped = true;
	private Button start;
	private Button stop;
	private Button clear;
	private MilliChrono clock;
	private Timer t;
	
	public TimerHandler(Timer t, Button start, Button stop, Button clear, MilliChrono clock) {
		this.start = start;
		this.stop  = stop;
		this.clear = clear;
		this.clock = clock;
		this.t = t;
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
		    if (!stopped) {
			    clock.stop();
		    	stopped = true;
		    	t.stopped = true;
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
        /*else if(clear == v){
        	if (stopped) {
        		clock.clear();
        	}
        }*/
    }

}
