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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

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
        
        TextView firstCrewName = (TextView) findViewById(R.id.first_crew_name);
        TextView secondCrewName = (TextView) findViewById(R.id.second_crew_name);
        
        firstCrewName.setText("Crew 1");
        firstCrewName.setGravity(Gravity.CENTER);
        secondCrewName.setText("Crew 2");
        secondCrewName.setGravity(Gravity.CENTER);

        ArrayList<String>list = new ArrayList<String>(Arrays.asList(names));
        ArrayList<String>list2 = new ArrayList<String>(Arrays.asList(names2));

        StableArrayAdapter adapter = new StableArrayAdapter(this, R.layout.text_view, list);
        DynamicListView listView = (DynamicListView) findViewById(R.id.listview);
        
        StableArrayAdapter adapter2 = new StableArrayAdapter(this, R.layout.text_view, list2);
        DynamicListView listView2 = (DynamicListView) findViewById(R.id.listview2);

        listView.setList(list);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        listView2.setList(list2);
        listView2.setAdapter(adapter2);
        listView2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        //TODO: add method to change view/make blue in DynamicListView.java?
    }
}
