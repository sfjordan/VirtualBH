package com.vbh.virtualboathouse;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vbh.virtualboathouse.StableArrayAdapter.RowType;

public class AthleteListName implements MyListItem {
    private final String         str1;
    private final String         str2;
    private final String		 str3;
 
    public AthleteListName(String text1, String text2, String text3) {
        this.str1 = text1;
        this.str2 = text2;
        this.str3 = text3;
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
        
        TextView text1 = (TextView) convertView.findViewById(R.id.list_content1);
        TextView text2 = (TextView) convertView.findViewById(R.id.list_content2);
        TextView textheader = (TextView) convertView.findViewById(R.id.header);
        text1.setText(str1);
        text2.setText(str2);
        if (str3 != null){
        	textheader.setText(str3);
        }
        else text2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_draggable, 0);
 
        return convertView;
    }
 
}