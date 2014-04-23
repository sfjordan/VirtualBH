package com.vbh.virtualboathouse;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

public class ChangeLineupsActivity extends Activity {
	
	DragSortListView listView;
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_linups);

		/*if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		
		listView = (DragSortListView) findViewById(R.id.listview);
	    String[] names = {"Sam", "Matt", "Ed", "Brian"};
	    ArrayList<String> list = new ArrayList<String>(Arrays.asList(names));
	    adapter = new ArrayAdapter<String>(this,
	            R.layout.simple_list_item_1, list);
	    listView.setAdapter(adapter);
	    listView.setDropListener(onDrop);
	    listView.setRemoveListener(onRemove);

	    DragSortController controller = new DragSortController(listView);
	    controller.setDragHandleId(R.id.imageView1);
	            //controller.setClickRemoveId(R.id.);
	    controller.setRemoveEnabled(false);
	    controller.setSortEnabled(true);
	    controller.setDragInitMode(1);
	            //controller.setRemoveMode(removeMode);

	    listView.setFloatViewManager(controller);
	    listView.setOnTouchListener(controller);
	    listView.setDragEnabled(true);
		
		
	}
	
	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener()
	{
	    @Override
	    public void drop(int from, int to)
	    {
	        if (from != to)
	        {
	            String item = adapter.getItem(from);
	            adapter.remove(item);
	            adapter.insert(item, to);
	        }
	    }
	};

	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener()
	{
	    @Override
	    public void remove(int which)
	    {
	        adapter.remove(adapter.getItem(which));
	    }
	};
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_linups, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	/*public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_change_linups,
					container, false);
			return rootView;
		}
	}*/

}
