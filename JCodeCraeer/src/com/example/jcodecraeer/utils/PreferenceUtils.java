package com.example.jcodecraeer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtils {
	public static final String SHARE_NAME = "JCODECRAEER_PRJ";
	public static final String IS_FIRST_IN = "is_first_in";
	public SharedPreferences prefs;
	
	public PreferenceUtils(){
		
	}
	
	public boolean IsFisrtIn(Context context,String key){
		boolean result = true;
		prefs = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
		result = prefs.getBoolean(key, true);
		return result;
	}
	
	public void setFirstIn(Context context,String key,boolean value){
		prefs = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	

}
