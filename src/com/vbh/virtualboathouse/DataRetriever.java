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
	
	private String ATHLETE_URL = "https://cos333.herokuapp.com/json/athletes/";
	private String BOATS_URL   = "https://cos333.herokuapp.com/json/boats/";
	private String PRACTICE_URL = "https://cos333.herokuapp.com/json/practices/";
	private String LINEUP_URL   = "/lineups/";
	
	private String ATHLETE_DATA_FILENAME  = "athleteData";
	private String BOAT_DATA_FILENAME     = "boatData";
	private String PRACTICE_DATA_FILENAME = "practiceData";
	private String LINEUP_DATA_FILENAME   = "practiceLineupData";
	private String GENERIC_DATA_FILENAME  = "genericData";
	
	private int currentData = 0;
	private final int ATHLETE         = 1;
	private final int BOATS           = 2;
	private final int PRACTICE        = 3;
	private final int PRACTICE_LINEUP = 4;
	
	private Context context; 
	
	private AthleteModel[] am;
	private ErrorModel em;
	private BoatModel[] bm;
	private PracticeModel[] pm;
	private PracticeLineupsModel[] plm;

	
	public AthleteModel[] getAthleteModel() {
		return am;
	}
	
	
	public PracticeModel[] getPracticeModel() {
		return pm;
	}
	
	public BoatModel[] getBoatModel() {
		return bm;
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
    	FileOutputStream fos;
		ObjectOutputStream os;
		try {
			switch(currentData) {
    			case ATHLETE:  fos = context.openFileOutput(ATHLETE_DATA_FILENAME, Context.MODE_PRIVATE);
    			case BOATS:    fos = context.openFileOutput(BOAT_DATA_FILENAME, Context.MODE_PRIVATE);
    			case PRACTICE: fos = context.openFileOutput(PRACTICE_DATA_FILENAME, Context.MODE_PRIVATE);
    			case PRACTICE_LINEUP: fos = context.openFileOutput(LINEUP_DATA_FILENAME, Context.MODE_PRIVATE);
    			default: fos = context.openFileOutput(GENERIC_DATA_FILENAME, Context.MODE_PRIVATE);
			}	
			os = new ObjectOutputStream(fos);
	    	switch(currentData) {
	    		case ATHLETE:  os.writeObject(this.am);
	    		case BOATS:    os.writeObject(this.bm);
	    		case PRACTICE: os.writeObject(this.pm);
	    		case PRACTICE_LINEUP: os.writeObject(this.plm);
	    	}
			os.close();
		} catch (Exception e) {
			return false;
		}
    	
    	return true;
    }
    
    public void getAthletes() {
    	currentData = ATHLETE;
    	this.execute(ATHLETE_URL);	
    }
    
    public void getBoats() {
    	currentData = BOATS;
    	this.execute(BOATS_URL);	
    }
    
    public void getPractices() {
    	currentData = PRACTICE;
    	this.execute(PRACTICE_URL);	
    }
    public void getPractice(int practiceID) {
    	currentData = PRACTICE_LINEUP;
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
	private void print_content(HttpsURLConnection con){
		if(con!=null){
			try {
		 
			   System.out.println("****** Content of the URL ********");			
			   BufferedReader br = 
				new BufferedReader(
					new InputStreamReader(con.getInputStream()));
		 
			   String input;
		 
			   while ((input = br.readLine()) != null){
			      System.out.println(input);
			   }
			   br.close();
		 
			} 
			catch (IOException e) {
			   e.printStackTrace();
			}
	    }
	 
	  }
	private void print_https_cert(HttpsURLConnection con){	 
	    if(con!=null){
	      try {
	    	
	    	System.out.println("****** cert info for URL ********");
			System.out.println("Response Code : " + con.getResponseCode());
			System.out.println("Cipher Suite : " + con.getCipherSuite());
			System.out.println("\n");
		 
			Certificate[] certs = con.getServerCertificates();
			for(Certificate cert : certs){
			   System.out.println("Cert Type : " + cert.getType());
			   System.out.println("Cert Hash Code : " + cert.hashCode());
			   System.out.println("Cert Public Key Algorithm : " 
		                                    + cert.getPublicKey().getAlgorithm());
			   System.out.println("Cert Public Key Format : " 
		                                    + cert.getPublicKey().getFormat());
			   System.out.println("\n");
			}
		 
	      }
      	catch (SSLPeerUnverifiedException e) {
			e.printStackTrace();
		}
      	catch (IOException e){
			e.printStackTrace();
		}
	 
    }
	 
   }

	

}
