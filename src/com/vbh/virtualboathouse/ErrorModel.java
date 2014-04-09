package com.vbh.virtualboathouse;

import java.io.Serializable;

public class ErrorModel implements Serializable {

	/**
	 * Constant for serialization
	 */
	private static final long serialVersionUID = 2L;

	private String[] errorMessages;
	
	public ErrorModel(String input) {
		
	}
	public String[] getErrors() {
		return errorMessages;
	}
}
