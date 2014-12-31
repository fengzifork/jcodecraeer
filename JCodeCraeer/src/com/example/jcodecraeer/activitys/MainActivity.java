package com.example.jcodecraeer.activitys;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.jcodecraeer.BaseActivity;
import com.example.jcodecraeer.R;
import com.example.jcodecraeer.entity.Article;

public class MainActivity extends BaseActivity implements OnItemClickListener{
	private GridView  gridview;
	private ArrayList<Article> menus = new ArrayList<Article>();
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		prepareView();
	}

	@Override
	public void prepareView() {
		 //添加menu
		Article art;
		for(int i=0;i<10;i++){
			art = new Article();
			menus.add(art);
		}
		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new GridAdapter());
		gridview.setOnItemClickListener(this);
	}
	
	private class GridAdapter extends BaseAdapter{
 
		public GridAdapter(){
			
		}
		
		@Override
		public int getCount() {
			return menus.size();
		}

		@Override
		public Object getItem(int arg0) {
			return menus.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			View view = inflater.inflate(R.layout.item_grid_menu, null);
			return view;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent();
		i.setClass(this, SubMainActivity.class);
		startActivity(i);
	}

}
