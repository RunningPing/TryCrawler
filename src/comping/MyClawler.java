package comping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyClawler {

	public static void main(String[] args) throws IOException {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String srcUrl=br.readLine();
		ArrayList<String> pageUrls=new ArrayList<>();
		ArrayList<String> srcHtmlUrls=new ArrayList<>();
		ArrayList<String> picUrls=new ArrayList<>();
		pageUrls=ParserHtml.getPageUrl(srcUrl);
		System.out.println("页面获取完成，共"+pageUrls.size()+"个页面");
		int sortPicNum=1;
		for(String pageUrl:pageUrls){
			srcHtmlUrls=ParserHtml.getMainUrl(pageUrl, srcUrl, srcHtmlUrls);
		}
		System.out.println("页面链接获取完成，共"+srcHtmlUrls.size()+"个页面");
		for(String srcHtmlUrl:srcHtmlUrls){
			ArrayList<String> picUrlsTemp=ParserHtml.getPicture(srcHtmlUrl);
			if(!picUrlsTemp.isEmpty()){
				for(String picUrl:picUrlsTemp){
					SavePicture.savePicetur(picUrl,srcHtmlUrl,sortPicNum);
				}
				sortPicNum++;
			}

			
		}
		System.out.println("图片链接获取完成，共"+sortPicNum+"个图片");

	}
}
