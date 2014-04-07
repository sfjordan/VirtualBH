package com.vbh.virtualboathouse;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.vbh.virtualboathouse.MilliChrono;


public class TimerHandler extends Activity implements OnClickListener {
	
	private boolean stopped = true;
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
