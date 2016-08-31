package com.fgj.jcodecraeer.activitys;

import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.fgj.imageloader.ImageLoader;
import com.fgj.jcodecraeer.R;
import com.fgj.jcodecraeer.entity.Article;
import com.fgj.pulllistview.PullToRefreshListView;
import com.fgj.swipefinish.SildingFinishLayout;
import com.fgj.swipefinish.SildingFinishLayout.OnSildingFinishListener;


public class QuestionActivity extends Activity{
	private ArrayList<Article> mArticleList;
	private PullToRefreshListView mListView;
	private VideoListAdapter mAdapter;
	private ImageLoader mImageLoader;
	private ImageView topImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
		prepareView();
	}

	public void prepareView() {
		setTitle(getIntent().getStringExtra("pagetid"));
		
		mArticleList = new ArrayList<Article>();
		final String  href = getIntent().getStringExtra("url");
		mImageLoader = new ImageLoader(this);
		mImageLoader.setRequiredSize(5 * (int)getResources().getDimension(R.dimen.litpic_width));
		mAdapter = new VideoListAdapter();
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		mListView.setAdapter(mAdapter);
		
		topImg = (ImageView) findViewById(R.id.top_img);
		topImg.setVisibility(View.GONE);
		topImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mListView.setSelection(0);
			}
		});
		mListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				loadNewsList(href, 1, true);
			}
			public void onLoadingMore() {
				int pageIndex = 1;
				Log.i("pageIndex","pageIndex = " + pageIndex);
				loadNewsList(href, pageIndex, false);
			}
		});
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				topImg.setVisibility(View.VISIBLE);
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Uri uri = Uri.parse(href);
				Intent intent = new Intent("android.intent.action.VIEW", uri);
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				QuestionActivity.this.startActivity(intent);
				
			}
		});
		loadNewsList(href, 1, true);
		SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
		mSildingFinishLayout
				.setOnSildingFinishListener(new OnSildingFinishListener() {
					@Override
					public void onSildingFinish() {
						finish();
					}
				});
		mSildingFinishLayout.setTouchView(mListView);
	}
	
 
	public ArrayList<Article>  article(String href, final int page){
		ArrayList<Article> articleList = new ArrayList<Article>();
		try {
			Log.i("url","url = " + href);
		    Document doc = Jsoup.connect(href).timeout(50000).get(); 
		    Element masthead = doc.select("div.col-md-9").first();
		    Elements articleElements =  masthead.select("div.ask_seg_content");
		    Elements titleElements =  masthead.select("div.reply_box");
		    
	    	for(int i = 0; i < articleElements.size(); i++) {
			    Article article = new Article();
			    Element articleElement = articleElements.get(i);
			    
			    try {
			    	 //摘要
			        article.setTitle(articleElement.text());
				} catch (Exception e) {
					e.printStackTrace();
				}
			   
			    
			    try {
			    	 //正文
				    String summary = titleElements.text();
				    if(summary.length() > 500000)
				    	summary = summary.substring(0, 500000);
				    article.setSummary(summary);
				} catch (Exception e) {
					e.printStackTrace();
				}
			   
			    
			    try {
			    	//图片
				    Element imgElement = null;
				    String imgsrc = "";
				    ArrayList<String> imgUrl = new ArrayList<String>();
				    int size = articleElement.select("img").size();
				    for(int j=0;j<size;j++){
				    	imgElement = articleElement.select("img").get(j);
					    if(imgElement != null){
					    	imgsrc  ="http://www.jcodecraeer.com" + imgElement.attr("src");
					    }
				    	imgUrl.add(imgsrc);
				    }
				    article.setAllImage(imgUrl);
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
			LayoutInflater layoutInflater = LayoutInflater.from(QuestionActivity.this);
			convertView = layoutInflater.inflate(R.layout.article, null);
			TextView title = (TextView) convertView.findViewById(R.id.title);
			TextView summary = (TextView) convertView.findViewById(R.id.summary);
			LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout);
			
    		Article article = mArticleList.get(position);
 			title.setText(article.getTitle());
 			summary.setText(article.getSummary());
 			
 			for(final String url :article.getAllImage()){
 				if(!url.equals("")){
 					ImageView image = new ImageView(QuestionActivity.this);
 					image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
 					image.setAdjustViewBounds(true);
 					image.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							  Intent intent = new Intent("android.intent.action.VIEW"); 
							  intent.addCategory("android.intent.category.DEFAULT"); 
							  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
							  Uri uri = Uri.fromFile(mImageLoader.getCache(url)); 
							  intent.setDataAndType(uri, "image/*"); 
							  QuestionActivity.this.startActivity(intent);
						}
					});
 					layout.addView(image);
 					mImageLoader.DisplayImage(url, image);
 				}
 			}
    		return convertView;
    	}
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
					if (articleList.size() < 10) {
						   mListView.onLoadingMoreComplete(true);	   
					} else if (articleList.size() == 10) {
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
					articleList = article(href, page);
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

}
