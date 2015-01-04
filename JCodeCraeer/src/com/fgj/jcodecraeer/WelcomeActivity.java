package com.fgj.jcodecraeer;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.fgj.jcodecraeer.activitys.MainTabActivity;
import com.fgj.jcodecraeer.utils.PreferenceUtils;

public class WelcomeActivity extends BaseActivity {
	ImageView imageView;
	AnimationDrawable anim;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1000){
				if(prefs.IsFisrtIn(WelcomeActivity.this, PreferenceUtils.IS_FIRST_IN)){
					//进入引导页面
					Intent intent = new Intent();
					intent.setClass(WelcomeActivity.this, SplashActivity.class);
					startActivity(intent);
				}else{
					//进入主类
					Intent intent = new Intent();
					intent.setClass(WelcomeActivity.this, MainTabActivity.class);
					startActivity(intent);
				}
				finish();
			}
			
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.anim.welcome);
        anim = (AnimationDrawable) imageView.getDrawable();
        anim.start();
        
        try {
        	handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					handler.sendEmptyMessage(1000);
				}
			}, 2000);
		} catch (Exception e) {
		}
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		super.onPause();
		anim.stop();
	}
 

}
