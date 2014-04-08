package com.vbh.virtualboathouse;

/*
 * The Android chronometer widget revised so as to count milliseconds
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;
import java.text.DecimalFormat;

public class MilliChrono extends Chronometer {
    @SuppressWarnings("unused")
	private static final String TAG = "MilliChrono";

    private long mBase;
    private long mRunningTime;
    private long showRunningTime;
    private boolean mVisible;
    private boolean mStarted;
    private boolean mRunning;
    private static final int TICK_WHAT = 2;
    private long timeElapsed = 0L;
    private int STOPPED = 0;
    private int RUNNING = 1;
    private int CLEARED = 2;
    
    private OnChronometerTickListener mOnChronometerTickListener;

    public interface OnChronometerTickListener {

        void onChronometerTick(MilliChrono milli_chrono);
    }
    
    
    public MilliChrono(Context context) {
        this (context, null, 0);
    }

	public MilliChrono(Context context, AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public MilliChrono(Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);
        init();
    }

    private void init() {
    	mRunningTime = 0L;
        mBase = SystemClock.elapsedRealtime();
        updateText(mRunningTime);
    }
    public void setBase(long base) {
        mBase = base;
        dispatchChronometerTick();
        updateText(SystemClock.elapsedRealtime());
    }

    public long getBase() {
        return mBase;
    }
    
    public long getElapsedTime() {
    	return timeElapsed;
    }
    
    public void restartTimer(boolean running, long base) {
    	if (running) {
    		setBase(base);
    		setStarted(true);
    	}
    	else {
    		updateText(0L);
    	}
    }
    
    public long saveTime() {
    	if (mStarted) {
    		return mBase;
    	}
    	else {
    		return timeElapsed;
    	}
    }
    public boolean saveState() {
    	return mStarted;
    }

    public void setOnChronometerTickListener(
            OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }

    public android.widget.Chronometer.OnChronometerTickListener getOnChronometerTickListener() {
        return (android.widget.Chronometer.OnChronometerTickListener) mOnChronometerTickListener;
    }

    public void start() {
        mBase = SystemClock.elapsedRealtime() - timeElapsed;
        mStarted = true;
        updateRunning();
    }

    public void stop() {
        mStarted = false;
        timeElapsed = SystemClock.elapsedRealtime() - mBase;
        System.out.println("Running time: "+mRunningTime);
        updateRunning();
    }
    
    public void clear(){
    	mRunningTime = 0L;
    	timeElapsed = 0L;
    	mStarted = false;
    	updateText(timeElapsed);
    }


    public void setStarted(boolean started) {
        mStarted = started;
        updateRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super .onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super .onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }
    
    public synchronized void updateText(long time) {
       //timeElapsed = now - mBase;
        
        DecimalFormat df = new DecimalFormat("00");
        
        int hours = (int)(time / (3600 * 1000));
        int remaining = (int)(time % (3600 * 1000));
        
        int minutes = (int)(remaining / (60 * 1000));
        remaining = (int)(remaining % (60 * 1000));
        
        int seconds = (int)(remaining / 1000);
        remaining = (int)(remaining % (1000));
        
        int hundredths = (int)(((int)time % 1000) / 10);
        
        String text = "";
        
        if (hours > 0) {
        	text += df.format(hours) + ":";
        }
        
       	text += df.format(minutes) + ":";
       	text += df.format(seconds) + ".";
       	text += df.format(hundredths);   
        setText(text);
    }

    private void updateRunning() {
        boolean running = mVisible && mStarted;
        if (running != mRunning) {
            if (running) {
            	long now = SystemClock.elapsedRealtime() - mBase;
                updateText(now);
                dispatchChronometerTick();
                mHandler.sendMessageDelayed(Message.obtain(mHandler,
                        TICK_WHAT), 10);
            } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            mRunning = running;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mRunning) {
            	long now = SystemClock.elapsedRealtime() - mBase;
                updateText(now);
                dispatchChronometerTick();
                sendMessageDelayed(Message.obtain(this , TICK_WHAT), 10);
            }
        }
    };

    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }
    
}