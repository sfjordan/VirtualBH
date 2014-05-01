package com.vbh.virtualboathouse;

import com.google.gson.annotations.SerializedName;

public class ReturnedNotesModel {

	@SerializedName("type")
	private String type;
	@SerializedName("id")
	private int id;
	@SerializedName("subject")
	private String subject;
	@SerializedName("text")
	private String text;

	
	public ReturnedNotesModel(String type, int pieceID, String subject, String text) {
		this.type    = type;
		this.id   = pieceID;
		this.subject = subject;
		this.text    = text;
	}
	
}
