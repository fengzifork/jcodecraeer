package com.fgj.jcodecraeer.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fgj.jcodecraeer.R;

public class OwnerActivity extends Activity implements OnClickListener {
	private TextView about;
	private TextView version;
	private TextView weibo;
	private LinearLayout login;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_owner);
		setTitle("个人中心");

		prepareView();
	}

	public void prepareView() {
		about = (TextView) findViewById(R.id.about);
		version = (TextView) findViewById(R.id.version);
		weibo = (TextView) findViewById(R.id.weibo_layout);
		login = (LinearLayout) findViewById(R.id.owner_layout);
		
		weibo.setOnClickListener(this);
		about.setOnClickListener(this);
		login.setOnClickListener(this);
		version.setText(version.getText()+getVersionName());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.about:
			Intent i = new Intent();
			i.setClass(OwnerActivity.this, AboutActivity.class);
			startActivity(i);
			break;
		case R.id.weibo_layout:
			Intent weibo = new Intent();
			weibo.setAction("android.intent.action.VIEW");    
            Uri content_url = Uri.parse("http://weibo.com/u/2711441293");   
            weibo.setData(content_url);  
			startActivity(weibo);
			break;
		case R.id.owner_layout:
			Intent owner = new Intent();
			owner.setAction("android.intent.action.VIEW");    
            Uri owner_url = Uri.parse("http://www.jcodecraeer.com/member/login.php");   
            owner.setData(owner_url);  
			startActivity(owner);
			break;
		}
	}

	private String getVersionName(){
		String version = "1.0";
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
					0);
			version = packInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}
}
