package com.vbh.virtualboathouse;

import java.io.Serializable;

import android.content.Context;

public class CurrentUser implements Serializable {
	/**
	 * Constant for serialization
	 */
	private static final long serialVersionUID = -8125332426893543983L;
	public static final String API_KEY = "apiKey";
	public static final String USERNAME = "username";
	public static final String USER_DATA_FILE = "userData";
	public static final String USER_DATA_PREFS = "UserDataPrefs";
	private final LoginModel lm;
	private final String username;
	private String name;
	
	public CurrentUser(LoginModel lm, String username) {
		this.lm = lm;
		this.username = username;
	}
	
	public String getAPIKey() {
		return lm.getAPIKey();
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getName() {
		return name;
	}

	public static boolean deleteUserDataFile(Context context) {
		return context.deleteFile(USER_DATA_FILE);
	}
}
