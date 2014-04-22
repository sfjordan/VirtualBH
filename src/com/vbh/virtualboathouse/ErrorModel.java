package com.vbh.virtualboathouse;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class ErrorModel implements Serializable {

	/**
	 * Constant for serialization
	 */
	private static final long serialVersionUID = 2L;

	@SerializedName("error")
	private String error;
	

	public String getError() {
		return error;
	}
}
