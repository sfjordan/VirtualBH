package com.vbh.virtualboathouse;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.R.string;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class DataRetriever extends AsyncTask<String, Void, ArrayList<String>>{
	
	private final String ATHLETE_URL = "https://cos333.herokuapp.com/json/athletes/";
	private final String BOATS_URL   = "https://cos333.herokuapp.com/json/boats/";
	private final String PRACTICE_URL = "https://cos333.herokuapp.com/json/practices/";
	private final String LINEUP_URL   = "/lineups/";
	private final String RECENT_PRACTICE_URL = "https://cos333.herokuapp.com/json/practice/recent";
	
	public final String ATHLETE_DATA_FILENAME  = "athleteModelData";
	public final String BOAT_DATA_FILENAME     = "boatModelData";
	public final String PRACTICE_DATA_FILENAME = "practiceModelData";
	public final String LINEUP_DATA_FILENAME   = "practiceLineupModelData";
	public final String RECENT_PRACTICE_DATA_FILENAME = "recentPracticeModelData";
	public final String GENERIC_DATA_FILENAME  = "genericModelData";
	
	private int currentData = 0;
	private final int ATHLETE         = 1;
	private final int BOATS           = 2;
	private final int PRACTICE        = 3;
	private final int PRACTICE_LINEUP = 4;
	private final int RECENT_PRACTICE = 5;
	
	private Context context; 
	
	private int currentPracticeID;
	
	private AthleteModel[] am;
	private ErrorModel em;
	private BoatModel[] bm;
	private PracticeModel[] pm;
	private PracticeLineupsModel[] plm;
	private RecentModel rm;

	
	public AthleteModel[] getAthleteModel() {
		return am;
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
        
        // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpGet httpget = new HttpGet(urls[0]);
	    HttpResponse response;
	    try {
	        // Execute HTTP Get Request
	        response = httpclient.execute(httpget);
			
		} catch (ClientProtocolException e) {
	       return null;
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		try {
			data.add(inputStreamToString(response.getEntity().getContent()));
		} catch (IllegalStateException e1) {
			return null;
		} catch (IOException e1) {
			return null; }
	    return data;
	}
    @Override
    protected void onPostExecute(ArrayList<String> data) {
    	boolean seenError = false;
    	Gson gson = new Gson(); 
    	if (data == null) {
    		seenError = true;
    		// display could not get data 
    	}
	    try {
	    	switch(currentData) {
	    		case ATHLETE: am = gson.fromJson(data.get(0), AthleteModel[].class); // deserializes jsonResponse into athlete models
	    		case BOATS: bm = gson.fromJson(data.get(0), BoatModel[].class); // deserializes jsonResponse into boat models
	    		case PRACTICE: pm = gson.fromJson(data.get(0), PracticeModel[].class); // deserializes jsonResponse into practice models
	    		case PRACTICE_LINEUP: plm = gson.fromJson(data.get(0), PracticeLineupsModel[].class); // deserializes jsonResponse into lineups
	    		case RECENT_PRACTICE: rm = gson.fromJson(data.get(0), RecentModel.class); // deserializes jsonResponse into id - should only have one value
	    	}
	    	saveData();
	    } catch (Exception e) {
	    	try {
	    		 this.em = gson.fromJson(data.get(0), ErrorModel.class); // deserializes jsonResponse into error message 
				 seenError = true; 
	    	}
			catch (Exception e2) {
				// log and return
				return;
			}
	    }
	    
	    if (seenError) {
	    	// Display error message as a toast and log the problem
	    	
	    }
    }
    
    public boolean saveData() {
		boolean success = true;
    	switch(currentData) {
			case ATHLETE:  success = DataSaver.writeObjectArray(this.am, ATHLETE_DATA_FILENAME, context);
			case BOATS:    success = DataSaver.writeObjectArray(this.bm, BOAT_DATA_FILENAME, context);
			case PRACTICE: success = DataSaver.writeObjectArray(this.pm, PRACTICE_DATA_FILENAME, context);
			case PRACTICE_LINEUP: success = DataSaver.writeObjectArray(this.plm, LINEUP_DATA_FILENAME + currentPracticeID, context);
			case RECENT_PRACTICE: success = DataSaver.writeObject(this.rm, RECENT_PRACTICE_DATA_FILENAME, context);
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
    
    public void getMostRecentPracticeID() {
    	currentData = RECENT_PRACTICE;
    	this.execute(RECENT_PRACTICE_URL);
    }
    
    public void getPractices() {
    	currentData = PRACTICE;
    	this.execute(PRACTICE_URL);	
    }
    public void getPractice(int practiceID) {
    	currentData = PRACTICE_LINEUP;
    	currentPracticeID = practiceID;
    	this.execute(PRACTICE_URL + practiceID + LINEUP_URL);	
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
