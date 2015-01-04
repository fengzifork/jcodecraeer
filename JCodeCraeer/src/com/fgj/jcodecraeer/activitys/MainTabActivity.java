package com.fgj.jcodecraeer.activitys;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.fgj.jcodecraeer.R;

public class MainTabActivity extends TabActivity implements OnCheckedChangeListener{
	TabHost mTabHost;
	RadioGroup mRadioGroup;
	RadioButton radio1,radio2,radio3,radio4;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_tab);
		
		mTabHost =  getTabHost();
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		 
		TabSpec tab_main1 = mTabHost.newTabSpec(getString(R.string.tab_main1));
		TabSpec tab_main2 = mTabHost.newTabSpec(getString(R.string.tab_main2));
		TabSpec tab_main3 = mTabHost.newTabSpec(getString(R.string.tab_main3));
		TabSpec tab_main4 = mTabHost.newTabSpec(getString(R.string.tab_main4));

		tab_main1.setContent(new Intent(this,  MainActivity.class))
				.setIndicator(getString(R.string.tab_main1));
		tab_main2.setContent(new Intent(this, OpenCodeActivity.class))
				.setIndicator(getString(R.string.tab_main2));
		tab_main3.setContent(new Intent(this, QAskActivity.class))
				.setIndicator(getString(R.string.tab_main3));
		tab_main4.setContent(new Intent(this, TAGActivity.class))
				.setIndicator(getString(R.string.tab_main4));

		mTabHost.addTab(tab_main1);
		mTabHost.addTab(tab_main2);
		mTabHost.addTab(tab_main3);
		mTabHost.addTab(tab_main4);
		
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radio1 = (RadioButton) findViewById(R.id.radio1);
		radio2 = (RadioButton) findViewById(R.id.radio2);
		radio3 = (RadioButton) findViewById(R.id.radio3);
		radio4 = (RadioButton) findViewById(R.id.radio4);
		
		mRadioGroup.setOnCheckedChangeListener(this);
		
		radio1.setChecked(true);
	}
	
 

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio1:
			mTabHost.setCurrentTabByTag(getString(R.string.tab_main1));
			break;
		case R.id.radio2:
			mTabHost.setCurrentTabByTag(getString(R.string.tab_main2));
			break;
		case R.id.radio3:
			mTabHost.setCurrentTabByTag(getString(R.string.tab_main3));
			break;
		case R.id.radio4:
			mTabHost.setCurrentTabByTag(getString(R.string.tab_main4));
			break;
		default:
			break;
		}
	}
	
 
}
