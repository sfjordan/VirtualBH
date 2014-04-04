package com.vbh.virtualboathouse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class TimerControlsHandler extends Activity implements OnClickListener {
	
	private Timer[] timers;
	private Context context;
	
	public TimerControlsHandler(Timer[] timers, Context context) {
		this.timers = timers;
		this.context = context;
	}
	
	@Override
    public void onClick(View v) {
        if(DisplayTimersActivity.start_all == v) {
        	for(Timer t : timers) {
        		t.start();
        	}
        }
        else if(DisplayTimersActivity.stop_all == v){
        	for(Timer t : timers) {
    			t.stop();
    		}
        }
        else if(DisplayTimersActivity.save_times == v){
        	AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        	builder.setMessage(R.string.dialog_message);
        	AlertDialog saveDialog = builder.create();
        	saveDialog.show();
        	
        }
    }

}

