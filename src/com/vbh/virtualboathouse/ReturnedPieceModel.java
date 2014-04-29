package com.vbh.virtualboathouse;

import com.google.gson.annotations.SerializedName;

public class ReturnedPieceModel {
	
	@SerializedName("id")
	private int id;
	
	public int getPieceID() {
		return id;
	}
}
