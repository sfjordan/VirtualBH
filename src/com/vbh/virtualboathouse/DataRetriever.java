package com.vbh.virtualboathouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.widget.CheckBox;

public class DataRetriever extends AsyncTask<String, Void, ArrayList<String>>{
	
	private final String ATHLETE_URL = "https://cos333.herokuapp.com/json/athletes/";
	private final String BOATS_URL   = "https://cos333.herokuapp.com/json/boats/";
	private final String PRACTICE_URL = "https://cos333.herokuapp.com/json/practices/";
	private final String SINGLE_PRACTICE_URL = "https://cos333.herokuapp.com/json/practice/";
	private final String LINEUP_URL   = "/lineups/";
	private final String RECENT_PRACTICE_URL = "https://cos333.herokuapp.com/json/practice/recent";
	private final String RECENT_LINEUPS_URL = "https://cos333.herokuapp.com/json/lineups/recent";
	private final String PIECE_URL = "https://cos333.herokuapp.com/json/pieces/add";
	private final String SAVE_LINEUP_URL = "https://cos333.herokuapp.com/json/lineups/add";
	private final String SAVE_RESULT_URL = "https://cos333.herokuapp.com/json/results/add";
	
	public final String ATHLETE_DATA_FILENAME  = "athleteModelData";
	public final String BOAT_DATA_FILENAME     = "boatModelData";
	public final String PRACTICE_DATA_FILENAME = "practiceModelData";
	public final String LINEUP_DATA_FILENAME   = "practiceLineupModelData";
	public final String RECENT_PRACTICE_DATA_FILENAME = "recentPracticeModelData";
	public final String GENERIC_DATA_FILENAME  = "genericModelData";
	public final String RECENT_LINEUPS_DATA_FILENAME = "recentLineupModelData";
	
	private int currentData = 0;
	private final int ATHLETE         = 1;
	private final int BOATS           = 2;
	private final int PRACTICE        = 3;
	private final int PRACTICE_LINEUP = 4;
	private final int RECENT_PRACTICE = 5;
	private final int ATHLETES_BOATS  = 6;
	
	private Context context; 
	
	private int currentPracticeID;
	private String username;
	private String apiKey;
	
	private AthleteModel[] am;
	private ErrorModel em;
	private BoatModel[] bm;
	private PracticeModel[] pm;
	private PracticeLineupsModel[] plm;
	private RecentModel rm;
	private LineupModel[] lm;
	
	private Map<Integer, Boat> boatList;
	private Roster roster;
	private Practice practice;

	
	public AthleteModel[] getAthleteModel() {
		return am;
	}
	
	public LineupModel[]  getRecentLineupModel() {
		return lm;
	}
	
	public PracticeModel[] getPracticeModel() {
		return pm;
	}
	
	public BoatModel[] getBoatModel() {
		return bm;
	}
	
	public RecentModel getRecentModel() {
		return rm;
	}
	
	public PracticeLineupsModel[] getPracticeLineupsModel() {
		return plm;
	}
	
	public ErrorModel getErrorModel() {
		return em;
	}
	
    public DataRetriever(Context context) {
    	this.context = context;
    	
    }
    @Override
    protected ArrayList<String> doInBackground(String... urls) {
    	System.out.println("in background");
        ArrayList<String> data = new ArrayList<String>();
        
        if (!isNetworkConnected(context)) {
        	return null;
        }
        // get the user info from shared preferences
        SharedPreferences sp = context.getSharedPreferences(CurrentUser.USER_DATA_PREFS, Context.MODE_PRIVATE);
        username = sp.getString(CurrentUser.USERNAME, "admin");
        apiKey = sp.getString(CurrentUser.API_KEY, "fail");
        if (apiKey == "fail") {
        	return null;
        }
        // Create a new HttpClient and Post Header
        String dataReturned = performHTTPRequest(urls[0]);
	    if (dataReturned == null) return null;
	    data.add(dataReturned);
	   
		if (currentData == ATHLETES_BOATS) {
		    dataReturned = performHTTPRequest(BOATS_URL);
		    if (dataReturned == null) return null;
		    Log.i("DataRetriever", "boatModel = "+ dataReturned);
		    // TODO remove
		    data.add(dataReturned);
		    dataReturned = performHTTPRequest(RECENT_PRACTICE_URL);
		    if (dataReturned == null) return null;
		    data.add(dataReturned);
		    Gson gson = new Gson();
		    Log.i("DataRetriever", "recentModel = "+ data.get(2));
		    rm = gson.fromJson(data.get(2), RecentModel.class);
		    currentPracticeID = rm.getPracticeID();
		    dataReturned = performHTTPRequest(RECENT_LINEUPS_URL);
		    data.add(dataReturned);
//		    dataReturned = performHTTPRequest(SINGLE_PRACTICE_URL + rm.getPracticeID() + LINEUP_URL);
//		    if (dataReturned == null) return null;
//		    data.add(dataReturned);
		    Log.i("DataRetriever", "all data successfully pulled ");
		    // save data
		    // TODO check if there is data to upload from a file (aka support multiple practices)
		    SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
		    int uploadID = sharedPref.getInt(context.getString(R.string.PRACTICE_TO_UPLOAD_ID), -1);
		    if (uploadID != -1) {
		    	Practice uploadPractice = DataSaver.readObject(context.getString(R.string.PRACTICE_FILE) + uploadID, context);
		    	Map<Long, Piece> pieces = uploadPractice.getPieces();
		    	for (Long pID : pieces.keySet()) {
		    		Piece p = pieces.get(pID);
		    		PieceModel pm = new PieceModel(uploadID, p.getPieceID(), p.getDateString());
		    		String pieceStr = gson.toJson(pm);
		    		ReturnedPieceModel rpm = gson.fromJson(performHTTPRequest(PIECE_URL, "piece", pieceStr), ReturnedPieceModel.class);
		    		int webPieceID = rpm.getPieceID();
		    		// save the lineups and results
		    		for (Long lineupID : p.getLineups()) {
		    			Lineup l = uploadPractice.getLineup(lineupID);
		    			ReturnedLineupModel rlm = new ReturnedLineupModel(l.getAllAthleteIDs(), l.getPosition(), l.getBoatID(), webPieceID);
		    			String lineupString = gson.toJson(rlm);
		    			performHTTPRequest(SAVE_LINEUP_URL, "lineup", lineupString);
		    			// TODO logic for a countdown piece
		    			
		    			ReturnedResultsModel rrm = new ReturnedResultsModel(l.getAllAthleteIDs(), p.getDistance(), p.getDateString(), p.getTime(lineupID), webPieceID); 
		    			String resultString = gson.toJson(rrm);
		    			performHTTPRequest(SAVE_RESULT_URL, "results", resultString);
		    		}
		    	}
		    	sharedPref.edit().remove(context.getString(R.string.PRACTICE_TO_UPLOAD_ID)).apply();
		    }
		}
		
	    return data;
	}
    
    private String performHTTPRequest(String url) {
    	HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("api_key", apiKey));
        try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		HttpResponse response;
		try {
	        // Execute HTTP Get Request
			response = httpclient.execute(httpPost);	
		} catch (ClientProtocolException e) {
	       return null;
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		try {
			return inputStreamToString(response.getEntity().getContent());
		} catch (IllegalStateException e1) {
			return null;
		} catch (IOException e1) {
			return null; }
    }
    private String performHTTPRequest(String url, String dataIdentifier, String dataToPost) {
    	HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("api_key", apiKey));
        nameValuePairs.add(new BasicNameValuePair(dataIdentifier, dataToPost));
        try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		HttpResponse response;
		try {
	        // Execute HTTP Get Request
			response = httpclient.execute(httpPost);	
		} catch (ClientProtocolException e) {
	       return null;
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		try {
			if(response == null) return null;
			return inputStreamToString(response.getEntity().getContent());
		} catch (IllegalStateException e1) {
			return null;
		} catch (IOException e1) {
			return null; }
    }
    @Override
    protected void onPostExecute(ArrayList<String> data) {
    	boolean seenError = false;
    	Gson gson = new Gson(); 
    	Log.i("DataRetriever", "entered onPostExecute ");
    	if (data == null) {
    		seenError = true;
    		// display could not get data 
    		 Log.e("DataRetriever", "data is null ");
    	}
	    try {
	    	Log.i("DataRetriever", "entered try statement " + currentData);
	    	SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.SHARED_PREFS_FILE), Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
	    	switch(currentData) {
	    		case ATHLETE: am = gson.fromJson(data.get(0), AthleteModel[].class); // deserializes jsonResponse into athlete models
	    		case BOATS: bm = gson.fromJson(data.get(0), BoatModel[].class); // deserializes jsonResponse into boat models
	    		case PRACTICE: pm = gson.fromJson(data.get(0), PracticeModel[].class); // deserializes jsonResponse into practice models
	    		case PRACTICE_LINEUP: plm = gson.fromJson(data.get(0), PracticeLineupsModel[].class); // deserializes jsonResponse into lineups
	    		case RECENT_PRACTICE: 
	    			rm = gson.fromJson(data.get(0), RecentModel.class); // deserializes jsonResponse into id - should only have one value
	    			editor.putInt(context.getString(R.string.CURRENT_PRACTICE_ID), currentPracticeID);
	    			editor.apply();
	    			this.getPractice(rm.getPracticeID());
	    			currentData = RECENT_PRACTICE;
	    		case ATHLETES_BOATS:  
	    			Log.i("DataRetriever", "athleteModel = " + data.get(0));
	    			am = gson.fromJson(data.get(0), AthleteModel[].class);
	    			Log.i("DataRetriever", "athlete data successfully converted from JSON ");
	    			Log.i("DataRetriever", "boatModel = " + data.get(1));
	    			Log.i("DataRetriever", "LineupsModel = " + data.get(3));
	    			bm = gson.fromJson(data.get(1), BoatModel[].class);
	    			Log.i("DataRetriever", "boat data successfully converted from JSON ");
	    			editor.putInt(context.getString(R.string.CURRENT_PRACTICE_ID), currentPracticeID);
	    			editor.apply();
	    			Log.i("DataRetriever", "recent practice ID put in sharedPrefs");
	    			lm = gson.fromJson(data.get(3), LineupModel[].class);
	    			//plm = gson.fromJson(data.get(3), PracticeLineupsModel[].class);
	    			Log.i("DataRetriever", "recent lineup data successfully converted from JSON ");
	    			buildPractice(am, bm, lm);
	    	}
	    	saveData();
	    } catch (Exception e) {
	    	try {
	    		 Log.e("DataRetriever", "data is an error message ");
	    		 this.em = gson.fromJson(data.get(0), ErrorModel.class); // deserializes jsonResponse into error message 
				 seenError = true;
				 Log.e("DataRetriever", "Error: " +  em.getError());
	    	}
			catch (Exception e2) {
				// log and return
				saveData();
				return;
			}
	    }
	    
	    if (seenError) {
	    	// Display error message as a toast and log the problem
	    	
	    }
    }
    
    private void buildPractice(AthleteModel[] am, BoatModel[] bm, LineupModel[] lm) {
    	// build boat list
		boatList = new HashMap<Integer, Boat>(bm.length);
		for (BoatModel boatModel : bm) {
			Boat boat = new Boat(boatModel);
			boatList.put(boat.getBoatID(), boat);
		}
		// build roster
		roster = new Roster(am);
		// build current practice
		practice = new Practice(currentPracticeID);
		for (LineupModel lineupModel : lm) {
			Lineup l = new Lineup(lineupModel, roster, boatList);
			practice.addCurrentLineup(l);
		}
	}

	@SuppressLint("UseSparseArrays")
	public boolean saveData() {
		boolean success = true;
    	switch(currentData) {
			case ATHLETE:  success = DataSaver.writeObjectArray(this.am, ATHLETE_DATA_FILENAME, context);
			case BOATS:    success = DataSaver.writeObjectArray(this.bm, BOAT_DATA_FILENAME, context);
			case PRACTICE: success = DataSaver.writeObjectArray(this.pm, PRACTICE_DATA_FILENAME, context);
			case PRACTICE_LINEUP: success = DataSaver.writeObjectArray(this.plm, LINEUP_DATA_FILENAME + currentPracticeID, context);
			case RECENT_PRACTICE: success = true;
			case ATHLETES_BOATS: 
	    		// save roster and boat list to files
	         	success = DataSaver.writeObject(boatList, context.getString(R.string.BOATS_FILE), context);
	    		if (!success) return false;
	    		success = DataSaver.writeObject(roster, context.getString(R.string.ROSTER_FILE), context);
	    		if (!success) return false;
	    		// save practice to file
	    		success = DataSaver.writeObject(practice, context.getString(R.string.PRACTICE_FILE) + practice.getPracticeID(), context);
	    		if (!success) return false;
	    	    Log.i("DataRetriever", "all data successfully saved to files ");
		}
    	return success;
    }
    
    public void getAthletes() {
    	currentData = ATHLETE;
    	this.execute(ATHLETE_URL);	
    }
    
    public void getBoats() {
    	currentData = BOATS;
    	this.execute(BOATS_URL);	
    }
    public void getAthletesAndBoats() {
    	currentData = ATHLETES_BOATS;
    	this.execute(ATHLETE_URL);
    }
    
    public void getMostRecentPractice() {
    	currentData = RECENT_PRACTICE;
    	this.execute(RECENT_PRACTICE_URL);
    }
    
    public void uploadPractice() {
    	
    	
    }
    
    public void getPractices() {
    	currentData = PRACTICE;
    	this.execute(PRACTICE_URL);	
    }
    public void getPractice(int practiceID) {
    	currentData = PRACTICE_LINEUP;
    	currentPracticeID = practiceID;
    	this.execute(SINGLE_PRACTICE_URL + practiceID + LINEUP_URL);	
    }
    
    public static boolean isNetworkConnected(Context c) {
        ConnectivityManager conManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        return ( netInfo != null && netInfo.isConnected() );
    }
    
	private String inputStreamToString(InputStream is) {
	    String line = "";
	    StringBuilder total = new StringBuilder();
	    BufferedReader rd;
	    // Wrap a BufferedReader around the InputStream
	    try { 
	        rd = new BufferedReader(new InputStreamReader(is)); 
	    } catch (Exception e){
	    	return null;
	    }
	    
	    // Read response until the end
	    try {
			while ((line = rd.readLine()) != null) { 
			    total.append(line); 
			}
		} catch (IOException e) {
			return null;
		} 
	    return total.toString(); // Return full string
	}

}
