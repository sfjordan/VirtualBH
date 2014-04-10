package com.vbh.virtualboathouse;

import java.io.Serializable;

public class CurrentUser implements Serializable {
	/**
	 * Constant for serialization
	 */
	private static final long serialVersionUID = -8125332426893543983L;
	
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
	
}
