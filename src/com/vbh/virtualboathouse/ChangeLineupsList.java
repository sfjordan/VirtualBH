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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        context = this;
		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
			getData();
			
		}
		//roster.getAthlete(athleteID)
        
        
        
        
        LayoutInflater inflater = LayoutInflater.from(this);
        athleteList = buildList(new ArrayList<AthleteListName>());
        adapter = new StableArrayAdapter(this, inflater, athleteList);
        DynamicListView listView = (DynamicListView) findViewById(R.id.listview);
        

        listView.setList(athleteList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        
        Button button_done = (Button) findViewById(R.id.button_done);
        button_done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				if (v==findViewById(R.id.button_done)){
					if (isValidLineup()){
						saveData();
						//continue to wherever you need to go
						for (int i = 0; i<athleteList.size(); i++){
							AthleteListName name = athleteList.get(i);
							if (name.isAthlete()) System.out.println(name.getName());
						}
					}
					else {
						Log.i("changelineupslist","invalid lineup");
			        	
			        	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			        	    @Override
			        	    public void onClick(DialogInterface dialog, int which) {
			        	        switch (which){
			        	        case DialogInterface.BUTTON_POSITIVE:
			        	            //Yes button clicked
			        	        	saveData();
			        	        	//continue to wherever you need to go
									for (int i = 0; i<athleteList.size(); i++){
										AthleteListName name = athleteList.get(i);
										if (name.isAthlete()) System.out.println(name.getName());
									}
			        	        	
			        	            break;

			        	        case DialogInterface.BUTTON_NEGATIVE:
			        	            //No button clicked
			        	            break;
			        	        }
			        	    }
			        	};

			        	AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			        	builder.setMessage(R.string.invalid_lineup_error_message).setPositiveButton("Yes", dialogClickListener)
			        	    .setNegativeButton("No", dialogClickListener).show();
					}
				}
			}
		});
    }
    
    private void getData(){
    	// get the practice ID
		SharedPreferences sharedPref = context.getSharedPreferences(
		        getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
		currentPracticeID = sharedPref.getInt(getString(R.string.CURRENT_PRACTICE_ID), 8);
		// currently, just get the most recent practice
		lm = DataSaver.readObjectArray(getString(R.string.RECENT_LINEUP_FILE), this);
		Log.i("changelineuplist", "lm is currently null: " + (lm==null));
		DataRetriever dr = new DataRetriever(this);
		plm = DataSaver.readObjectArray(dr.RECENT_PRACTICE_DATA_FILENAME + currentPracticeID , this);
		System.out.println("plm is null: "+(plm==null));
		this.roster = DataSaver.readObject(getString(R.string.ROSTER_FILE), this);
		Log.i("changelineuplist", "roster is currently null: " + (roster==null));
		this.boatList = DataSaver.readObject(getString(R.string.BOATS_FILE), this);
		Log.i("CrewSelector", "boatList is currently null: " + (boatList==null));
	
		lineups = new HashMap<Integer, Lineup>(lm.length);
		lineupNames = new String[lm.length];
		lineupIDs = new int[lm.length];
    }
    
    private ArrayList<AthleteListName> buildList(ArrayList<AthleteListName> athleteList){
    	numLineups = 0;
		for(LineupModel l : lm){
			Lineup lineup = new Lineup(l, roster, boatList);
			//lineups.put(l.getLineupID(), l);
			//set header:
			if (lineup.getCoxswainName() == null)
				athleteList.add(new AthleteListName(null,null,null,lineup.getBoatName(),lineup.getNumOfSeats()));
			else athleteList.add(new AthleteListName(null,null,null,lineup.getCoxswainName(),lineup.getNumOfSeats()));
			int[] athleteIDs = lineup.getAthleteIDs();
			//now to flip the order:
			Stack<Integer> IDs = new Stack<Integer>();
			for(int a : athleteIDs){
				IDs.push(a);
			}
			while(!IDs.isEmpty()){
				int id = IDs.pop();
				Athlete ath = roster.getAthlete(id);
				athleteList.add(new AthleteListName(ath.getFirstInitLastName(),ath.getSide(),id,null,null));
			}
			numLineups++;
		}
    	return athleteList;    	
    }
    
    private void saveData(){
    	
    }
    
    private boolean isValidLineup(){
    	boolean valid = true;
    	int n = 0;
    	int numPorts = 0,numStars = 0,numBoth = 0,numOfSeats = 0;
    	for (AthleteListName aln : athleteList) {
    		if(!aln.isAthlete()) {
    			Log.i("isValid","numPorts: "+numPorts);
    	    	Log.i("isValid","numStars: "+numStars);
    	    	Log.i("isValid","numBoth: "+numBoth);
    	    	Log.i("isValid","numOfSeats: "+numOfSeats);
    			if(numOfSeats !=0)n++;
    			Log.i("isValid","lineup num: "+n);
    			if(!possibleLineup(numPorts, numStars, numBoth, numOfSeats)) valid = false;
    			Log.i("aln if loop","aln title: "+aln.getTitle());
    			numPorts = 0;
    			numStars = 0;
    			numBoth = 0;
    			numOfSeats = aln.getNumOfSeats();
    		}
    		else{
    			Log.i("aln else loop","aln name: "+aln.getName());
	    		if (aln.getSide()!=null && aln.getSide().equalsIgnoreCase("port")) numPorts++;
	    		if (aln.getSide()!=null && aln.getSide().equalsIgnoreCase("starboard")) numStars++;
	    		if (aln.getSide()!=null && aln.getSide().equalsIgnoreCase("both")) numBoth++;
    			
    		}
    	}
    	Log.i("isValid","numPorts: "+numPorts);
    	Log.i("isValid","numStars: "+numStars);
    	Log.i("isValid","numBoth: "+numBoth);
    	Log.i("isValid","numOfSeats: "+numOfSeats);
    	Log.i("isValid","lineup num: "+n);
    	if(!possibleLineup(numPorts, numStars, numBoth,numOfSeats)) valid = false;
    	Log.i("isValid","valid: "+valid);
    	Log.i("isValid","returning: "+valid);
    	return valid;
    }
    
    private Boolean possibleLineup(int numPorts, int numStars, int numBoth, int numSeats){
    	Boolean possible = true;
    	if(numPorts+numStars+numBoth != numSeats) possible=false;
    	if(numSeats == 1) {
    		Log.i("possibleLineup","possible: "+possible);
    		return possible;
    	}
    	int lesser = Math.min(numPorts, numStars);
    	int greater = Math.max(numPorts, numStars);
    	if (lesser!=greater){
    		if (lesser + numBoth < greater) possible=false;
    	}
    	Log.i("possibleLineup","possible: "+possible);
    	return possible;
    }
    
    private Context getContext(){
    	return this;
    }
    
    
}
