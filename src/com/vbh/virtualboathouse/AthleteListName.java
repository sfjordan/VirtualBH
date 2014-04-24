package com.vbh.virtualboathouse;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vbh.virtualboathouse.StableArrayAdapter.RowType;

public class AthleteListName implements MyListItem {
    private final String         name;
    private final String         side;
    private final String		 title;
 
    public AthleteListName(String name, String side, String title) {
        this.name = name;
        this.side = side;
        this.title = title;
    }
    
    public String getName(){
    	return name;
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
        return RowType.LIST_ITEM.ordinal();
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = (View) inflater.inflate(R.layout.athlete_list_view, null);
        }
        
        TextView textname = (TextView) convertView.findViewById(R.id.list_content1);
        TextView textside = (TextView) convertView.findViewById(R.id.list_content2);
        TextView textheader = (TextView) convertView.findViewById(R.id.header);
        
        if(isAthlete()){
	        textname.setText(name);
	        textside.setText(side);
	        textheader.setText("");
	        textside.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_draggable, 0);
        }
        else {
        	textname.setText("");
        	textside.setText("");
        	textheader.setText(title);
        	
        }
 
        return convertView;
    }
 
}