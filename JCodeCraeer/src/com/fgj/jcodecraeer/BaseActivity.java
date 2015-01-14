package com.fgj.jcodecraeer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.fgj.jcodecraeer.utils.PreferenceUtils;

public class BaseActivity extends Activity{
	public PreferenceUtils prefs;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		prefs = new PreferenceUtils();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, android.R.anim.slide_out_right);
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
