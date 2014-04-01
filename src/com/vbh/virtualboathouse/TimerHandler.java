package com.vbh.virtualboathouse;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class TimerHandler extends Activity implements OnClickListener {
	
	private boolean stopped = false;
	private long lastStop;
	
	@Override
    public void onClick(View v) {
        if(DisplayTimersActivity.start_button == v)
        {
        	stopped = false;
		    DisplayTimersActivity.milli_chrono.setBase(SystemClock.elapsedRealtime());
		    DisplayTimersActivity.milli_chrono.start();
        }
        else if(DisplayTimersActivity.stop_button == v){
		    if (!stopped) {
		    	lastStop = SystemClock.elapsedRealtime();
		    	
			    DisplayTimersActivity.milli_chrono.updateText(lastStop);
			    DisplayTimersActivity.milli_chrono.stop();
		    	stopped = true;
		    }
        }
        else if(DisplayTimersActivity.clear_button == v){
        	if (stopped) {
        		DisplayTimersActivity.milli_chrono.updateText(DisplayTimersActivity.milli_chrono.getBase());
        	}
        }
    }

}
