package de.hfu.mos.home.news.data;


public class RssItem {
	
	// item title
	private final String title;
	// item link
	private final String link;

    public RssItem(String title, String link) {
        this.title = title;
        this.link = link;
    }
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		link = link;
	}
	
	@Override
	public String toString() {
		return title;
	}
	
}
