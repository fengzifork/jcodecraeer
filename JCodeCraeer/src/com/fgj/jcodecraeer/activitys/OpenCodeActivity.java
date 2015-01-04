package com.fgj.jcodecraeer.activitys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fgj.imageloader.ImageLoader;
import com.fgj.jcodecraeer.R;
import com.fgj.jcodecraeer.entity.Article;
import com.fgj.jcodecraeer.entity.Component;
import com.fgj.pulllistview.PullToRefreshListView;

 
public class OpenCodeActivity extends Activity{
	private static final String TAG = "OpenCodeActivity";
    private static final boolean DEBUG = true;
    
	private ArrayList<Article> mArticleList;
	private PullToRefreshListView mListView;
	private VideoListAdapter mAdapter;
	private ImageLoader mImageLoader;
	private ImageView topImg;
	private ImageView down;
	private ListView componentsList;
	private ArrayList<Component> mComponentList = new ArrayList<Component>();
	private ImageView onwer;
	
	static class ViewHolder {
		public TextView title;
		public TextView summary;
		public ImageView image;
		public TextView postTime; 
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_code);
		
		prepareView();
	}

	public void prepareView() {
		mArticleList = new ArrayList<Article>();
		final String  href = "http://www.jcodecraeer.com/plus/list.php?tid=31";
		mImageLoader = new ImageLoader(this);
		mImageLoader.setRequiredSize(5 * (int)getResources().getDimension(R.dimen.litpic_width));
		mAdapter = new VideoListAdapter();
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               if(position > 0 && position <= mArticleList.size()){
                   Intent intent  = new Intent(OpenCodeActivity.this, OpenSourceActivity.class);
                   intent.putExtra("url", mArticleList.get(position - 1).getUrl());
                   intent.putExtra("pagetid", mArticleList.get(position - 1).getTitle());
                   startActivity(intent);            	   
               }
            }
        });
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				loadNewsList(href, 1, true);
			}
			public void onLoadingMore() {
				int pageIndex = mArticleList.size() / 6 + 1;
				Log.i("pageIndex","pageIndex = " + pageIndex);
				loadNewsList(href, pageIndex, false);
			}
		});
		topImg = (ImageView) findViewById(R.id.top_img);
		topImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mListView.setSelection(0);
			}
		});
		down = (ImageView) findViewById(R.id.down);
		
		componentsList = (ListView) findViewById(R.id.components_list);
		componentsList.setAdapter(new ComponentAdapter());
		componentsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ComponentAdapter.ViewHolder holder = (ComponentAdapter.ViewHolder) arg1.getTag();
				String href = holder.url.getText().toString();
				if(href!=null && href.contains("http://www.jcodecraeer.com/plus/list.php?tid=31&codecategory=")){
					loadNewsList(href, 1, true);
				}
			}
		});
		componentsList.setVisibility(View.GONE);
		down.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(componentsList.getVisibility()==View.GONE){
					componentsList.setVisibility(View.VISIBLE);
				}else{
					componentsList.setVisibility(View.GONE);
				}
			}
		});
		onwer = (ImageView) findViewById(R.id.owner);
		onwer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(OpenCodeActivity.this, OwnerActivity.class);
				startActivity(i);
			}
		});
		loadNewsList(href, 1, true);
	}
	
	class VideoListAdapter extends BaseAdapter {
    	private int mLastAnimatedPosition;
    	public int getCount() {
    		return mArticleList.size();
    	}

    	public Object getItem(int arg0) {
    		return null;
    	}

    	public long getItemId(int arg0) {
    		return 0;
    	}

    	public View getView(int position, View convertView, ViewGroup parent) {
    		ViewHolder viewHolder = null;
    		if (convertView == null) {
    			LayoutInflater layoutInflater = LayoutInflater.from(OpenCodeActivity.this);
    			viewHolder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.item_article_list, null);
	 			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
    			viewHolder.summary = (TextView) convertView.findViewById(R.id.summary);
    			viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
    			viewHolder.postTime = (TextView) convertView.findViewById(R.id.postTime);
    			convertView.setTag(viewHolder);
    		} else {
    			viewHolder = (ViewHolder) convertView.getTag();
    		}
    		Article article = mArticleList.get(position);
 			viewHolder.title.setText(article.getTitle());
 			viewHolder.summary.setText(article.getSummary());
 			viewHolder.postTime.setText(article.getPostTime());
 			if(!article.getImageUrl().equals("")){
 				mImageLoader.DisplayImage(article.getImageUrl(), viewHolder.image);
 			}else{
 				viewHolder.image.setVisibility(View.GONE);
 			}
    		return convertView;
    	}
    }	
	
	private class ComponentAdapter extends BaseAdapter {
    	public int getCount() {
    		return mComponentList.size();
    	}

    	public Object getItem(int arg0) {
    		return mComponentList.get(arg0);
    	}

    	public long getItemId(int arg0) {
    		return arg0;
    	}

    	public View getView(int position, View convertView, ViewGroup parent) {
    		ViewHolder viewHolder = null;
    		if (convertView == null) {
    			LayoutInflater layoutInflater = LayoutInflater.from(OpenCodeActivity.this);
    			viewHolder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.item_comp_list, null);
	 			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
    			viewHolder.url = (TextView) convertView.findViewById(R.id.url);
    			convertView.setTag(viewHolder);
    		} else {
    			viewHolder = (ViewHolder) convertView.getTag();
    		}
    		Component article = mComponentList.get(position);
 			viewHolder.title.setText(article.getName());
 			viewHolder.url.setText(article.getHref());
    		return convertView;
    	}
    	
    	class ViewHolder {
    		public TextView title;
    		public TextView url;
    	}
    }	
	 
	public ArrayList<Article>  parseArticleList(String href, final int page){
		ArrayList<Article> articleList = new ArrayList<Article>();
		try {
			href = _MakeURL(href, new HashMap<String, Object>(){{
			    put("PageNo", page);
		    }});
			Log.i("url","url = " + href);
		    Document doc = Jsoup.connect(href).timeout(10000).get(); 
		    Element masthead = doc.select("div.real_left").first();
		    Elements articleElements =  masthead.select("li.codeli");	
		    
		    
		    Element rightmasthead = doc.select("div.fix_right").first();
		    Elements componentElements =  rightmasthead.select("li.slidebar-category-one");	
		    //解析菜单
		    /**
		     * <li class="slidebar-category-one ">
		     * <a href="/plus/list.php?tid=31&codecategory=500" >指示器 (ActivityIndicator) <span style="float:right;  ">3</span>
		     * </a>
		     * </li>
		     * **/
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
		    
		    //解析文件
		    for(int i = 0; i < articleElements.size(); i++) {
			    Article article = new Article();
			    Element articleElement = articleElements.get(i);
			    
			    try {
			    	Element titleElement = articleElement.select("div.codeli-info a").first();
			    	String url = "http://www.jcodecraeer.com" + titleElement.attr("href"); 
				    String title = titleElement.text();
				    article.setTitle(title);
				    article.setUrl(url);
				    
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    try {
			    	Element summaryElement = articleElement.select("div.codeli-info p").first();
			    	String summary = summaryElement.text();
				    if(summary.length() > 2000)
				    	summary = summary.substring(0, 2000);
				    article.setSummary(summary);
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    try {
			    	Element imgElement = null;
				    if(articleElement.select("img").size() != 0){
				       imgElement = articleElement.select("img").first();
				    }
				    String imgsrc = "";
				    if(imgElement != null){
				    	imgsrc  ="http://www.jcodecraeer.com" + imgElement.attr("data-url");
				    }
				    article.setImageUrl(imgsrc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    try {
			    	 Element timeElement = articleElement.select("div.otherinfo").first();
					    String postTime = timeElement.text();
					    article.setPostTime(postTime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    articleList.add(article);
		    }
		} catch (Exception e) {
			 e.printStackTrace();
		}
		
		return articleList;
	}
	
    private void loadNewsList(final String href ,final int page, final boolean isRefresh) {
	    final  Handler handler = new Handler(){
			public void handleMessage(Message msg) {		
				if (msg.what == 1) {
					ArrayList<Article> articleList = (ArrayList<Article>)msg.obj;
					if (isRefresh) {
					    mArticleList.clear();	//下拉刷新之前先将数据清空
						mListView.onRefreshComplete (new Date().toLocaleString());
					} 
				    for (Article article : articleList) {
				    	mArticleList.add(article);
				    }
				    mAdapter.notifyDataSetChanged();
					if (articleList.size() < 6) {
						   mListView.onLoadingMoreComplete(true);	   
					} else if (articleList.size() == 6) {
						   mListView.onLoadingMoreComplete(false);	 
					}
				}
			}
	    };

		new Thread() {
			public void run() {
				Message msg = new Message();
				ArrayList<Article> articleList = new ArrayList<Article>();
				try {
					articleList = parseArticleList(href, page);
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.what = 1;
				msg.obj = articleList;
				handler.sendMessage(msg);
			}
		}.start();
	} 
     		
	private static String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?")<0)
			url.append('?');
		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			//不做URLEncoder处理
			//url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
		}
		return url.toString().replace("?&", "?");
	}

}