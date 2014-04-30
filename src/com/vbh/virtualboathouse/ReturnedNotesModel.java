package com.vbh.virtualboathouse;

import com.google.gson.annotations.SerializedName;

public class ReturnedNotesModel {

	@SerializedName("type")
	private String type;
	@SerializedName("piece")
	private int piece;
	@SerializedName("subject")
	private String subject;
	@SerializedName("text")
	private String text;

	
	public ReturnedNotesModel(String type, int pieceID, String subject, String text) {
		this.type    = type;
		this.piece   = pieceID;
		this.subject = subject;
		this.text    = text;
	}
	
}
