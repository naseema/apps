package com.naseemapps.hangman;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;

public class GameSharedPref {

	
	final static String PREF_ELECTRIC_IMP_AGENT_ID = "prefElectecImpAgentId";
	final static String PREF_USER_ID = "prefUserId";
	
	
	private static SharedPreferences mSharedPrefs;
	
	private static GameSharedPref mIinstance;
	
	private GameSharedPref () {}
	
	private GameSharedPref (Context context) {
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static GameSharedPref getInstance(Context context) {
		if (mIinstance == null) {
			mIinstance = new GameSharedPref(context);
		}
		return mIinstance;
	}
	
	public void savePref(String key, String val) {
		Editor editor = mSharedPrefs.edit();
    		editor.putString(key, val);
    	editor.commit();
	}
	
	public String loadPref(String key, String defaultVal) {
		return mSharedPrefs.getString(key, defaultVal);
	}

	
}
