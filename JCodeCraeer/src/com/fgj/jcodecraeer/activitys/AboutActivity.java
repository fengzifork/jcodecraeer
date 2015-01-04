package com.fgj.jcodecraeer.activitys;

import com.fgj.jcodecraeer.R;

import android.app.Activity;
import android.os.Bundle;

public class AboutActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setTitle("关于");
		
		prepareView();
	}

	public void prepareView() {
	}
}
