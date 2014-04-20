package com.vbh.virtualboathouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mUsername;
	private String mPassword;
	
	public static boolean successfulLogin = false;

	// UI references.
	private EditText mUsernameView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	// constants
	private int MIN_PASSWORD_LEN = 6;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mUsername = getIntent().getStringExtra(EXTRA_EMAIL);
		mUsernameView = (EditText) findViewById(R.id.email);
		mUsernameView.setText(mUsername);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUsernameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUsername = mUsernameView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < MIN_PASSWORD_LEN) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError(getString(R.string.error_field_required));
			focusView = mUsernameView;
			cancel = true;
		}
//	    else if (!mUsername.contains("@")) {
//			mUsernameView.setError(getString(R.string.error_invalid_email));
//			focusView = mUsernameView;
//			cancel = true;
//		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		private LoginModel lm; 
		private ErrorModel em;
		private boolean seenError = false;
		public static final String API_URL = "https://cos333.herokuapp.com/json/";
		
		public LoginModel getLoginModel() {
			return this.lm;
		}
		
		public ErrorModel getErrorModel() {
			return this.em;
		}
		@Override
		protected Boolean doInBackground(Void... params) {
		    // Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(API_URL + "login/");
		    HttpResponse response;
		    try {
		        // Add username and password
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("username", mUsername));
		        nameValuePairs.add(new BasicNameValuePair("password", mPassword ));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        response = httpclient.execute(httppost);
				
			} catch (ClientProtocolException e) {
		       return false;
			} catch (UnsupportedEncodingException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
		    String jsonResponse;
			try {
				jsonResponse = inputStreamToString(response.getEntity().getContent());
			} catch (IllegalStateException e1) {
				return false;
			} catch (IOException e1) {
				return false; }
		    if (jsonResponse == null) {
		    	return false;
		    }
		    Gson gson = new Gson(); 
		    try {
				 this.lm = gson.fromJson(jsonResponse, LoginModel.class); // deserializes jsonResponse into api key
		    } catch (Exception e) {
		    	try {
		    		 this.em = gson.fromJson(jsonResponse, ErrorModel.class); // deserializes jsonResponse into error message 
					 seenError = true; 
		    	}
				catch (Exception e2) {
					return false;
				}
		    } 
		    
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				
				if (seenError) {
					Log.i("UserAuthTask", "UserAuthTask recieved an error message ");
					mPasswordView.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
				}
				else {
					Log.i("UserAuthTask", "UserAuthTask succeeded on httpPost request " + lm.getAPIKey());
					// save API key in internal storage
					CurrentUser cu = new CurrentUser(lm, mUsername);
					DataSaver.writeObject(cu, CurrentUser.USER_DATA_FILE, getApplicationContext());
					// save the api key and the username for future accesses
					SharedPreferences sp = getSharedPreferences(CurrentUser.USER_DATA_PREFS, Context.MODE_PRIVATE);
					SharedPreferences.Editor spEditor = sp.edit();
					spEditor.putString(CurrentUser.API_KEY, cu.getAPIKey());
					spEditor.putString(CurrentUser.USERNAME, cu.getUsername());
					spEditor.commit();
					// launch the main menu activity 
					Intent splashscreenIntent = new Intent(getApplicationContext(), Splashscreenactivity.class);
					startActivity(splashscreenIntent);
				}
			} else {
				// display toast saying there was a server authentication and invite the user to try again
				Log.i("UserAuthTask", "UserAuthTask failed on httpPost request ");
				
			}
			finish();
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
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
}
