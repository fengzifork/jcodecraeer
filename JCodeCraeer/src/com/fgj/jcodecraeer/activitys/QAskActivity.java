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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fgj.imageloader.ImageLoader;
import com.fgj.jcodecraeer.R;
import com.fgj.jcodecraeer.entity.Article;
import com.fgj.pulllistview.PullToRefreshListView;

 
public class QAskActivity extends Activity{
	private static final String TAG = "QAskActivity";
    private static final boolean DEBUG = true;
    
	private ArrayList<Article> mArticleList;
	private PullToRefreshListView mListView;
	private VideoListAdapter mAdapter;
	private ImageLoader mImageLoader;
	private ImageView topImg;
	private ImageView onwer;
	private Animation slide_bottom_to_top;
	
	static class ViewHolder {
		public TextView title;
		public TextView summary;
		public TextView postTime; 
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qask);
		
		prepareView();
	}

	public void prepareView() {
		setTitle(R.string.tab_main3);
		mArticleList = new ArrayList<Article>();
//		final String  href = getIntent().getStringExtra("pagetid");
		final String  href = "http://www.jcodecraeer.com/ask/?type=-1";
		mImageLoader = new ImageLoader(this);
		mImageLoader.setRequiredSize(5 * (int)getResources().getDimension(R.dimen.litpic_width));
		mAdapter = new VideoListAdapter();
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               if(position > 0 && position <= mArticleList.size()){
                   Intent intent  = new Intent(QAskActivity.this, QuestionActivity.class);
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
				int pageIndex = mArticleList.size() / 20 + 1;
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
		onwer = (ImageView) findViewById(R.id.owner);
		onwer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(QAskActivity.this, OwnerActivity.class);
				startActivity(i);
			}
		});
		slide_bottom_to_top = AnimationUtils.loadAnimation(QAskActivity.this, R.anim.push_right_in);
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
    			LayoutInflater layoutInflater = LayoutInflater.from(QAskActivity.this);
    			viewHolder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.item_qask_list, null);
	 			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
    			viewHolder.summary = (TextView) convertView.findViewById(R.id.summary);
    			viewHolder.postTime = (TextView) convertView.findViewById(R.id.postTime);
    			convertView.setTag(viewHolder);
    		} else {
    			viewHolder = (ViewHolder) convertView.getTag();
    		}
    		Article article = mArticleList.get(position);
 			viewHolder.title.setText(article.getTitle());
 			viewHolder.summary.setText(article.getSummary());
 			convertView.setAnimation(slide_bottom_to_top); 
    		return convertView;
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
		    Element masthead = doc.select("div.l-main-col").first();
		    Elements articleElements =  masthead.select("div.question-summary");		
		    for(int i = 0; i < articleElements.size(); i++) {
			    Article article = new Article();
			    Element articleElement = articleElements.get(i);
			    
			    try {
			    	Element titleElement = articleElement.select("h3 a").first();
			    	String url = "http://www.jcodecraeer.com/ask/" + titleElement.attr("href"); 
				    String title = titleElement.text();
				    article.setTitle(title);
				    article.setUrl(url);
				    
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    try {
			    	Element summaryElement = articleElement.select("div.started").first();
			    	String summary = summaryElement.text();
				    if(summary.length() > 2000)
				    	summary = summary.substring(0, 2000);
				    article.setSummary(summary);
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
					if (articleList.size() < 20) {
						   mListView.onLoadingMoreComplete(true);	   
					} else if (articleList.size() == 20) {
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
		}
		return url.toString().replace("?&", "?");
	}

}