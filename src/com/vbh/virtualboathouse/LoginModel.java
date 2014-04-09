package com.vbh.virtualboathouse;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class LoginModel implements Serializable  {

	/**
	 * Constant for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("api_key")
	private String apiKey;
	
	public String getAPIKey() {
		return apiKey;
	}
}
