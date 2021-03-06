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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

public class StableArrayAdapter extends ArrayAdapter<AthleteListName> {

    final int INVALID_ID = -1;

    HashMap<AthleteListName, Integer> mIdMap = new HashMap<AthleteListName, Integer>();
    private LayoutInflater inflater;

    public StableArrayAdapter(Context context, LayoutInflater inflater, List<AthleteListName> objects) {
        super(context, 0, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
        this.inflater = inflater;
    }
    
    public enum RowType {
        // Here we have two items types, you can have as many as you like though
        LIST_ITEM, HEADER_ITEM
    }
    @Override
    public int getViewTypeCount() {
        // Get the number of items in the enum
        return RowType.values().length;
 
    }
    
    @Override
    public int getItemViewType(int position) {
        // Use getViewType from the Item interface
        return getItem(position).getViewType();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Use getView from the Item interface
        return getItem(position).getView(inflater, convertView);
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        MyListItem item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
