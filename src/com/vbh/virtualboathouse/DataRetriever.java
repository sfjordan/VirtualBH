package com.vbh.virtualboathouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class DataRetriever extends AsyncTask<String, Void, ArrayList<String>>{
 
    public DataRetriever() {
 
    }
    
    protected ArrayList<String> doInBackground(String... urls) {
    	System.out.println("in background");
        ArrayList<String> data = new ArrayList<String>();
     
        try {
            URL url = new URL(urls[0]);
            HttpsURLConnection urlConnection = 
                (HttpsURLConnection) url.openConnection();
            
            //dumpl all cert info
   	     	print_https_cert(urlConnection);
            //dump all the content
   	     	print_content(urlConnection);
   	     	
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
                        // gets the server json data
            BufferedReader bufferedReader = 
                new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
            String next;
            while ((next = bufferedReader.readLine()) != null){
            	System.out.println("next line = "+next);
                JSONArray ja = new JSONArray(next);
 
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    data.add(jo.getString("text"));
                }
            }
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return data;
	}
    
    public void getAthletes() {
    	this.execute("https://cos333.herokuapp.com/json/athletes/");	
    }
    
    public void getBoats() {
    	this.execute("https://cos333.herokuapp.com/json/boats/");	
    }
    
    public static boolean isNetworkConnected(Context c) {
        ConnectivityManager conManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        return ( netInfo != null && netInfo.isConnected() );
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
