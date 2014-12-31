package com.example.jcodecraeer.activitys;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imageloader.ImageLoader;
import com.example.jcodecraeer.R;
import com.example.jcodecraeer.entity.Article;
import com.example.pulllistview.PullToRefreshListView;

public class SubMainActivity extends Activity{
	private static final String TAG = "MainActivity";
    private static final boolean DEBUG = true;
    
	private ArrayList<Article> mArticleList;
	private PullToRefreshListView mListView;
	private VideoListAdapter mAdapter;
	private ImageLoader mImageLoader;
	static class ViewHolder {
		public TextView title;
		public TextView summary;
		public ImageView image;
		public TextView postTime; 
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_main);
		
		prepareView();
	}

	public void prepareView() {
		setTitle(getIntent().getStringExtra("name"));
		mArticleList = new ArrayList<Article>();
		final String  href = getIntent().getStringExtra("title");
		mImageLoader = new ImageLoader(this);
		mImageLoader.setRequiredSize(5 * (int)getResources().getDimension(R.dimen.litpic_width));
		mAdapter = new VideoListAdapter();
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               if(position > 0 && position <= mArticleList.size()){
                   Intent intent  = new Intent(SubMainActivity.this, ArticleActivity.class);
                   intent.putExtra("url", mArticleList.get(position - 1).getUrl());
                   intent.putExtra("title", mArticleList.get(position - 1).getTitle());
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
				int pageIndex = mArticleList.size() / 10 + 1;
				Log.i("pageIndex","pageIndex = " + pageIndex);
				loadNewsList(href, pageIndex, false);
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
    			LayoutInflater layoutInflater = LayoutInflater.from(SubMainActivity.this);
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
	
	
	/**
	 * 
	 * <div class="archive-list-item">                           
					<div class="post-intro">
						<h4><a href="/a/wangzhantuijian/yidonghulian/2014/1225/2216.html" rel="bookmark" title=" 苹果 iOS 和 OS X 系统质量下降危及未来增长"> 苹果 iOS 和 OS X 系统质量下降危及未来增长</a></h4>		
						<div class="clearfix">
							<a href='/tags.php?/苹果/' class='tag'>苹果</a> 
							<span class="author">泡在网上的日子</span> 
							<span class="spector">|</span>
							<span class="date">14-12-25</span>     
						    <span class="click">27阅</span>
						</div>						
						<p>《福布斯》杂志网络版周一发表分析文章称，尽管苹果硬件销售强劲，但是近来iOS 8和OS X Yosemite系统连连出现的问题为苹果敲响了警钟。软件质量的下降不仅会危及苹果未来增长，还会影响用户忠诚度。 以下是文章摘要： iPhone 6在发售首个周末销量就创下新纪 </p>
					</div>
				</div>
	 * */
	public ArrayList<Article>  parseArticleList(String href, final int page){
		ArrayList<Article> articleList = new ArrayList<Article>();
		try {
			href = _MakeURL(href, new HashMap<String, Object>(){{
			    put("PageNo", page);
		    }});
			Log.i("url","url = " + href);
		    Document doc = Jsoup.connect(href).timeout(10000).get(); 
		    Element masthead = doc.select("div.archive-list").first();
		    Elements articleElements =  masthead.select("div.archive-list-item");		
		    for(int i = 0; i < articleElements.size(); i++) {
			    Article article = new Article();
			    Element articleElement = articleElements.get(i);
			    
			    try {
			    	Element titleElement = articleElement.select("h4 a").first();
			    	String url = "http://www.jcodecraeer.com" + titleElement.attr("href"); 
				    String title = titleElement.text();
				    article.setTitle(title);
				    article.setUrl(url);
				    
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    try {
			    	Element summaryElement = articleElement.select("div.post-intro p").first();
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
