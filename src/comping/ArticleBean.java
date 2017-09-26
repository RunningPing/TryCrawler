package comping;

public class ArticleBean {

	private String title;
	private String url;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public ArticleBean(String title, String url) {
		super();
		this.title = title;
		this.url = url;
	}
	
	
}
