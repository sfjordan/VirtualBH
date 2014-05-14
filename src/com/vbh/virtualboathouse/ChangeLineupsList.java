/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vbh.virtualboathouse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Stack;

import com.vbh.virtualboathouse.CrewSelectorActivity.PlaceholderFragment;

/**
 * This application creates a listview where the ordering of the data set
 * can be modified in response to user touch events.
 *
 * An item in the listview is selected via a long press event and is then
 * moved around by tracking and following the movement of the user's finger.
 * When the item is released, it animates to its new position within the listview.
 */


public class ChangeLineupsList extends Activity {
	
	private int currentPracticeID;
	private Practice currentPractice;
	private Roster roster;
	private Map<Integer, Boat> boatList;
	private Map<Integer, Lineup> lineups;
	private ArrayList<AthleteListName> athleteList;
	private PracticeLineupsModel[] plm;
	private LineupModel[] lm;
	private String[] lineupNames;
	private int[] lineupIDs;
	private int numLineups;
	StableArrayAdapter adapter;
	Context context;
	
	private String fromstr;
	private SharedPreferences sharedPref;
	private TextView instructions;
	private Button button_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_list_view);
        button_done = (Button) findViewById(R.id.button_done);
		button_done.setVisibility(View.INVISIBLE);
		instructions = (TextView) findViewById(R.id.explanation_text);
		DynamicListView listView = (DynamicListView) findViewById(R.id.listview);
		
		fadeSwap();
        //setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
		if (savedInstanceState != null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
			fromstr = savedInstanceState.getString("FROM_STR");
			getData();
	        LayoutInflater inflater = LayoutInflater.from(this);
	        athleteList = buildList(new ArrayList<AthleteListName>());
	        adapter = new StableArrayAdapter(this, inflater, athleteList);
	        
	        
	
	        listView.setList(athleteList);
	        listView.setAdapter(adapter);
	        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	        
	        button_done.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v){
					if (v==findViewById(R.id.button_done)){
						if (isValidLineup()){
							saveData();
							Iterator<Entry<Long, Lineup>> currentLineups = currentPractice.getCurrentLineups().entrySet().iterator();
	        		    	while(currentLineups.hasNext()){
	        		    		currentLineups.next().getValue().printLineup();			        		    		
	        		    	}
	        		    	//continue to wherever you need to go
	        		    	if(fromstr.equals("PickNewPieceActivity")){
	        		    		launchPickNewPiece();        		    		
	        		    	}
	        		    	else if(fromstr.equals("CrewSelectorActivity")){
	        		    		launchSplashscreen();
	        		    	}
	        		    	
						}
						else {
							Log.i("changelineupslist","invalid lineup");
							AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				        	builder.setMessage(R.string.invalid_lineup_error_message);
				        	AlertDialog invalidLineupDialog = builder.create();
				        	invalidLineupDialog.show();
						}
					}
				}
			});
			
		}
		else {
			Bundle b = getIntent().getExtras();
			if (b!=null){
				fromstr = b.getString(getString(R.string.ACTIVITY_FROM));
			}
	        getData();
	        LayoutInflater inflater = LayoutInflater.from(this);
	        athleteList = buildList(new ArrayList<AthleteListName>());
	        adapter = new StableArrayAdapter(this, inflater, athleteList);
	        
	        
	
	        listView.setList(athleteList);
	        listView.setAdapter(adapter);
	        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	        
	        button_done.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v){
					if (v==findViewById(R.id.button_done)){
						if (isValidLineup()){
							saveData();
							Iterator<Entry<Long, Lineup>> currentLineups = currentPractice.getCurrentLineups().entrySet().iterator();
	        		    	while(currentLineups.hasNext()){
	        		    		currentLineups.next().getValue().printLineup();			        		    		
	        		    	}
	        		    	//continue to wherever you need to go
	        		    	if(fromstr.equals("PickNewPieceActivity")){
	        		    		launchPickNewPiece();        		    		
	        		    	}
	        		    	else if(fromstr.equals("CrewSelectorActivity")){
	        		    		launchSplashscreen();
	        		    	}
	        		    	
						}
						else {
							Log.i("changelineupslist","invalid lineup");
							AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				        	builder.setMessage(R.string.invalid_lineup_error_message);
				        	AlertDialog invalidLineupDialog = builder.create();
				        	invalidLineupDialog.show();
						}
					}
				}
			});
	    }
    }
    
    private void getData(){
    	// get the practice ID
		sharedPref = context.getSharedPreferences(
		        getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
		currentPracticeID = sharedPref.getInt(getString(R.string.CURRENT_PRACTICE_ID), 8);
		// get the current practice from a file
		currentPractice = DataSaver.readObject(getString(R.string.PRACTICE_FILE) + currentPracticeID, context);
		// get the roster and boatlist
		boatList = DataSaver.readObject(context.getString(R.string.BOATS_FILE), context);
		// TODO check for null
		Log.i("changelineuplist", "boatList is currently null: " + (boatList==null));
		roster = DataSaver.readObject(context.getString(R.string.ROSTER_FILE), context);
		// TODO check for null
		Log.i("changelineuplist", "roster is currently null: " + (roster==null));
		Log.i("getData","currentLineups START:");
		for (Lineup l : currentPractice.getCurrentLineupsList()){
			l.printLineup();
		}
		Log.i("getData","currentLineups END:");
    }
    
    private ArrayList<AthleteListName> buildList(ArrayList<AthleteListName> athleteList){
    	numLineups = 0;
    	Iterator<Entry<Long, Lineup>> currentLineups = currentPractice.getCurrentLineups().entrySet().iterator();
		while(currentLineups.hasNext()){
			Lineup lineup = currentLineups.next().getValue();
			lineup.printLineup();
			if (!lineup.isCoxed())
				athleteList.add(new AthleteListName(null, lineup.getBoatName(),lineup.getBoatID(),lineup.getNumOfSeats(),lineup.getPosition()));
			else athleteList.add(new AthleteListName(lineup.getCoxswainID(),lineup.getCoxswainName(),lineup.getBoatID(),lineup.getNumOfSeats(),lineup.getPosition()));
			int[] athleteIDs = lineup.getAthleteIDs();
			for(int a: athleteIDs){
				Athlete ath = roster.getAthlete(a);
				athleteList.add(new AthleteListName(ath.getFirstInitLastName(),ath.getSide(),a));
			}
			numLineups++;
		}
    	return athleteList;    	
    }
    
    private void saveData() {
    	LinkedList<Lineup> lineupsToSave = new LinkedList<Lineup>();
    	//clearCurrentLineups
    	currentPractice.clearCurrentLineups();
    	//build lineups
    	LinkedList<Lineup> lineups = makeLineups();
    	//check against list of known lineups
    	Lineup matchedLineup = null;
    	int n = 0;
    	for(Lineup newlineup: lineups){
    		Log.i("saveData,List","dealing with newlineup, boat:"+newlineup.getBoatName());
	    	Iterator<Entry<Long, Lineup>> allLineups = currentPractice.getLineups().entrySet().iterator();
	    	while(allLineups.hasNext()){
	    		Lineup l = allLineups.next().getValue();
	    		if(Arrays.hashCode(l.getAthleteIDs()) == Arrays.hashCode(newlineup.getAthleteIDs())){
	    			//lineupsToSave.add(l);
	    			Log.i("saveData,List","newlineup matches existing one");
	    			matchedLineup = l;
	    		}
	    	}
	    	if(matchedLineup == null){
	    		Log.i("saveData,List","adding new lineup to practice, boatname:"+newlineup.getBoatName());
	    		n++;
	    		lineupsToSave.add(newlineup);
	    	}
	    	else lineupsToSave.add(matchedLineup);
	    	matchedLineup = null;
    	}
    	//add new lineups if needed
    	Log.i("saveData",n+" new lineups to save");
    	for (Lineup l: lineupsToSave)
    		currentPractice.addCurrentLineup(l);
    	/*if (n > 0 && fromstr.equals("PickNewPieceActivity"))
    		sharedPref.edit().putBoolean("DATA_SET_CHANGED", true).apply();*/
    	// write practice to file
    	DataSaver.writeObject(currentPractice, getString(R.string.PRACTICE_FILE) + currentPracticeID, this);
    }
    
    private boolean isValidLineup(){
    	boolean valid = true;
    	int n = 0;
    	int numPorts = 0,numStars = 0,numBoth = 0,numOfSeats = 0;
    	for (AthleteListName aln : athleteList) {
    		//TODO
    		if(!aln.isAthlete()) {
    			/*Log.i("isValid","numPorts: "+numPorts);
    	    	Log.i("isValid","numStars: "+numStars);
    	    	Log.i("isValid","numBoth: "+numBoth);
    	    	Log.i("isValid","numOfSeats: "+numOfSeats);*/
    			if(numOfSeats !=0)
    				n++;
    			//Log.i("isValid","lineup num: "+n);
    			if(!possibleLineup(numPorts, numStars, numBoth, numOfSeats)) valid = false;
    			//Log.i("aln if loop","aln title: "+aln.getTitle());
    			numPorts = 0;
    			numStars = 0;
    			numBoth = 0;
    			numOfSeats = aln.getNumOfSeats();
    		}
    		else{
    			//Log.i("aln else loop","aln name: "+aln.getName());
	    		if (aln.getSide()!=null && aln.getSide().equalsIgnoreCase("port")) numPorts++;
	    		if (aln.getSide()!=null && aln.getSide().equalsIgnoreCase("starboard")) numStars++;
	    		if (aln.getSide()!=null && aln.getSide().equalsIgnoreCase("both")) numBoth++;
    			
    		}
    	}
    	/*Log.i("isValid","numPorts: "+numPorts);
    	Log.i("isValid","numStars: "+numStars);
    	Log.i("isValid","numBoth: "+numBoth);
    	Log.i("isValid","numOfSeats: "+numOfSeats);
    	Log.i("isValid","lineup num: "+n);*/
    	if(!possibleLineup(numPorts, numStars, numBoth,numOfSeats)) valid = false;
    	//Log.i("isValid","valid: "+valid);
    	//Log.i("isValid","returning: "+valid);
    	return valid;
    }
    
    private Boolean possibleLineup(int numPorts, int numStars, int numBoth, int numSeats){
    	Boolean possible = true;
    	if(numPorts+numStars+numBoth != numSeats) possible=false;
    	if(numSeats == 1) {
    		//Log.i("possibleLineup","possible: "+possible);
    		return possible;
    	}
    	int lesser = Math.min(numPorts, numStars);
    	int greater = Math.max(numPorts, numStars);
    	if (lesser!=greater){
    		if (lesser + numBoth < greater) possible=false;
    	}
    	//Log.i("possibleLineup","possible: "+possible);
    	return possible;
    }
    
    private LinkedList<Lineup> makeLineups(){
    	LinkedList<Lineup> lineups = new LinkedList<Lineup>();
    	if (athleteList.isEmpty())
    		return lineups;
    	LinkedList<LinkedList<AthleteListName>> separatedAthleteLists = new LinkedList<LinkedList<AthleteListName>>();
    	LinkedList<AthleteListName> ll = null;
    	for (AthleteListName aln : athleteList) {
    		if(!aln.isAthlete()) {
    			//boat name card
    			if (ll!=null){
    				separatedAthleteLists.add(ll);
    			}
    			ll= new LinkedList<AthleteListName>();
    			Log.i("makelineups","adding boat "+aln.getTitle()+" to new lineup");
    			ll.add(aln);
    		}
    		else{
    			//athlete card
    			Log.i("makelineups","adding athlete "+aln.getName()+" to current lineup");
    			ll.add(aln);
    		}
    	}
    	
    	//to catch the end-case
    	separatedAthleteLists.add(ll);
    	
    	for(LinkedList<AthleteListName> llaln : separatedAthleteLists){
    		//Log.i("creating new lineups,List","llaln is empty: "+llaln.isEmpty());
    		//create new lineups
    		/*for(AthleteListName aln : llaln){
    			if (!aln.isAthlete())
    				Log.i("List","aln title:"+aln.getTitle()+" and id:"+aln.getathID());
    			else Log.i("List","aln name:"+aln.getName()+" and id:"+aln.getathID()); 
    		}*/
    		lineups.add(new Lineup(llaln, roster, boatList));
    	}
    	return lineups;
    }
    
    private void fadeSwap(){
    	// fade out instructions view nicely after 5 seconds
		AlphaAnimation alphaAnim = new AlphaAnimation(1.0f,0.0f);
		alphaAnim.setStartOffset(3000);                        // start in 3 seconds
		alphaAnim.setDuration(400);
		alphaAnim.setAnimationListener(new AnimationListener()
		{
			 public void onAnimationEnd(Animation animation)
			 {
			   // make invisible when animation completes, you could also remove the view from the layout
				 instructions.setVisibility(View.INVISIBLE);
				 button_done.setVisibility(View.VISIBLE);
				 
			 }
	
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				//do nothing
				
			}
	
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				// do nothing
				
			}
		});

		instructions.setAnimation(alphaAnim);
    }
    
    private TextView getInstructionsTextView(){
    	return instructions;
    }
    
    private Context getContext(){
    	return this;
    }
    
    private void launchPickNewPiece(){
    	Intent pickNewPieceIntent = new Intent(this, PickNewPieceActivity.class);
		startActivity(pickNewPieceIntent);
    }
    
    private void launchSplashscreen(){
    	SplashscreenActivity.updateSyncTextLastSync();
    	sharedPref.edit().putBoolean("DATA_SET_CHANGED", false).apply();
    	Intent splashscreenIntent = new Intent(this, SplashscreenActivity.class);
    	splashscreenIntent.putExtra(getString(R.string.ACTIVITY_FROM),"changeLineupList");
		startActivity(splashscreenIntent);
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
    	if (getIntent().getExtras() != null)
    		savedInstanceState.putString("FROM_STR", getIntent().getExtras().getString(getString(R.string.ACTIVITY_FROM)));
    	
        
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
