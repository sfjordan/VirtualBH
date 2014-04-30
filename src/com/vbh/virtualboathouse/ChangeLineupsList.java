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
						
						Iterator<Entry<Long, Lineup>> allLineups = currentPractice.getCurrentLineups().entrySet().iterator();
        		    	while(allLineups.hasNext()){
        		    		allLineups.next().getValue().printLineup();			        		    		
        		    	}
						/*for (int i = 0; i<athleteList.size(); i++){
							AthleteListName name = athleteList.get(i);
							if (name.isAthlete()) System.out.println(name.getName());
						}*/
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
			        	        	Iterator<Entry<Long, Lineup>> allLineups = currentPractice.getCurrentLineups().entrySet().iterator();
			        		    	while(allLineups.hasNext()){
			        		    		allLineups.next().getValue().printLineup();			        		    		
			        		    	}
			        	        	//continue to wherever you need to go
									/*for (int i = 0; i<athleteList.size(); i++){
										AthleteListName name = athleteList.get(i);
										if (name.isAthlete()) System.out.println(name.getName());
									}*/
			        	        	
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
		// get the current practice from a file
		currentPractice = DataSaver.readObject(getString(R.string.PRACTICE_FILE) + currentPracticeID, context);
		// get the roster and boatlist
		boatList = DataSaver.readObject(context.getString(R.string.BOATS_FILE), context);
		// TODO check for null
		Log.i("changelineuplist", "boatList is currently null: " + (boatList==null));
		roster = DataSaver.readObject(context.getString(R.string.ROSTER_FILE), context);
		// TODO check for null
		Log.i("changelineuplist", "roster is currently null: " + (roster==null));
		// currently, just get the most recent practice
		lm = DataSaver.readObjectArray(getString(R.string.RECENT_LINEUP_FILE), this);
		Log.i("changelineuplist", "lm is currently null: " + (lm==null));
		DataRetriever dr = new DataRetriever(this);
		//plm = DataSaver.readObjectArray(dr.RECENT_PRACTICE_DATA_FILENAME + currentPracticeID , this);
		//System.out.println("plm is null: "+(plm==null));
	
		lineups = new HashMap<Integer, Lineup>(lm.length);
		lineupNames = new String[lm.length];
		lineupIDs = new int[lm.length];
    }
    
    private ArrayList<AthleteListName> buildList(ArrayList<AthleteListName> athleteList){
    	numLineups = 0;
		for(LineupModel l : lm){
			Lineup lineup = new Lineup(l, roster, boatList);
			lineup.printLineup();
			//lineups.put(l.getLineupID(), l);
			//set header:
			//Log.i("buildlist","Cox name: "+lineup.getCoxswainName()+" and id: "+lineup.getCoxwainID());
			if (lineup.getCoxswainName() == null)
				athleteList.add(new AthleteListName(null, lineup.getBoatName(),lineup.getBoatID(),lineup.getNumOfSeats(),lineup.getPosition()));
			else athleteList.add(new AthleteListName(lineup.getCoxswainID(),lineup.getCoxswainName(),lineup.getBoatID(),lineup.getNumOfSeats(),lineup.getPosition()));
			int[] athleteIDs = lineup.getAthleteIDs();
			for(int a: athleteIDs){
				Athlete ath = roster.getAthlete(a);
				athleteList.add(new AthleteListName(ath.getFirstInitLastName(),ath.getSide(),a));
			}
			//now to flip the order:
			/*Stack<Integer> IDs = new Stack<Integer>();
			for(int a : athleteIDs){
				IDs.push(a);
			}
			while(!IDs.isEmpty()){
				int id = IDs.pop();
				Athlete ath = roster.getAthlete(id);
				athleteList.add(new AthleteListName(ath.getFirstInitLastName(),ath.getSide(),id,null,null,null));
			}*/
			numLineups++;
		}
		/*athleteList.add(new AthleteListName(null,null,null,"boat",null,8));
		athleteList.add(new AthleteListName("S.Jordan", "port",8,null,null,null));
		athleteList.add(new AthleteListName("M.drabick", "starboard",8,null,null,null));
		athleteList.add(new AthleteListName("E.Walker", "port",8,null,null,null));
		athleteList.add(new AthleteListName("bob", "starboard",8,null,null,null));
		
		athleteList.add(new AthleteListName(null,null,null,"boat 2",null,8));
		athleteList.add(new AthleteListName("frank", "port",8,null,null,null));
		athleteList.add(new AthleteListName("john", "starboard",8,null,null,null));
		athleteList.add(new AthleteListName("jack", "port",8,null,null,null));
		athleteList.add(new AthleteListName("smith", "starboard",8,null,null,null));*/
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
    	}
    	//add new lineups if needed
    	Log.i("saveData",n+" new lineups to save");
    	for (Lineup l: lineupsToSave)
    		currentPractice.addCurrentLineup(l);
    }
    
    private boolean isValidLineup(){
    	boolean valid = true;
    	int n = 0;
    	int numPorts = 0,numStars = 0,numBoth = 0,numOfSeats = 0;
    	for (AthleteListName aln : athleteList) {
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
    
    private Context getContext(){
    	return this;
    }
    
    
}
