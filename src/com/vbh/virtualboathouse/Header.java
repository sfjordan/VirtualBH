package com.vbh.virtualboathouse;

import com.vbh.virtualboathouse.StableArrayAdapter.RowType;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Header implements MyListItem {
    private final String         name;
 
    public Header(String name) {
        this.name = name;
    }
 
    @Override
    public int getViewType() {
        return RowType.HEADER_ITEM.ordinal();
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            // No views to reuse, need to inflate a new view
            convertView = (View) inflater.inflate(R.layout.boat_name_view, null);
        }
 
        TextView text = (TextView) convertView.findViewById(R.id.boat_name_text);
        text.setText(name);
 
        return convertView;
    }
}