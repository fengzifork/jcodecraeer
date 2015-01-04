package com.fgj.jcodecraeer.activitys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fgj.jcodecraeer.BaseActivity;
import com.fgj.jcodecraeer.R;
import com.fgj.jcodecraeer.entity.Component;

public class TAGActivity extends BaseActivity implements OnItemClickListener{
	private static final String TAG = "TAGActivity";
	private GridView  gridview;
	private ArrayList<Component> mComponentList = new ArrayList<Component>();
	private GridAdapter adapter;
	private ImageView onwer;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1000){
				adapter.notifyDataSetChanged();
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);
		
		prepareView();
	}

	@SuppressLint("NewApi")
	@Override
	public void prepareView() {
		AsyncTask.execute(new Runnable(){
			@Override
			public void run() {
//				parseTagList("http://www.jcodecraeer.com/tags.php");
				try {
					InputStream in = getAssets().open("tag.htm");
					StringBuffer sb = new StringBuffer();
					String line = "";
					BufferedReader bw = new BufferedReader(new InputStreamReader(in,
							"GBK"));
					while ((line = bw.readLine()) != null) {
						sb.append(line);
					}
					parseLocalTagList(sb.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		gridview = (GridView) findViewById(R.id.gridview);
		adapter = new GridAdapter();
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);
		onwer = (ImageView) findViewById(R.id.owner);
		onwer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(TAGActivity.this, OwnerActivity.class);
				startActivity(i);
			}
		});
	}
	
	private class GridAdapter extends BaseAdapter{
 
		public GridAdapter(){
			
		}
		
		@Override
		public int getCount() {
			return mComponentList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mComponentList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			ViewHodler hoder = null;
			if(view==null){
				LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
				view = inflater.inflate(R.layout.item_tag_menu, null);
				hoder = new ViewHodler();
				
				hoder.name = (TextView) view.findViewById(R.id.summary);
				hoder.title = (TextView) view.findViewById(R.id.title);
				
				view.setTag(hoder);
			}else{
				hoder = (ViewHodler) view.getTag();
			}
			
			hoder.name.setText(mComponentList.get(arg0).getName());
			hoder.title.setText(mComponentList.get(arg0).getHref());
			
			return view;
		}
		
		public class ViewHodler{
			TextView name;
			TextView title;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		GridAdapter.ViewHodler holder = (GridAdapter.ViewHodler) view.getTag();
		String pagetid = holder.title.getText().toString();
		String name = holder.name.getText().toString();
		
		if(pagetid!=null && pagetid.contains("http://www.jcodecraeer.com/tags.php?")){
			Intent i = new Intent();
			i.setClass(this, TAGMainActivity.class);
			i.putExtra("pagetid", pagetid);
			i.putExtra("name", name);
			startActivity(i);
		}
		
	}
	
	public void  parseTagList(String href){
		try {
			Log.i("url","url = " + href);
		    Document doc = Jsoup.connect(href).timeout(10000).get(); 
		    Element rightmasthead = doc.select("div.tags_list").first();
		    Elements componentElements =  rightmasthead.select("li a");	
		    
		    for(int i=0;i<componentElements.size();i++){
		    	try {
		    		Component com = new Component();
		    		Element componentElement = componentElements.get(i);
		    		Element component = componentElement.select("a").first();
		    		String url = "http://www.jcodecraeer.com" + component.attr("href"); 
				    String title = component.text();
				    Log.d(TAG, "url="+url+";title="+title);
				    com.setName(title);
				    com.setHref(url);
				    mComponentList.add(com);
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		    handler.sendEmptyMessage(1000);
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	public void  parseLocalTagList(String href){
		try {
		    Document doc = Jsoup.parse(href);
		    Element rightmasthead = doc.select("div.tags_list").first();
		    Elements componentElements =  rightmasthead.select("li a");	
		    
		    for(int i=0;i<componentElements.size();i++){
		    	try {
		    		Component com = new Component();
		    		Element componentElement = componentElements.get(i);
		    		Element component = componentElement.select("a").first();
		    		String url =  component.attr("href"); 
				    String title = component.text();
				    Log.d(TAG, "url="+url+";title="+title);
				    com.setName(title);
				    com.setHref(url);
				    mComponentList.add(com);
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		    handler.sendEmptyMessage(1000);
		} catch (Exception e) {
			 e.printStackTrace();
		}
		
	}
}
