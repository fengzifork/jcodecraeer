package com.example.jcodecraeer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.jcodecraeer.utils.PreferenceUtils;

public class BaseActivity extends Activity{
	public PreferenceUtils prefs;
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		prefs = new PreferenceUtils();
	}
	
	
	public void prepareView(){
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
