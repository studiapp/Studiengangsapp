package de.hfu.mos.home.news;

import de.hfu.mos.home.news.data.RssItem;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class RssReader {
	// Our class has an attribute which represents RSS Feed URL
	private String rssUrl;

	/** We set this URL with the constructor
	 * Constructor
	 * 
	 * @param rssUrl
	 */
	public RssReader(String rssUrl) {
		this.rssUrl = rssUrl;
	}

	/**
	 * Get RSS items.
	 * This method will be called to get the parsing process result
	 * @return
	 */
	public List<RssItem> getItems() throws Exception {
		
		// SAX parse RSS data
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		RssParseHandler handler = new RssParseHandler();
		
		saxParser.parse(rssUrl, handler);

		return handler.getItems();
		
	}

}
