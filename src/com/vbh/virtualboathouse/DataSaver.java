package com.vbh.virtualboathouse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class DataSaver {
	
	
	public static <E> boolean writeObject(E cu, String filename, Context context) {
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

	@SuppressWarnings("unchecked")
	public static <E> E readObject(String filename, Context context) {
		FileInputStream fis;
		ObjectInputStream is;
		E e;
		try {
			fis = context.openFileInput(filename);
			is = new ObjectInputStream(fis);
			e = (E) is.readObject();
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
			os = new ObjectOutputStream(fos);
			os.writeObject(e);
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
			is = new ObjectInputStream(fis);
			e = (E[]) is.readObject();
		} catch (Exception e1) {
			return null;
		}
		return e;
	}
}
