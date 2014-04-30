package com.vbh.virtualboathouse;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vbh.virtualboathouse.StableArrayAdapter.RowType;

public class AthleteListName implements MyListItem {
    private final String         name;
    private final String         side;
    private final String		 title;
    private final Integer		 athID;
    private final Integer		 boatID;
    private final Integer 		 numOfSeats;
    private final String		 position;
    
 
    public AthleteListName(Integer athID, String title, Integer boatID, Integer numOfSeats, String position) {
        this.name = null;
        this.side = null;
        this.athID = athID;
        this.title = title;
        this.boatID = boatID;
        this.numOfSeats = numOfSeats;
        this.position = position;
    }
    
    public AthleteListName(String name, String side, Integer athID){
    	this.name = name;
    	this.side = side;
    	this.athID = athID;
    	this.title = null;
    	this.boatID = null;
    	this.numOfSeats = null;
    	this.position = null;
    }

	public String getName(){
    	return name;
    }
    
    public Integer getNumOfSeats(){
    	return numOfSeats;
    }
    
    public Integer getathID(){
    	return athID;
    }
    
    public Integer getboatID(){
    	return boatID;
    }
    
    public Boolean isAthlete(){
    	return (name != null);
    }
    
    public String getSide(){
    	return side;
    }
    public String getTitle(){
    	return title;
    }
    
    public String getPosition(){
    	return position;
    }
 
    @Override
    public int getViewType() {
        if(isAthlete()) return 0;
        else return 1;
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null 
        		|| convertView.findViewById(R.id.list_content1)==null
        		|| convertView.findViewById(R.id.header)==null) {
        	if (isAthlete()){
        		convertView = (View) inflater.inflate(R.layout.athlete_list_view, null);
        		
        	}
        	else convertView = (View) inflater.inflate(R.layout.boat_name_view, null);
            //TODO?
        }
        
        if(isAthlete()){
	        TextView textname = (TextView) convertView.findViewById(R.id.list_content1);
	        TextView textside = (TextView) convertView.findViewById(R.id.list_content2);
	        textname.setText(name);
	        textside.setText(side);
	        textside.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_draggable, 0);
        }
        else{
        	TextView textheader = (TextView) convertView.findViewById(R.id.header);
        	textheader.setText(title);
        }
 
        return convertView;
    }
 
}