package com.fgj.jcodecraeer.activitys;

import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
import com.fgj.jcodecraeer.BaseActivity;
import com.fgj.jcodecraeer.R;
import com.fgj.jcodecraeer.entity.Article;
import com.fgj.jcodecraeer.entity.Component;
import com.fgj.pulllistview.PullToRefreshListView;
import com.fgj.swipefinish.SildingFinishLayout;
import com.fgj.swipefinish.SildingFinishLayout.OnSildingFinishListener;

 /**
  * 
  * 
  *
  *****************************************************************************************************************************************************************************
  *  http://www.jcodecraeer.com/tags.php?/java/
  * @author :fengguangjing
  * @createTime:2016-10-14下午5:21:16
  * @version:4.2.4
  * @modifyTime:
  * @modifyAuthor:
  * @description:
  *****************************************************************************************************************************************************************************
  */
public class TAGMainActivity extends BaseActivity{
	private static final String TAG = "TAGMainActivity";
    private static final boolean DEBUG = true;
    
	private ArrayList<Article> mArticleList;
	private PullToRefreshListView mListView;
	private VideoListAdapter mAdapter;
	private ImageLoader mImageLoader;
	private ImageView topImg;
	
	private TextView title;
	private ListView componentsList;
	private ImageView down;
	private ArrayList<Component> mRightList = new ArrayList<Component>();
	
	static class ViewHolder {
		public TextView title;
		public TextView summary;
		public ImageView image;
		public TextView postTime; 
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_main);
		
		prepareView();
	}

	public void prepareView() {
		title = (TextView) findViewById(R.id.title);
		title.setText(getIntent().getStringExtra("name"));
		
		mArticleList = new ArrayList<Article>();
		final String  href = getIntent().getStringExtra("pagetid");
//		final String  href = "http://www.jcodecraeer.com/plus/list.php?tid=4";
		mImageLoader = new ImageLoader(this);
		mImageLoader.setRequiredSize(5 * (int)getResources().getDimension(R.dimen.litpic_width));
		mAdapter = new VideoListAdapter();
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               if(position > 0 && position <= mArticleList.size()){
                   Intent intent  = new Intent(TAGMainActivity.this, ArticleActivity.class);
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
				int pageIndex = mArticleList.size() / 25 + 1;
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
		
		componentsList = (ListView) findViewById(R.id.components_list);
		down = (ImageView) findViewById(R.id.down);
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
		componentsList.setAdapter(new ComponentAdapter());
		componentsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ComponentAdapter.ViewHolder holder = (ComponentAdapter.ViewHolder) arg1.getTag();
				String href = holder.url.getText().toString();
				if(href!=null && href.contains("http://www.jcodecraeer.com/tags.php?/")){
					loadNewsList(href, 1, true);
				}
			}
		});
		componentsList.setVisibility(View.GONE);
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
    			LayoutInflater layoutInflater = LayoutInflater.from(TAGMainActivity.this);
    			viewHolder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.item_tag_list, null);
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
	
 
	public ArrayList<Article>  parseArticleList(String href, final int page){
		ArrayList<Article> articleList = new ArrayList<Article>();
		try {
//			href = _MakeURL(href, new HashMap<String, Object>(){{
//			    put("", page);
//		    }});
			if(page>1){
				href = href + page+"/";
			}
			Log.i("url","url = " + href);
		    Document doc = Jsoup.connect(href).timeout(10000).get(); 
		    Element masthead = doc.select("div.col-md-10").first();
		    Elements articleElements =  masthead.select("li.archive-item");	
		    
		    //右侧标签
		    Element rightmasthead = doc.select("div.tags_list").first();
		    Elements componentElements =  rightmasthead.select("li.side");	
		    
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
				    mRightList.add(com);
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		    
		    
		    for(int i = 0; i < articleElements.size(); i++) {
			    Article article = new Article();
			    Element articleElement = articleElements.get(i);
			    
			    try {
			    	Element titleElement = articleElement.select("h3 a").first();
			    	String url = "http://www.jcodecraeer.com" + titleElement.attr("href"); 
				    String title = titleElement.text();
				    article.setTitle(title);
				    article.setUrl(url);
				    
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    try {
			    	Element summaryElement = articleElement.select("div.archive-detail p").first();
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
				    	imgsrc  ="http://www.jcodecraeer.com" + imgElement.attr("src");
				    }
				    article.setImageUrl(imgsrc);
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    try {
			    	 Element timeElement = articleElement.select(".date").first();
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
					if (articleList.size() < 25) {
						   mListView.onLoadingMoreComplete(true);	   
					} else if (articleList.size() == 25) {
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
    
    private class ComponentAdapter extends BaseAdapter {
    	public int getCount() {
    		return mRightList.size();
    	}

    	public Object getItem(int arg0) {
    		return mRightList.get(arg0);
    	}

    	public long getItemId(int arg0) {
    		return arg0;
    	}

    	public View getView(int position, View convertView, ViewGroup parent) {
    		ViewHolder viewHolder = null;
    		if (convertView == null) {
    			LayoutInflater layoutInflater = LayoutInflater.from(TAGMainActivity.this);
    			viewHolder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.item_comp_list, null);
	 			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
    			viewHolder.url = (TextView) convertView.findViewById(R.id.url);
    			convertView.setTag(viewHolder);
    		} else {
    			viewHolder = (ViewHolder) convertView.getTag();
    		}
    		Component article = mRightList.get(position);
 			viewHolder.title.setText(article.getName());
 			viewHolder.url.setText(article.getHref());
    		return convertView;
    	}
    	
    	class ViewHolder {
    		public TextView title;
    		public TextView url;
    	}
    }	
}