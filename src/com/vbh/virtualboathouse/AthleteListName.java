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
    private final Integer				ID;
    private final Integer 			numOfSeats;
    
 
    public AthleteListName(String name, String side, Integer ID, String title, Integer numOfSeats) {
        this.name = name;
        this.side = side;
        this.ID = ID;
        this.title = title;
        this.numOfSeats = numOfSeats;
    }
    
    
    
    public String getName(){
    	return name;
    }
    
    public Integer getNumOfSeats(){
    	return numOfSeats;
    }
    
    public Integer getID(){
    	return ID;
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