package com.fgj.jcodecraeer;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fgj.jcodecraeer.activitys.MainTabActivity;
import com.fgj.jcodecraeer.utils.PreferenceUtils;

public class SplashActivity extends BaseActivity implements OnPageChangeListener,OnClickListener{
    private ViewPager viewPager;
    private ImageView[] dots ;  
    private int currentIndex;  
    private boolean misScrolled = true;
    private int size = 4;
    private ArrayList<View> pageViews;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		prepareView();
	}

	@Override
	public void prepareView() {
		viewPager = (ViewPager) findViewById(R.id.pager);
		
		
		LayoutInflater inflater = getLayoutInflater();
        pageViews = new ArrayList<View>();
        pageViews.add(inflater.inflate(R.layout.viewpager_splash1, null));
        pageViews.add(inflater.inflate(R.layout.viewpager_splash2, null));
        pageViews.add(inflater.inflate(R.layout.viewpager_splash3, null));
        pageViews.add(inflater.inflate(R.layout.viewpager_splash4, null));
        
        
        viewPager.setAdapter(new SplashPagerAdapter());
		viewPager.setOnPageChangeListener(this);
		initDots(); 
	}

	@Override
	public void onClick(View v) {
		int position = (Integer)v.getTag();  
        setCurView(position);  
        setCurDot(position);  
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		switch (state) {
		case ViewPager.SCROLL_STATE_DRAGGING:
			misScrolled = false;
			break;
		case ViewPager.SCROLL_STATE_SETTLING:
			misScrolled = true;
			break;
		case ViewPager.SCROLL_STATE_IDLE:
			if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !misScrolled) {
				prefs.setFirstIn(this,PreferenceUtils.IS_FIRST_IN,false);
				startActivity(new Intent(this, MainTabActivity.class));
				finish();
			}
			misScrolled = true;
			break;
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		 setCurDot(position);  
	}
	
	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
		dots = new ImageView[size];
		for (int i = 0; i < size; i++) {
			ImageView img = new ImageView(getApplicationContext());
			img.setImageResource(R.drawable.dot);
			img.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			img.setPadding(15, 15, 15, 15);
			img.setClickable(true);
			
			dots[i] = img;
			dots[i].setEnabled(true); 
			dots[i].setOnClickListener(this);
			dots[i].setTag(i); 
			
			ll.addView(dots[i]);
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false); 
	}
	
	 /** 
     *设置当前的引导页  
     */  
    private void setCurView(int position)  
    {  
        if (position < 0 || position >= size) {  
            return;  
        }  
  
       viewPager.setCurrentItem(position);  
    }  
  
    /** 
     *这只当前引导小点的选中  
     */  
    private void setCurDot(int position)  
    {  
        if (position < 0 || position > size - 1 || currentIndex == position) {  
            return;  
        }  
  
        dots[position].setEnabled(false);  
        dots[currentIndex].setEnabled(true);  
  
        currentIndex = position; 
    }  
    
    private class SplashPagerAdapter extends PagerAdapter{

        @Override
        public void destroyItem(View v, int position, Object arg2) {
            ((ViewPager)v).removeView(pageViews.get(position));
            
        }

        @Override
        public void finishUpdate(View arg0) {
            
        }
        
        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public Object instantiateItem(View v, int position) {
            ((ViewPager) v).addView(pageViews.get(position)); 
            //圆点点击，结束引导页
            return pageViews.get(position);  
        }

        @Override
        public boolean isViewFromObject(View v, Object arg1) {
            return v == arg1;
        }



        @Override
        public void startUpdate(View arg0) {
            
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
	

}
