package com.vbh.virtualboathouse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

public class DataSaver {
	// constants for bundling data
	static final String STATE_PIECE_ID = "currentPieceID";
	static final String STATE_PRACTICE_ID = "currentPracticeID";
	static final String STATE_PRACTICE = "practice";
	static final String STATE_PIECE = "piece";
	static final String STATE_ROSTER = "roster";
	static final String STATE_BOAT_ID_ARRAY = "boatIDArray";
	static final String STATE_BOAT_ARRAY    = "boatArray";
	static final String STATE_LINEUPS_ARRAY = "lineupsArray";
	static final String STATE_LINEUP_ID_ARRAY = "lineupIDArray";
	static final String STATE_LINEUPS_CHECKBOXES = "lineupCheckBoxes";
	static final String STATE_BOATS = "boats";
	static final String STATE_LINEUPS = "lineups";
	
	
	public static <E> boolean writeObject(E cu, String filename, Context context) {
		FileOutputStream fos;
		ObjectOutputStream os;
		try {
			fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			Log.i("DataSaver " + filename, "file output open " + (fos != null));
			os = new ObjectOutputStream(fos);
			Log.i("DataSaver " + filename, "object output open " + (os != null));
			os.writeObject(cu);
			Log.i("DataSaver " + filename, "object written - successfully?");
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static <E> E readObject(String filename, Context context) {
		FileInputStream fis;
		ObjectInputStream is;
		E e;
		try {
			fis = context.openFileInput(filename);
			Log.i("DataSaver", "file input open " + (fis != null));
			is = new ObjectInputStream(fis);
			Log.i("DataSaver", "object input open " + (is != null));
			e = (E) is.readObject();
			Log.i("DataSaver", "object is read " + (e != null));
		} catch (Exception e1) {
			return null;
		}
		return e;
	}
	public static <E> boolean writeObjectArray(E[] e, String filename, Context context) {
		FileOutputStream fos;
		ObjectOutputStream os;
		try {
			fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			Log.i("DataSaver " + filename, "file output open " + (fos != null));
			os = new ObjectOutputStream(fos);
			Log.i("DataSaver " + filename, "object output open " + (os != null));
			os.writeObject(e);
			Log.i("DataSaver " + filename, "object written - successfully?");
			os.close();
		} catch (Exception e1) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static <E> E[] readObjectArray(String filename, Context context) {
		FileInputStream fis;
		ObjectInputStream is;
		E[] e;
		try {
			fis = context.openFileInput(filename);
			Log.i("DataSaver", "file input open " + (fis != null));
			is = new ObjectInputStream(fis);
			Log.i("DataSaver", "object input open " + (is != null));
			e = (E[]) is.readObject();
			Log.i("DataSaver", "object is read " + (e != null));
		} catch (Exception e1) {
			return null;
		}
		return e;
	}
}
