package comping;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;  

public class MyHttpGetContent {
	
	private MyHttpGetContent(){
		initHttpClient();
	}
	
	private static final MyHttpGetContent myHttpGetContent = new MyHttpGetContent();
	
	public static MyHttpGetContent getMyHttpGetContentInstance(){
		return myHttpGetContent;
	}
	
	 HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {  
	        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {  
	            if (executionCount >= 3) {  
	                // 超过三次则不再重试请求  
	                return false;  
	            }  
	            if (exception instanceof InterruptedIOException) {  
	                // Timeout  
	                return false;  
	            }  
	            if (exception instanceof UnknownHostException) {  
	                // Unknown host  
	                return false;  
	            }  
	            if (exception instanceof ConnectTimeoutException) {  
	                // Connection refused  
	                return false;             
	            }  
	            if (exception instanceof SSLException) {  
	                // SSL handshake exception  
	                return false;  
	            }  
	            HttpClientContext clientContext = HttpClientContext.adapt(context);  
	            HttpRequest request = clientContext.getRequest();  
	            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);  
	            if (idempotent) {  
	                // Retry if the request is considered idempotent  
	                return true;  
	            }  
	            return false;  
	        }  
	    };
	    
	    private PoolingHttpClientConnectionManager httpClientConnectionManager = null;
	    
	    public void initHttpClient(){  
	        //创建httpclient连接池  
	        httpClientConnectionManager = new PoolingHttpClientConnectionManager();  
	        //设置连接池最大数量  
	        httpClientConnectionManager.setMaxTotal(10);  
	        //设置单个路由最大连接数量  
	        httpClientConnectionManager.setDefaultMaxPerRoute(10);  
	    }  
	    
	    public CloseableHttpClient getHttpClient(){
	    	RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).
	    			setSocketTimeout(10000).setCircularRedirectsAllowed(false).build();
	    	CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpClientConnectionManager)
	    			.setDefaultRequestConfig(requestConfig)
	    			.setRetryHandler(myRetryHandler)
	    			.build();
	    	return httpClient;
	    }
	    
	    public static String getContent(String urlString){
	    	String src="";
	    	if(null==urlString || urlString.isEmpty() || !urlString.startsWith("http")){//如果urlString为null或者urlString为空，或urlString非http开头，返回src空值  
	            return src;  
	        }
	    	CloseableHttpResponse response=null;
	    	HttpGet httpGet=null;
	    	urlString=urlString.trim();
	    	try {
				URL url=new URL(urlString);
				URI uri=new URI(url.getProtocol(),url.getHost(),url.getPath(),url.getQuery(),null);
				httpGet=new HttpGet(uri);
				httpGet.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");  
				httpGet.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");  
	            httpGet.addHeader("Connection","keep-alive");  
	            httpGet.addHeader("Accept-Encoding", "gzip, deflate");
	            httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
	            httpGet.addHeader("Cookie","UM_distinctid=15ad4707f8311b-0abc44c1bb8345-4c594131-100200-15ad4707f8469;safedog-flow-item=0652D496DBCD67CF7669013A0C745DB5;Q8qA_2132_saltkey=fPq1RIIP;Q8qA_2132_lastvisit=1490608808;Q8qA_2132_ulastactivity=214csLFFG52v4pVApxjgyzWEdldItgvsh4SLeNIbxSVb1M3bdcJ0;Q8qA_2132_auth=79cdRv5nZ4GJDu30FG%2FeOlF8JFPbpyH%2F3NflpSumlZfy2JElgf5xoMXKDCgFz6AuXtEI0XSO2uvPrnf02SdpbMpnfJY;Q8qA_2132_lastcheckfeed=293607%7C1490612433;Q8qA_2132_nofavfid=1;Q8qA_2132_visitedfid=217D72;Q8qA_2132_smile=1D1;Q8qA_2132_sid=xc044D;Q8qA_2132_lip=10.177.239.244%2C1492599152;Q8qA_2132_lastact=1492612487%09misc.php%09patch;Q8qA_2132_myrepeat_rr=R0; Q8qA_2132_sendmail=1");
	            response=MyHttpGetContent.getMyHttpGetContentInstance().getHttpClient().execute(httpGet);
	            int statuCode=response.getStatusLine().getStatusCode();
	            switch(statuCode){
	            case 200:
	            	HttpEntity entity=response.getEntity();
	            	InputStream inputStream = entity.getContent();
	            	BufferedInputStream bis=new BufferedInputStream(inputStream);
	            	ByteArrayBuffer buffer=new ByteArrayBuffer(4096);
	            	byte[] temp=new byte[4096];
	            	int count;
	            	String charset=null;
	            	while((count=bis.read(temp))!=-1){
	            		buffer.append(temp, 0, count);;
	            	}
	            	for(HeaderElement headerElement:entity.getContentType().getElements()){
	            		if(headerElement.toString().contains("utf-8"))
	            			charset="utf-8";
	            	};
	            	if(charset==null){
	            		charset="GB2312";
	            	}
	            	src=new String(buffer.toByteArray(),charset);
	            	break;
	            default:
	            	System.out.println("读取失败");
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
	    	
	    	return src;
	    }
	    
	    public static String getCookie(String urlString) throws Exception{
	    	String src="";
	    	if(null==urlString || urlString.isEmpty() || !urlString.startsWith("http")){//如果urlString为null或者urlString为空，或urlString非http开头，返回src空值  
	            return src;  
	        }
	    	CloseableHttpResponse response=null;
	    	HttpGet httpGet=null;
	    	urlString=urlString.trim();
	    	try {
				URL url=new URL(urlString);
				URI uri=new URI(url.getProtocol(),url.getHost(),url.getPath(),url.getQuery(),null);
				httpGet=new HttpGet(uri);
				httpGet.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");  
				httpGet.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");  
	            httpGet.addHeader("Connection","keep-alive");  
	            httpGet.addHeader("Accept-Encoding", "gzip, deflate");
	            httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
	            httpGet.addHeader("Cookie","safedog-flow-item=809EDB85DEB7B20E3EA42994FC74E667;Q8qA_2132_nofavfid=1;Q8qA_2132_saltkey=bcMwXzcA;Q8qA_2132_lastvisit=1504324417;Q8qA_2132_lastcheckfeed=293607%7C1504328056;Q8qA_2132_auth=1c6ertDgonZwfbjSgzXcLXKv953IZi6zOMZ8OWohDjfUo5fdhT1q0L1j7JWD3L0nojIjdVQDyi4g4fjfS4QzHWOMy1I;UM_distinctid=15e8d8a1045173-054d0bcdb653fa-333f5803-100200-15e8d8a1046346;Q8qA_2132_home_readfeed=1505619449;Q8qA_2132_visitedfid=16D91D72D142D13D554D560D110D567D555;Q8qA_2132_smile=4D1;Q8qA_2132_lip=10.170.56.114%2C1506431409;Q8qA_2132_myrepeat_rr=R0;Q8qA_2132_ulastactivity=11c5SLjmQQJofoNTY8CwXLezKUSo0y58tVeNjsy%2BiC4eI1rblJ8c;Q8qA_2132_sendmail=1;Q8qA_2132_lastact=1506431824%09portal.php%09; Q8qA_2132_sid=IjL44C;");
	            response=MyHttpGetContent.getMyHttpGetContentInstance().getHttpClient().execute(httpGet);
	            Header cookie=response.getFirstHeader("Set-Cookie");
	            cookieString=cookie.toString();
	            System.out.println(cookieString);
	    	}finally{
	    		httpGet.abort();
	    	}
	    	return cookieString;
	    	
	    }
	    static String cookieString=null;
	    
	    public static void main(String[] args) throws Exception {
	    	System.out.println(getContent("http://rs.xidian.edu.cn/portal.php"));
	    }
	    
}
