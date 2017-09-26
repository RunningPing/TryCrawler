package comping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserHtml {


	
	public static ArrayList<String> getMainUrl(String srcUrl,String url,ArrayList<String> elements){
		String srcHtml=MyHttpGetContent.getContent(srcUrl);
		Document doc=Jsoup.parse(srcHtml,url);
		Element bodyContent=doc.body();
		Elements contentTitle=bodyContent.select("table#threadlisttableid");
		Elements tbodys=contentTitle.select("tbody[id^=normalthread]");
		for(Element tbody:tbodys){
			Elements temp=tbody.select("a[class=s xst]");
			for(Element a:temp){
				String tempStr=a.attr("abs:href");
				elements.add(tempStr);
			}
		}
		return elements;
	}
		
	public static ArrayList<String> getPageUrl(String srcUrl){
		ArrayList<String> elements=new ArrayList<>();
		elements.add(srcUrl);
		String temp=srcUrl;
		int count=1;
		while(true){
			String srcHtml=MyHttpGetContent.getContent(temp);
			Document doc=Jsoup.parse(srcHtml,srcUrl);
			Element bodyContent=doc.body();
			Elements aTarget=bodyContent.select("a[class=nxt]");
			for(Element a:aTarget){
				if(a.text().contains("ÏÂÒ»Ò³")){
					temp=a.attr("abs:href");
					if(!temp.equals(elements.get(count-1))){
						elements.add(temp);
						count++;
					}
				}
			}
			if(count==21){
				break;
			}
		}
		return elements;
		
	}
	
	public static ArrayList<String> getPicture(String url){
		ArrayList<String> picUrls=new ArrayList<>();
		String srcHtml=MyHttpGetContent.getContent(url);
		Document doc=Jsoup.parse(srcHtml,url);
		Element bodyContent=doc.body();
		Elements aTargets=bodyContent.getElementsByTag("ignore_js_op");
		for(Element aTarget:aTargets){
			for(Element imgUrl:aTarget.getElementsByTag("img")){
				String picUrl=imgUrl.attr("abs:zoomfile");
				if(!picUrl.equals("")){
					picUrls.add(picUrl);
				}
			}
		}
		return picUrls;
	}
	
	public static void main(String[] args) {
		String url="http://rs.xidian.edu.cn/forum.php?mod=viewthread&tid=852063&extra=page%3D1";
//		String srcHtml=MyHttpGetContent.getContent(url);
//		System.out.println(srcHtml);
		
		ArrayList<String> urlList=new ArrayList<>();
		urlList=ParserHtml.getPicture(url);
		int i=0;
		System.out.println(urlList.size());
		for(String article:urlList){
			System.out.println(article);
		}
	}
}
