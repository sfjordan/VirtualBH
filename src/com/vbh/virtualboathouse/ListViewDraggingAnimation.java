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
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This application creates a listview where the ordering of the data set
 * can be modified in response to user touch events.
 *
 * An item in the listview is selected via a long press event and is then
 * moved around by tracking and following the movement of the user's finger.
 * When the item is released, it animates to its new position within the listview.
 */
public class ListViewDraggingAnimation extends Activity {
	
	String[] names = {"Sam", "Ed", "Brian", "Matt"};
	String[] names2 = {"Bob", "John", "Frank", "Steve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        
        LayoutInflater inflater = LayoutInflater.from(this);
        ArrayList<MyListItem> items = new ArrayList<MyListItem>();
        
        //items.add(new Header("Friday - November 30th 2012"));
        items.add(new AthleteListName("", "", "Boat 1"));
        items.add(new AthleteListName("Sam" , "Port", null));
        items.add(new AthleteListName("Matt", "Starboard",null));
        items.add(new AthleteListName("Ed" , "Port",null));
        items.add(new AthleteListName("Steve","Starboard", null));
         
        //items.add(new Header("Saturday - December 1st 2012"));
        items.add(new AthleteListName("", "", "Boat 2"));
        items.add(new AthleteListName("Frank" , "Port",null));
        items.add(new AthleteListName("John", "Starboard",null));
        items.add(new AthleteListName("Bill" , "Port",null));
        items.add(new AthleteListName("Hafiiiiiz","Starboard",null));

        StableArrayAdapter adapter = new StableArrayAdapter(this, inflater, items);
        DynamicListView listView = (DynamicListView) findViewById(R.id.listview);
        

        listView.setList(items);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        
        /*long id = adapter.getItemId(0);
        System.out.println("id: "+id);
        if(firstCrewName !=null){
        	System.out.println("view text: "+firstCrewName.getText());
        	firstCrewName.setText("SUCCESS");
        }
        else System.out.println("firstCrewName is null");*/
        
        //TODO: add method to change view/make blue in DynamicListView.java?
    }
}
