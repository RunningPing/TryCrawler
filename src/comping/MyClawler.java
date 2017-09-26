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
		System.out.println("ҳ���ȡ��ɣ���"+pageUrls.size()+"��ҳ��");
		int sortPicNum=1;
		for(String pageUrl:pageUrls){
			srcHtmlUrls=ParserHtml.getMainUrl(pageUrl, srcUrl, srcHtmlUrls);
		}
		System.out.println("ҳ�����ӻ�ȡ��ɣ���"+srcHtmlUrls.size()+"��ҳ��");
		for(String srcHtmlUrl:srcHtmlUrls){
			ArrayList<String> picUrlsTemp=ParserHtml.getPicture(srcHtmlUrl);
			if(!picUrlsTemp.isEmpty()){
				for(String picUrl:picUrlsTemp){
					SavePicture.savePicetur(picUrl,srcHtmlUrl,sortPicNum);
				}
				sortPicNum++;
			}

			
		}
		System.out.println("ͼƬ���ӻ�ȡ��ɣ���"+sortPicNum+"��ͼƬ");

	}
}
