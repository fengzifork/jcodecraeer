package com.example.jcodecraeer.activitys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
import com.example.jcodecraeer.BaseActivity;
import com.example.jcodecraeer.R;
import com.example.jcodecraeer.entity.Article;
import com.example.pulllistview.PullToRefreshListView;

public class SubMainActivity extends BaseActivity{
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

	@Override
	public void prepareView() {
		setTitle(getIntent().getStringExtra("title"));
		mArticleList = new ArrayList<Article>();
		final String  href = "http://jcodecraeer.com/plus/list.php?tid=4";
		mImageLoader = new ImageLoader(this);
		mImageLoader.setRequiredSize(5 * (int)getResources().getDimension(R.dimen.litpic_width));
		mAdapter = new VideoListAdapter();
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               if(position > 0 && position <= mArticleList.size()){
//                   Intent intent  = new Intent(MainActivity.this, articleDetailActivity.class);
//                   intent.putExtra("href", mArticleList.get(position - 1).getLink());
//                   startActivity(intent);            	   
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
			    Element titleElement = articleElement.select("h4 a").first();
			    Element summaryElement = articleElement.select("div.post-intro p").first();
			    Element imgElement = null;
			    if(articleElement.select("img").size() != 0){
			       imgElement = articleElement.select("img").first();
			    }
			    Element timeElement = articleElement.select(".date").first();
			    String url = "http://www.jcodecraeer.com" + titleElement.attr("href"); 
			    String title = titleElement.text();
			    String summary = summaryElement.text();
			    if(summary.length() > 70)
			    	summary = summary.substring(0, 70);
			    String imgsrc = "";
			    if(imgElement != null){
			    	imgsrc  ="http://www.jcodecraeer.com" + imgElement.attr("src");
			    }
			  
			    String postTime = timeElement.text();
			    article.setTitle(title);
			    article.setSummary(summary);
			    article.setImageUrl(imgsrc);
			    article.setPostTime(postTime);
			    article.setUrl(url);
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
