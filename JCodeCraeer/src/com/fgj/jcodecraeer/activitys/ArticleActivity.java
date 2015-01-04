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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.fgj.imageloader.ImageLoader;
import com.fgj.jcodecraeer.R;
import com.fgj.jcodecraeer.entity.Article;
import com.fgj.pulllistview.PullToRefreshListView;


public class ArticleActivity extends Activity{
	private ArrayList<Article> mArticleList;
	private PullToRefreshListView mListView;
	private VideoListAdapter mAdapter;
	private ImageLoader mImageLoader;
	private ImageView topImg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		
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
		loadNewsList(href, 1, true);
	}
	
	
	/**
	 * <div class="articlecon clearfix">
	<h1 class="title">僧多粥少？还原 OpenStack 的真实“钱景”</h1>
    <div class="subinfo clearfix">
             泡在网上的日子  发表于 2014-12-17 22:10  
            <span>第</span> 
        <script src="/plus/count.php?view=yes&aid=2177&mid=1" type='text/javascript' language="javascript"></script> 
        <span>次阅读</span>
        <span class="tags" id="arc_tags">                            
			<a href='/tags.php?/OpenStack/' class='tag'>OpenStack</a>
         </span>
        <script src="/action/is_admin.php?aid=2177"  type='text/javascript' language="javascript"> </script>
    </div>
	<!-- JiaThis Button BEGIN -->
	<div class="jiathis_style" style=" height:30px; width:180px; margin:auto;text-align:center;">
		<a class="jiathis_button_qzone"></a>
		<a class="jiathis_button_tsina"></a>
		<a class="jiathis_button_tqq"></a>
		<a class="jiathis_button_weixin"></a>
		<a class="jiathis_button_renren"></a>
		<a class="jiathis_button_xiaoyou"></a>
		<a href="http://www.jiathis.com/share" class="jiathis jiathis_txt jtico jtico_jiathis" target="_blank"></a>
		<a class="jiathis_counter_style"></a>
	</div>
	<script type="text/javascript" src="http://v3.jiathis.com/code/jia.js?uid=1397764132570636" charset="utf-8"></script>
	<!-- JiaThis Button END -->
    <div id="main_richtext"><!--富摘要-->
            <p class="summary"><strong>摘要</strong> 451 Research发布了OpenStack的收入分析预测，指出OpenStack市场的收入规模2014年约8.83亿美元，2018年将增长至33亿 美元，年复合增长率高达40%。但是,如今市场上已经有数十家“OpenStack公司”，他们靠什么盈利？未来空间有多大?以下内容由IT经理网编译整</p>
        <div class="arc_body"> 
         <p>451 Research发布了OpenStack的收入分析预测，指出OpenStack市场的收入规模2014年约8.83亿美元，2018年将增长至33亿 美元，年复合增长率高达40%。但是,如今市场上已经有数十家“OpenStack公司”，他们靠什么盈利？未来空间有多大?以下内容由IT经理网编译整 理自Gigaom：</p><p><strong>开源盈利模式之痛</strong></p><p>评估开源软件的商业价值通常是非常困难的一件事，主要是因为两点，一是开源软件服务市场的水很浑，各家厂商的收入往往都不很透明。再则，开源软件的 盈利模式非常多样化，有收取技术支持服务费的，有开源的同时搭售商业化版本的，有提供专业服务的，也有多种模式混合的。无论模式如何，开源市场迄今依然没 有诞生“高富帅”利润土壤，这让人们对时下火爆的OpenStack公司们感到担忧。</p><p>但有一件事是非常明确的：那就是IT厂商已经习惯了从软件授权中获取利润丰厚的预付款，然后通过年服务费和维护费再从客户身上细水长流地刮一遍油 水；但这种盈利模式在开源软件行当是绝对行不通的。一方面是因为很多企业客户选择开源软件服务就是为了摆脱软件授权模式的沉重枷锁，另外，对于开源软件厂 商而言，从产品中很难获取高额利润。 Host Analytics的首席执行官Dave Kellogg上个月在博客中对启动IPO进程的Hortonworks曾进行分析，他认为开源软件行当中唯一把生意做大的是Red Hat，但是相比IBM、Oracle，Red Hat依然只能算是小打小闹。</p><p>回到OpenStack的话题，如今市场上已经有数十家“OpenStack公司”，他们靠什么盈利？未来空间有多大?目前OpenStack基金 会有8家白金会员，16家金牌会员和87家企业赞助商，共计约111家“利益相关者”，想知道其中任何一家的真实收入都是非常困难的。</p><p><strong>僧多粥少，OpenStack的真实钱景</strong></p><p>幸运的是，上周<a href="https://451research.com/">451 Research</a>发布了 OpenStack的收入分析预测，指出OpenStack市场的收入规模2014年约8.83亿美元，2018年将增长至33亿美元，年复合增长率高达 40%。451 Research的报告调查了60家OpenStack业务公司，统计数字包括了公有云和私有云市场。报告指出当下OpenStack的大多数收入都来自 60家受访企业中的30家（例如Mirantis和Rackspace），大多数企业距离大规模盈利还很遥远。而且2018年的33亿美元收入平摊到60 家厂商头上，每家的平均理想化收入约5500万美元，钱景虽说不上暗淡，但也绝无太大想象空间，而且这个市场显然已经僧多粥少，不可能在涌入上百家新的企 业来分一杯羹。</p><p><img data-bd-imgshare-binded="1" style="background-image:none;border-bottom:0px;border-left:0px;display:inline;border-top:0px;border-right:0px;padding-left:0px;padding-right:0px;padding-top:0px;" title="openstack市场规模统计" alt="openstack市场规模统计" src="/uploads/20141217/32651418825984.jpg" height="398" width="632" border="0" /></p><p>从上图可以看出，OpenStack市场目前最主要的收入来自服务，其次是产品发行部署和管理整体方案、Devops开发运维工具和基于OpenStack的PaaS服务等。</p><p>实际上，OpenStack今天的成功很大一部分是品牌推广上的胜利，在开源云计算架构的战争中，OpenStack的反对者们认为<a href="http://www.ctocio.com/ccnews/9041.html">CloudStack实际上更加成熟</a>。甚至451 Research的云计算平台研究总监Jay Lyman也认为：如果企业需要立刻实施，那么选择CloudStack更加明智，但OpenStack的优势是会员企业实力雄厚，风物长宜放眼量，未来发展前景不错。（参考阅读：<a href="http://www.ctocio.com/ccnews/16052.html">OpenStack还要多少个十亿来修复</a>）</p>文章来自<a href="http://www.ctocio.com" title="IT经理网">IT经理网</a><p><br /></p>
        </div>
    </div>  
	 * **/
	public ArrayList<Article>  article(String href, final int page){
		ArrayList<Article> articleList = new ArrayList<Article>();
		try {
			Log.i("url","url = " + href);
		    Document doc = Jsoup.connect(href).timeout(50000).get(); 
		    Element masthead = doc.select("div.real_left").first();
		    Elements articleElements =  masthead.select("div.arc_body");	
		    Elements titleElements =  masthead.select("p.summary");
		    Elements firstElements =  masthead.select("div.first");
		    
	    	if(articleElements.size()>0){
	    		//除去每日一站
		    	for(int i = 0; i < articleElements.size(); i++) {
				    Article article = new Article();
				    Element articleElement = articleElements.get(i);
				    
				    try {
				    	 //摘要
				    	Element title = titleElements.get(0);
				        article.setTitle(title.text());
					} catch (Exception e) {
						e.printStackTrace();
					}
				   
				    
				    try {
				    	 //正文
					    String summary = articleElement.text();
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
	    	}else{
	    		//每日一站
				Article article = new Article();
				try {
			    	 //正文
				    String summary = firstElements.text();
				    if(summary.length() > 500000)
				    	summary = summary.substring(0, 500000);
				    article.setSummary(summary);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			   
			    
			    try {
			    	//图片
				    Element imgElement = null;
				    String imgsrc = "";
				    ArrayList<String> imgUrl = new ArrayList<String>();
				    int size = firstElements.select("img").size();
				    for(int j=0;j<size;j++){
				    	imgElement = firstElements.select("img").get(j);
					    if(imgElement != null){
					    	imgsrc  ="http://www.jcodecraeer.com" + imgElement.attr("src");
					    }
				    	imgUrl.add(imgsrc);
				    }
				    article.setAllImage(imgUrl);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			    articleList.add(article);
	    	}
			
			 
		    
		} catch (Exception e) {
			 e.printStackTrace();
		}
		
		return articleList;
	}
	
	/**
	 * <div class="wrapper"> 
                    <div class="pre_next">
                    <div class='div_button'>上一篇：<a href='/a/wangzhantuijian/waiwenfanyi/2014/1213/2152.html'>总结 2014 年中国互联网十大段子</a> <div>段子年年有，今年特别多。追寻回忆，搜集了一年中和互联网行业相关的一些段子，以飨读者。网络段子或赞扬或讽刺，或嘲弄，或戏谑，或调侃，或规劝，目的尽在不言中。一句段子的效果，抵得过千言万语。 【段子1】 民企腾讯有个老员工，每月收入买公司股票，坚</div></div>
                    <div class='div_button'>下一篇：<a href='/a/wangzhantuijian/waiwenfanyi/2014/1220/2193.html'>Coursera 创始人反思在线教育</a> <div>在 MOOC（大规模开放在线课程）开始兴起的时候，人们期望互联网技术能够变革传统教育，或者至少也能削弱传统教育的地位。事实证明，这只是一个比较天真的想法。近几年以来，虽然 MOOC 取得了很大的发展，但是，即使是 MOOC 的创业者们也承认，它并没有成为强</div></div>
                    </div>
                </div>

	 * **/
	public void LinkArticle(){
		
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
			LayoutInflater layoutInflater = LayoutInflater.from(ArticleActivity.this);
			convertView = layoutInflater.inflate(R.layout.article, null);
			TextView title = (TextView) convertView.findViewById(R.id.title);
			TextView summary = (TextView) convertView.findViewById(R.id.summary);
			LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout);
			
    		Article article = mArticleList.get(position);
 			title.setText(article.getTitle());
 			summary.setText(article.getSummary());
 			
 			for(final String url :article.getAllImage()){
 				if(!url.equals("")){
 					ImageView image = new ImageView(ArticleActivity.this);
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
							  ArticleActivity.this.startActivity(intent);
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
