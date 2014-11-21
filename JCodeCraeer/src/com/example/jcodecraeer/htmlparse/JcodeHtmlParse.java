package com.example.jcodecraeer.htmlparse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.example.jcodecraeer.entity.JcodeMenu;
import com.example.jcodecraeer.entity.JcodeSubMenu;
import com.example.jcodecraeer.entity.Waterfall;
import com.example.jcodecraeer.entity.Waterfalls;


public class JcodeHtmlParse {
	private static String URL = "http://www.jcodecraeer.com/plus/list.php?tid=7";
	private static int pagersize;
	private static int currentpager;
	private static int size;
	
	public static void main(String[] args) {

//		ArrayList<Waterfall> waterlist = new ArrayList<Waterfall>();
//		
//		getTagsContent(URL, waterlist);
//		for(int i=2;i<=pagersize;i++){
//			getTagsContent("http://www.jcodecraeer.com/plus/list.php?tid=7&TotalResult=103&PageNo="+i, waterlist);
//		}
//		
//	   try {
//        	StringBuilder content = new StringBuilder();
//            content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"+"\n");
//            content.append("<jcodecraeers pagersize=\""+pagersize+"\" currentpager=\""+currentpager+"\"  size=\""+size+"\">"+"\n");
//            int i = 1;
//            for(Waterfall fall:waterlist){
//                content.append("    <jcodecraeer id=\""+i+"\">\n");
//                content.append("        <title>"+fall.getTitle()+"</title>\n");
//                content.append("        <imageName>http://www.jcodecraeer.com"+fall.getImageName()+"</imageName>\n");
//                content.append("        <url>"+fall.getUrl()+"</url>\n");
//                content.append("        <subName>"+""+"</subName>\n");
//                content.append("        <subUrl>http://www.jcodecraeer.com"+fall.getSubUrl()+"</subUrl>\n");
//                content.append("        <subAction>"+fall.getSubAction()+"</subAction>\n");
//                content.append("        <date>"+fall.getDate()+"</date>\n");
//                content.append("    </jcodecraeer>\n");
//                i++;
//            }
//            content.append("</jcodecraeers>");
//            File fileName = new File("C:\\jcodecraeer.xml"); 
//            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
//            fileOutputStream.write(content.toString().getBytes("utf-8")); 
//            fileOutputStream.flush();
//            fileOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//		Waterfall fall = getHtmlArticles("http://www.jcodecraeer.com/a/chengxusheji/java/2014/0903/1672.html");
//		
//		   try {
//		    	StringBuilder content = new StringBuilder();
//		        content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"+"\n");
//		        content.append("<jcodecraeers>"+"\n");
//	            content.append("    <jcodecraeer id=\""+1+"\">\n");
//	            content.append("        <title>"+fall.getTitle()+"</title>\n");
//	            content.append("        <imageName>"+fall.getImageName()+"</imageName>\n");
//	            content.append("        <url>"+fall.getUrl()+"</url>\n");
//	            content.append("        <subName>"+""+"</subName>\n");
//	            content.append("        <subUrl>"+fall.getSubUrl()+"</subUrl>\n");
//	            content.append("        <subAction>"+fall.getSubAction()+"</subAction>\n");
//	            content.append("        <date>"+fall.getDate()+"</date>\n");
//	            content.append("    </jcodecraeer>\n");
//		        content.append("</jcodecraeers>");
//		        File fileName = new File("C:\\articles.xml"); 
//		        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
//		        fileOutputStream.write(content.toString().getBytes("utf-8")); 
//		        fileOutputStream.flush();
//		        fileOutputStream.close();
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		    }
		
		try {
			ArrayList<JcodeMenu> menulist = getHtmlMenus("http://www.jcodecraeer.com/");
			StringBuilder content = new StringBuilder();
			content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"+"\n");
			content.append("<jcodeMenus>"+"\n");
			int i=1,j=1;
			for(JcodeMenu menu:menulist){
	            content.append("    <menu id=\""+i+"\">\n");
	            content.append("        <name>"+menu.getName()+"</name>\n");
	            content.append("        <href>http://www.jcodecraeer.com"+menu.getHref()+"</href>\n");
	            content.append("        <rel>"+menu.getRel()+"</rel>\n");
	            
	            if(menu.getSubmenus()!=null ){
	            	j=1;
	            	for(JcodeSubMenu submenu: menu.getSubmenus()){
	            		content.append("        <submenu id=\""+j+"\">\n");
	            		content.append("            <submenuname>"+submenu.getName()+"</submenuname>\n");
	    	            content.append("            <submenuhref>http://www.jcodecraeer.com"+submenu.getHref()+"</submenuhref>\n");
	    	            content.append("        </submenu>\n");
	    	            j++;
	            	}
	            }
	            content.append("    </menu>\n");
	            i++;
			}
			content.append("</jcodeMenus>");
	        File fileName = new File("C:\\jcodeMenus.xml"); 
	        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
	        fileOutputStream.write(content.toString().getBytes("utf-8")); 
	        fileOutputStream.flush();
	        fileOutputStream.close();
		} catch (IOException e) {
		     e.printStackTrace();
		}
	}
	
	public static Waterfalls getHtmlFalls(){
		Waterfalls falls = new Waterfalls();
		ArrayList<Waterfall> waterlist = new ArrayList<Waterfall>();
		
		getTagsContent(URL, waterlist);
		for(int i=2;i<=pagersize;i++){
			getTagsContent("http://www.jcodecraeer.com/plus/list.php?tid=18&TotalResult=739&PageNo="+i, waterlist);
		}	
		falls.setFalls(waterlist);
		return falls;
	}
	
	public static ArrayList<JcodeMenu> getHtmlMenus(String url){
		ArrayList<JcodeMenu> menulist = new ArrayList<JcodeMenu>();
		JcodeMenu menu;
		try {
			String html = getHtmlString(url);
			Parser parser = Parser.createParser(html, "GBK");
			NodeFilter pagefilter = new HasAttributeFilter("id", "navMenu");
			NodeList pageList = parser.extractAllNodesThatMatch(pagefilter);
			/**
			 * <div class="nav" id="navMenu">   
				  <ul id="nav" >
				  <!-- all -->
					    <li><a href='/plus/list.php?tid=4'  rel='dropmenu4'><span>综合资讯</span></a></li>
					    <li><a href='/plus/list.php?tid=6'  rel='dropmenu6'><span>程序设计</span></a></li>
					    <li><a href='/plus/list.php?tid=16'  rel='dropmenu16'><span>安卓开发</span></a></li>
					    <li><a href='/plus/list.php?tid=5'  rel='dropmenu5'><span>前端开发</span></a></li>
					    <li><a href='/plus/list.php?tid=14' ><span>数据库</span></a></li>
					    <li><a href='/plus/list.php?tid=15' ><span>开发日志</span></a></li>
					    <li><a href='/plus/list.php?tid=32' ><span>应用推荐</span></a></li>
					    <li><a href='/plus/list.php?tid=9' ><span>每日一站</span></a></li>
				  </ul>
				</div>
			 * ***/
			Node anode = pageList.elementAt(0);//<div class="nav" id="navMenu">   
			Node ulnode = anode.getFirstChild().getNextSibling();
			
			NodeList anodeList = ulnode.getChildren();
			int anodesize = anodeList.size();
			for(int i=0;i<anodesize;i++){
				Node node = anodeList.elementAt(i);
				String tag = node.getText();
				if(tag.equals("li")){ 
					try {
						LinkTag atag = (LinkTag) node.getFirstChild();
						String content = atag.toPlainTextString().replace(" ", "");
						if(!content.equals("")){
							menu = new JcodeMenu();
							menu.setName(content);
							System.out.println(atag.getAttribute("href"));
							System.out.println(atag.getAttribute("rel"));
							menu.setHref(atag.getAttribute("href"));
							if(atag.getAttribute("rel")!=null){
								menu.setRel(atag.getAttribute("rel"));
								//子菜单
								Parser subparser = Parser.createParser(html, "GBK");
								NodeFilter subpagefilter = new HasAttributeFilter("id", atag.getAttribute("rel"));
								NodeList subpageList = subparser.extractAllNodesThatMatch(subpagefilter);
								/****
								 * <div class="drop" id="dropmenu4">
					                    <ul>
					                      <li>
					                        <a href="/plus/list.php?tid=8" id="architecture">IT业界</a>
					                      </li>
					                      <li>
					                        <a href="/plus/list.php?tid=21" id="architecture">移动互联</a>
					                      </li>
					                      <li>
					                        <a href="/plus/list.php?tid=22" id="architecture">社交网络</a>
					                      </li>
					                      <li>
					                        <a href="/plus/list.php?tid=24" id="architecture">电子商务</a>
					                      </li>
					                      <li>
					                        <a href="/plus/list.php?tid=17" id="architecture">安卓资讯</a>
					                      </li>
					                    </ul>
					  				</div>
								 * ***/
								try {
									Node subanode = subpageList.elementAt(0);//<div class="nav" id="navMenu">   
									Node subulnode = subanode.getFirstChild().getNextSibling();
									NodeList subanodeList = subulnode.getChildren();
									int subanodesize = subanodeList.size();
									ArrayList<JcodeSubMenu> submenulist = new ArrayList<JcodeSubMenu>();
									for(int j=0;j<subanodesize;j++){
										Node subnode = subanodeList.elementAt(j);
										String subtag = subnode.getText();
										if(subtag.equals("li")){ 
											LinkTag subatag = (LinkTag) subnode.getFirstChild().getNextSibling();
											String subcontent = subatag.toPlainTextString().replace(" ", "");
											if(!subcontent.equals("")){
												JcodeSubMenu submenu = new JcodeSubMenu();
												submenu.setName(subcontent);
												System.out.println(subatag.getAttribute("href"));
												submenu.setHref(subatag.getAttribute("href"));
												submenulist.add(submenu);
											}
										}
									}
									menu.setSubmenus(submenulist.toArray(new JcodeSubMenu[0]));
								} catch (Exception e) {
								}
								
								
							}
							
							menulist.add(menu);
						}
					} catch (Exception e) {}
				}
			}
			
		}catch (Exception e) {}
		return menulist;
		
	}

	public static Waterfall getHtmlArticles(String url){
		Waterfall fall = new Waterfall();
		
		try {
			String html = getHtmlString(url);
			Parser parser = Parser.createParser(html, "GBK");
			NodeFilter pagefilter = new HasAttributeFilter("class", "articlecon clearfix");
			NodeList pageList = parser.extractAllNodesThatMatch(pagefilter);

			Node anode = pageList.elementAt(0);
			NodeList anodeList = anode.getChildren();
			int anodesize = anodeList.size();
			for(int i=0;i<anodesize;i++){
				Node node = anodeList.elementAt(i);
				String tag = node.getText();
				String content = node.toPlainTextString().replace(" ", "");
				if(!content.equals("")){
					
					if(tag.equals("h1 class=\"title\"")){
						//<h1 class="title"
						System.out.println(content);
						fall.setTitle(content);
					}
					
					if(tag.equals("div class=\"subinfo clearfix\"")){
						//<div class="subinfo clearfix">
						try {
							NodeList spanList = node.getChildren();
							for(int j=0;j<spanList.size();j++){
								Node snode = spanList.elementAt(j);
								content = snode.toPlainTextString().replace("  ", "").replace("	", "").replace(" ", "");
								if(!content.equals("")){
									System.out.println(content);
									fall.setDate(fall.getDate()+content+" ");
								}
							}
						} catch (Exception e) {
						}
					}
					
					if(tag.equals("div id=\"main_richtext\"")){
						content = node.toPlainTextString().replace("    ", "\n").replace("\n\n\n", "");;
						content = content.replace("&lt;", "").replace("&gt;", "").replace("&nbsp;", "");
						System.out.println(content);
						fall.setSubAction(content);
					}
				}
			}
			
			/**
			 * <div class="articlecon clearfix">
                    	<h1 class="title">在 KitKat以上版本中使用Translucent将Navigation Bar透明化</h1>
                        <div class="subinfo clearfix">
                                 	泡在网上的日子  发表于 2014-11-17 01:18  
                                <span>第</span> 
                            <script src="/plus/count.php?view=yes&aid=1992&mid=1" type='text/javascript' language="javascript"></script> 
                            <span>次阅读</span>
                            <span class="tags" id="arc_tags">                            
								<a href='/tags.php?/KitKat/' class='tag'>KitKat</a>
                             </span>
                            <script src="/action/is_admin.php?aid=1992"  type='text/javascript' language="javascript"> </script>
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
                                <p class="summary"><strong>摘要</strong> Android 从 4.4(KitKat) 开始提供了一个视觉上的提升，让最上方的状态栏 (Status Bar) 以及最下方的导航栏 (Navigation Bar) 可以被透明化，并让 APP 的内容可以往上下延伸，使整个画面的可被利用度大幅提升。 从 3.0 (honeycomb) 开始，Navigation Bar采用</p>
                            <div class="arc_body"> 
                             <p>Android 从 4.4(KitKat) 开始提供了一个视觉上的提升，让最上方的状态栏 (Status Bar) 以及最下方的导航栏 (Navigation Bar) 可以被透明化，并让 APP 的内容可以往上下延伸，使整个画面的可被利用度大幅提升。</p><p><img class="aligncenter size-medium wp-image-894" alt="KitKat_Transparent" src="/uploads/20141117/22571416157242.png" height="300" width="168" /></p><p>从 3.0 (honeycomb) 开始，Navigation Bar采用虚拟键，一直都占据一块不小的空间，对很多人来说，整个屏幕无法充利用，是一件相当痛苦的事情。也因此，有些人会刻意去挑选仍维持着实体键设计的手机。<br /><br />而 Google 似乎也意识到这个状况，从 4.4 (KitKat) &nbsp;提供了开发者一个新的作法，让我们可以把导航栏 (Navigation Bar)给透明化，并让内容延伸到该处，甚至是状态列 (Status Bar) 也可以被设定透明，这样再搭配 Action Bar 的配色，可以像上图一般，让整个 APP 更显得一致。</p><p>那我们就看看是如何实现的吧：<br /></p><pre class="brush:js;toolbar:false;">if (Build.VERSION.SDK_INT &gt;= Build.VERSION_CODES.KITKAT) {
							    Window window = getWindow();
							    // Translucent status bar
							    window.setFlags(
							        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
							        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
							    // Translucent navigation bar
							    window.setFlags(
							        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
							        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
							}</pre><p>的确，代码就是这么短，一行设定Status Bar、一行设定Navigation Bar 。<br /></p><p>别忘了判断一下版本。确保4.4以下不会报错。<br /><br />再来，有个部份要稍微留意一下，如果不希望 APP 的内容被上拉到状态列 (Status bar) 的话，要记得在介面 (Layout) XML 档中，最外面的那层，要再加上一个属性 fitsSystemWindows为true ，请见下方 </p><pre class="brush:js;toolbar:false;">&lt;RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
							    xmlns:tools="http://schemas.android.com/tools"
							    android:layout_width="match_parent"
							    android:layout_height="match_parent"
							    android:fitsSystemWindows="true"
							    tools:context=".MainActivity" &gt;
							    &lt;!-- Content --&gt;
							&lt;/RelativeLayout&gt;</pre><p>在界面的根层加入 android:fitsSystemWindows=”true” 这个属性，这样就可以让内容界面从 Action Bar 下方开始。<br /><br />再来，若我们的 APP 可以从 4.4 (KitKat) 开始支持，那其实可以直接从 style 去进行设定，我们可以在官网上看到对透明化的说明里，官方提供了两种 no title 的主题风格可以让我们使用，分别如下<br /><br />Theme.Holo.Light.NoActionBar.TranslucentDecor</p><p><img class="aligncenter size-medium wp-image-899" alt="KitKat_NoTitle_light_Translucent" src="/uploads/20141117/3241416157868.png" height="300" width="168" /></p><p>Theme.Holo.NoActionBar.TranslucentDecor<br /><a class="fancybox" href="http://blog.mosil.biz/wp-content/uploads/2014/01/KitKat_NoTitle_Translucent.png"><img class="aligncenter size-medium wp-image-898" alt="KitKat_NoTitle_Translucent" src="/uploads/20141117/21571416157878.png" height="300" width="168" /></a></p><p>这样我们就可以做出全屏幕的APP。<br /><br />如果我们希望可以维持Action Bar的存在，那只需要继承一般的主题，并在主题中分别加入两个属性值即可</p><pre class="brush:js;toolbar:false;">&lt;style name="AppTheme" parent="AppBaseTheme"&gt;
							    &lt;!-- Status Bar --&gt;
							    &lt;item name="android:windowTranslucentStatus"&gt;true&lt;/item&gt;
							    &lt;!-- Navigation Bar --&gt;
							    &lt;item name="android:windowTranslucentNavigation"&gt;true&lt;/item&gt;
							&lt;/style&gt;</pre><p>跟java代码方式一样，也是两行完成，上面一行是设定Status Bar、下面一行是设定Navigation Bar 。别忘了，如果不希望内容被 Action Bar 压住，那先前提及的 Layout 属性 &nbsp;android:fitsSystemWindows=”true” 要设置到。<br /><br /><br />其实以现在的状况来说，通过java代码方式去设定是最安全的，毕竟目前绝大部份的装置都还未被升级到 4.4 (KitKat)。<br /><br /></p>
                            </div>
                        </div>  
			 * **/
		} catch (Exception e) {
		}
		
		return fall;
	}
	
	public static void getTagsContent(String urlStr, ArrayList<Waterfall> waterlist) {
		String html = getHtmlString(urlStr);
		try {
			Parser parser = Parser.createParser(html, "GBK");

			/**
			 * 	<div class="paginate-container">
					<div class='pagination'>
					<ul>
						<li><a>首页</a></li>
						<li class="number thisclass"><a>1</a></li>
						<li class="number"><a href='/plus/list.php?tid=18&TotalResult=739&PageNo=2'>2</a></li>
						<li class="number"><a href='/plus/list.php?tid=18&TotalResult=739&PageNo=3'>3</a></li>
						<li class="number"><a href='/plus/list.php?tid=18&TotalResult=739&PageNo=4'>4</a></li>
						<li class="number"><a href='/plus/list.php?tid=18&TotalResult=739&PageNo=5'>5</a></li>
						<li class="number"><a href='/plus/list.php?tid=18&TotalResult=739&PageNo=6'>6</a></li>
						<li class="number"><a href='/plus/list.php?tid=18&TotalResult=739&PageNo=7'>7</a></li>
						<li class="number"><a href='/plus/list.php?tid=18&TotalResult=739&PageNo=8'>8</a></li>
						<li class="number"><a href='/plus/list.php?tid=18&TotalResult=739&PageNo=9'>9</a></li>
						<li class="number"><a href='/plus/list.php?tid=18&TotalResult=739&PageNo=10'>10</a></li>
						<li class="number"><a href='/plus/list.php?tid=18&TotalResult=739&PageNo=11'>11</a></li>
						<li><a href='/plus/list.php?tid=18&TotalResult=739&PageNo=2'>下一页</a></li>
						<li><a href='/plus/list.php?tid=18&TotalResult=739&PageNo=74'>末页</a></li>
						<li><span class="pageinfo">共 <strong>74</strong>页<strong>739</strong>条</span></li>
					</ul>
					<div style='float: none; clear: both;'></div>
					</div>
			  </div>

			**/
			// 页数
			NodeFilter pagefilter = new HasAttributeFilter("class", "pagination");
			NodeList pageList = parser.extractAllNodesThatMatch(pagefilter);

			Node anode = pageList.elementAt(0);
			NodeList anodeList = anode.getChildren();
			int anodesize = anodeList.size();
			
			if(anodesize>1){
				Node li = anodeList.elementAt(1);
				NodeList lianodeList = li.getChildren();
				for (int i = 0; i < lianodeList.size(); i++) {
					try {
						LinkTag atag = (LinkTag) lianodeList.elementAt(i).getFirstChild();
						System.out.println(atag.getAttribute("href"));
						System.out.println(atag.toPlainTextString());
					} catch (Exception e) {
						Node linode =  lianodeList.elementAt(i);
						String content = linode.toPlainTextString();
						System.out.println(content);
						if(!content.equals("") && content.contains("共 ")){
							String c1  = content.replace("共 ", "").replace("页", ",").replace("条", "");
							String pagers = c1.split(",")[0];
							String sizes = c1.split(",")[1];
							pagersize = Integer.parseInt(pagers);
							size = Integer.parseInt(sizes);
						}
					}
				}

			}
			
			// 内容
			Parser contentparser = Parser.createParser(html, "GBK");
			NodeFilter attrfilter = new HasAttributeFilter("class", "archive-list-item");
			NodeList nodeList = contentparser
					.extractAllNodesThatMatch(attrfilter);
			int size = nodeList.size();
			/**
			 * 
			 * <div class="archive-list-item">                           
                    <a href='/a/anzhuokaifa/androidkaifa/2014/1117/1992.html' ><img  src='/uploads/20141117/22571416157242-lp.png' class='home_thumbnail wp-post-image' /></a>
					<div class="post-intro">
						<h4>
							<a href="/a/anzhuokaifa/androidkaifa/2014/1117/1992.html" rel="bookmark" title="<b>在 KitKat以上版本中使用Translucent将Navigation Bar透明化</b>">
								<b>在 KitKat以上版本中使用Translucent将Navigation Bar透明化</b>
							</a>
						</h4>		
						<div class="clearfix">
							<a href='/tags.php?/KitKat/' class='tag'>KitKat</a> 
							<span class="author">泡在网上的日子</span> 
							<span class="spector">|</span>
							<span class="date">14-11-17</span>     
						    <span class="click">7阅</span>
						</div>						
						<p>Android 从 4.4(KitKat) 开始提供了一个视觉上的提升，让最上方的状态栏 (Status Bar) 以及最下方的导航栏 (Navigation Bar) 可以被透明化，并让 APP 的内容可以往上下延伸，使整个画面的可被利用度大幅提升。 从 3.0 (honeycomb) 开始，Navigation Bar采用 </p>
					</div>
				</div>
			 * 
			 */
			
			Waterfall fall;
			if (size > 1) {
				for (int i = 0; i < size; i++) {
					fall = new Waterfall();
					Node divnode = nodeList.elementAt(i);
					NodeList subchild = divnode.getChildren();

					try {
						// <a href=
						LinkTag ahref = (LinkTag) subchild.elementAt(1);
						System.out.println(ahref.getAttribute("href"));
						ImageTag imgTag = (ImageTag) ahref.getFirstChild();
						System.out.println(imgTag.getAttribute("src"));
						fall.setSubUrl(ahref.getAttribute("href"));
						fall.setImageName(imgTag.getAttribute("src"));
					
						 try {	
							//<div class="post-intro">
							Node postNode = subchild.elementAt(3);
								try {
									//H4 --a href
									Node h4node = postNode.getChildren().elementAt(1);
									LinkTag ahref2 = (LinkTag) h4node.getFirstChild();
									System.out.println(ahref2.getAttribute("href"));
									System.out.println(ahref2.getAttribute("title").replace("<b>", "").replace("</b>", ""));
									System.out.println(ahref2.toPlainTextString().replace("<b>", "").replace("</b>", ""));
									fall.setTitle(ahref2.getAttribute("title").replace("<b>", "").replace("</b>", ""));
									fall.setSubUrl(ahref2.getAttribute("href"));
								} catch (Exception e) {
								}
								
								try {
									//<div class="clearfix">
									Node clearnode = postNode.getChildren().elementAt(3);
									NodeList clearchild = clearnode.getChildren();
									for(int k=0;k<clearchild.size();k++){
										Node knode = clearchild.elementAt(k);
										if(k%2==1){
											System.out.println(knode.toPlainTextString().replace(" ", ""));
											fall.setDate(fall.getDate()+knode.toPlainTextString().replace(" ", "")+" ");
										}
									}
								} catch (Exception e) {
								}
							
								try {
								//<p>Android 从 4
								Node pNode = postNode.getChildren().elementAt(5);
								System.out.println(pNode.toPlainTextString().replace(" ", ""));
								fall.setSubAction(pNode.toPlainTextString().replace(" ", ""));
								} catch (Exception e) {
								}
						 	} catch (Exception e) {}
					
					} catch (Exception ex) {
						//h4
						/**
						 * <div class="archive-list-item">                           
						<div class="post-intro">
						<h4><a href="/a/anzhuokaifa/androidkaifa/2014/1117/1991.html" rel="bookmark" title="异常：java.lang.ClassCastException: android.view.ViewGroup$LayoutParams cannot be cast to android.vi">异常：java.lang.ClassCastException: android.view.ViewGroup$LayoutParams cannot be cast to android.vi</a></h4>		
						<div class="clearfix">
							<a href='/tags.php?/异常/' class='tag'>异常</a> 
							<span class="author">泡在网上的日子</span> 
							<span class="spector">|</span>
							<span class="date">14-11-16</span>     
						    <span class="click">3阅</span>
						</div>						
						<p>今天在使用LayoutParams时出现了一个问题，我是这样用的： 在gridview初始化的时候，为gridview添加了一个header（我用的是第三方GridView是可以添加header的）： headerView = new View(getActivity());LayoutParams params = new LayoutParams(LayoutParam </p>
					</div>
				</div>
						 * */
						
						 try {	
								//<div class="post-intro">
								Node postNode = subchild.elementAt(1);
									try {
										//H4 --a href
										Node h4node = postNode.getChildren().elementAt(1);
										LinkTag ahref2 = (LinkTag) h4node.getFirstChild();
										System.out.println(ahref2.getAttribute("href"));
										System.out.println(ahref2.getAttribute("title").replace("<b>", "").replace("</b>", ""));
										System.out.println(ahref2.toPlainTextString().replace("<b>", "").replace("</b>", ""));
										fall.setTitle(ahref2.getAttribute("title").replace("<b>", "").replace("</b>", ""));
										fall.setSubUrl(ahref2.getAttribute("href"));
										
									} catch (Exception e) {
									}
									
									try {
										//<div class="clearfix">
										Node clearnode = postNode.getChildren().elementAt(3);
										NodeList clearchild = clearnode.getChildren();
										for(int k=0;k<clearchild.size();k++){
											Node knode = clearchild.elementAt(k);
											if(k%2==1){
												System.out.println(knode.toPlainTextString().replace(" ", ""));
												fall.setDate(fall.getDate()+knode.toPlainTextString().replace(" ", "")+" ");
											}
										}
									} catch (Exception e) {
									}
								
									try {
									//<p>Android 从 4
									Node pNode = postNode.getChildren().elementAt(5);
									System.out.println(pNode.toPlainTextString().replace(" ", ""));
									fall.setSubAction(pNode.toPlainTextString().replace(" ", ""));
									} catch (Exception e) {
									}
							 	} catch (Exception e) {}
					}
					waterlist.add(fall);
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	public static String getHtmlString(String urlpath) {
		StringBuffer sb = new StringBuffer();
		String line = "";
		BufferedReader bw = null;
		URL url = null;
		InputStream inputStream;
		try {
			url = new URL(urlpath);
			HttpURLConnection httpsURLConnection = (HttpURLConnection) url
					.openConnection();

			httpsURLConnection.setRequestMethod("GET");
			httpsURLConnection.setConnectTimeout(5 * 1000);
			System.out.println("httpsURLConnection.getResponseCode()"
					+ httpsURLConnection.getResponseCode());
			if (httpsURLConnection.getResponseCode() == 200) {
				inputStream = httpsURLConnection.getInputStream();
				// inputStream = context.getAssets().open(
				// "www.umei.cc.htm");
				bw = new BufferedReader(new InputStreamReader(inputStream,
						"GBK"));
				while ((line = bw.readLine()) != null) {
					sb.append(line);
					System.out.println(line);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
}
