package com.vbh.virtualboathouse;

import android.view.LayoutInflater;
import android.view.View;


public interface MyListItem {
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}