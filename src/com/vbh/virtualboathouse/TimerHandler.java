package com.vbh.virtualboathouse;

import android.app.Activity;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.vbh.virtualboathouse.MilliChrono;


public class TimerHandler extends Activity implements OnClickListener {
	
	private boolean stopped = true;
	@SuppressWarnings("unused")
	private long lastStop;
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
        	}
        }
        else if(stop == v){
		    if (!stopped) {
		    	lastStop = SystemClock.elapsedRealtime();
			    clock.stop();
		    	stopped = true;
		    }
        }
        else if(clear == v){
        	if (stopped) {
        		clock.clear();
        	}
        }
    }

}
