package comping;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

public class SavePicture {
	
	

	public  static void savePicetur(String urlString,String imageName,int num) throws IOException{
		File dir=new File("D:/Use/rspic");
		if(!dir.exists()){
			dir.mkdirs();
		}
		String tmp=imageName.substring(imageName.indexOf("?")+1)+urlString.substring(urlString.lastIndexOf("/")+1);
		File file=null;
		BufferedOutputStream bos = null;
		OutputStream os=null;
    	if(null==urlString || urlString.isEmpty() || !urlString.startsWith("http")){//如果urlString为null或者urlString为空，或urlString非http开头，返回src空值  
            return ;  
        }
    	CloseableHttpResponse response=null;
    	HttpGet httpGet=null;
    	urlString=urlString.trim();
    	try {
			URL url=new URL(urlString);
			URI uri=new URI(url.getProtocol(),url.getHost(),url.getPath(),url.getQuery(),null);
			httpGet=new HttpGet(uri);
			httpGet.addHeader("Accept","*/*");  
            httpGet.addHeader("Connection","keep-alive");  
            httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
            response=MyHttpGetContent.getMyHttpGetContentInstance().getHttpClient().execute(httpGet);
            int statuCode=response.getStatusLine().getStatusCode();
            switch(statuCode){
            case 200:
            	try {
        			file=new File("D:/Use/rspic"+"/"+num+"--"+tmp);
        			os=new FileOutputStream(file);
        			bos=new BufferedOutputStream(os);
        		} catch (FileNotFoundException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
            	HttpEntity entity=response.getEntity();
            	InputStream inputStream = entity.getContent();
            	BufferedInputStream bis=new BufferedInputStream(inputStream);
            	byte[] temp=new byte[4096];
            	int count;
            	while((count=bis.read(temp))!=-1){
            		 byte[] temp1 = new byte[count];
                     System.arraycopy(temp, 0, temp1, 0, count);
                     bos.write(temp1);
            	}
            	bis.close();
            	bos.close();
            	System.out.println(num+"--"+tmp.toString()+"保存成功");
            	break;
            default:
            	System.out.println("保存失败,图片链接是"+urlString+"  页面地址是："+imageName);
            }
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(response!=null){
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			httpGet.abort();
		}
    	
	}
	
	public static void main(String[] args) throws IOException {
		SavePicture.savePicetur("http://rs.xidian.edu.cn/data/attachment/forum/201703/18/155931e1gieuyu7w1ypzpg.jpg", "http://rs.xidian.edu.cn/forum.php?mod=viewthread&tid=853609&extra=page%3D1", 1);
	}
}
