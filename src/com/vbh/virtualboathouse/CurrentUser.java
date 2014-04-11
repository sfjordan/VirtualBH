package com.vbh.virtualboathouse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	public boolean writeObject(CurrentUser cu, String filename, Context context) {
		FileOutputStream fos;
		ObjectOutputStream os;
		try {
			fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			os = new ObjectOutputStream(fos);
			os.writeObject(cu);
			os.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static CurrentUser readObject(String filename, Context context) {
		FileInputStream fis;
		ObjectInputStream is;
		CurrentUser cu;
		try {
			fis = context.openFileInput(filename);
			is = new ObjectInputStream(fis);
			cu = (CurrentUser) is.readObject();
		} catch (Exception e) {
			return null;
		}
		return cu;
	}

	public static boolean deleteUserDataFile(Context context) {
		return context.deleteFile(USER_DATA_FILE);
	}
}
