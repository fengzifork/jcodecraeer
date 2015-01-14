package com.fgj.jcodecraeer.activitys;

import com.fgj.jcodecraeer.R;
import com.fgj.swipefinish.SildingFinishLayout;
import com.fgj.swipefinish.SildingFinishLayout.OnSildingFinishListener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class AboutActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setTitle("关于");
		
		prepareView();
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, android.R.anim.slide_out_right);
	}
	public void prepareView() {
		RelativeLayout content = (RelativeLayout) findViewById(R.id.content);
		SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
		mSildingFinishLayout
				.setOnSildingFinishListener(new OnSildingFinishListener() {
					@Override
					public void onSildingFinish() {
						finish();
					}
				});
		mSildingFinishLayout.setTouchView(content);
	}
}
