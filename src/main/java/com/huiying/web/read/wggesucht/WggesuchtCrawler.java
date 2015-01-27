package com.huiying.web.read.wggesucht;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.huiying.web.read.util.DownloadManager;
import com.huiying.web.read.util.InputManager;
import com.huiying.web.read.util.OutputManager;

/**
 * crawl wg-gesucht.de
 * 
 * @author dhuiying
 *
 */

public class WggesuchtCrawler {

	static final private String HTTP = "http://";
	static final private String WGGESUCHTHOME = "www.wg-gesucht.de";
	static final private String SLASH = "/";
	static final private String DOT = ".";
	static final private String HTML = "html";

	private DownloadManager dm;
	private OutputManager om;
	private InputManager im;

	public WggesuchtCrawler(String proxyHost, String proxyPort) {
		this();
		this.dm = new DownloadManager(proxyHost, proxyPort);
	}

	public WggesuchtCrawler() {
		this.dm = new DownloadManager();
		this.om = new OutputManager();
		this.im = new InputManager();
	}

	/**
	 * download and analysis the search result by giving
	 */
	public void crawlMainSearchResultPage(List<List<String>> searchParameters) {

		// List<String> mainSearchResult = dm.doPost("http://www.wg-gesucht.de",
		// searchParameters);

		// List<String> searchResultPageUrls =
		// getSearchResultPageUrls(mainSearchResult);
		// for (String url : searchResultPageUrls) {
		// System.out.println(url);
		// }
		// om.save2File("/home/dhuiying/workspace/WebCrawler/wggesucht",
		// searchResultPageUrls,
		// "searchResultPageUrls_munich_wgzimmer.txt");

		// read the urls from file
		List<String> searchResultPageUrls = im
				.readFile("/home/dhuiying/workspace/WebCrawler/wggesucht/searchResultPageUrls_munich_wgzimmer.txt");

		// get all the urls for house info
		List<String> houseInfoUrls = getHouseInfoUrls(searchResultPageUrls);
		for (String url : houseInfoUrls) {
			System.out.println(url);
		}

	}

	private List<String> getHouseInfoUrls(List<String> searchResultPageUrls) {
		List<String> houseInfoUrls = new ArrayList<String>();
		for (String url : searchResultPageUrls) {
			// download the search result page
			List<String> searchResultonCertainPage = dm.DownloadWebPage(url);
			String line = "";
			for (int i = 0; i < searchResultonCertainPage.size(); i++) {
				if (searchResultonCertainPage.get(i).indexOf("class=\"  listenansicht0") != -1
						|| searchResultonCertainPage.get(i).indexOf("class=\"  listenansicht1") != -1) {
					line = searchResultonCertainPage.get(i);
					houseInfoUrls.add(line.substring(line.indexOf("adID") + 6,
							line.indexOf("html") + 4));
					System.out.println(houseInfoUrls.get(houseInfoUrls.size() - 1));
				}
			}
			// sleep for 10 seconds
			try {
				System.out.println("we are goint to sleep 10 seconds....");
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// append data into file
			om.save2File("/home/dhuiying/workspace/WebCrawler/wggesucht", houseInfoUrls,
					"houseInfoUrls_munich_wgzimmer.txt");
		}
		return houseInfoUrls;
	}

	private List<String> getSearchResultPageUrls(List<String> mainSearchResult) {
		int targetLineNumber = -1;
		for (int i = 0; i < mainSearchResult.size(); i++) {
			if (mainSearchResult
					.get(i)
					.indexOf(
							"<strong class=\"posR\" style=\"bottom: 1px; font-weight: normal;\">&raquo;</strong>") != -1) {
				System.out.println(mainSearchResult.get(i));
				// the target url is located in the line above
				// so remeber the number
				targetLineNumber = i - 1;
				break;
			}
		}
		if (targetLineNumber == -1) {
			// there is no target url. It is an error.
			return null;
		} else {
			// find the url of the last search page
			String line = mainSearchResult.get(targetLineNumber);
			String url = line.substring(line.indexOf("href") + 6, line.indexOf("html") + 4);
			// get the maximum number
			int end = url.lastIndexOf('.');
			int begin = url.substring(0, url.length() - 5).lastIndexOf('.') + 1;
			int max = Integer.valueOf(url.substring(begin, end));
			// System.out.println(max);
			List<String> searchResultPageUrls = new ArrayList<String>();
			// generate the urls
			for (int i = 0; i <= max; i++) {
				searchResultPageUrls.add(HTTP + WGGESUCHTHOME + SLASH + url.substring(0, begin)
						+ (new Integer(i)).toString() + DOT + HTML);
			}
			return searchResultPageUrls;
		}
	}

	public static void main(String[] args) {
		WggesuchtCrawler wgCrawler = new WggesuchtCrawler("124.67.253.138", "808");
		// WggesuchtCrawler wgCrawler = new WggesuchtCrawler();

		List<List<String>> paras = new ArrayList<List<String>>();
		paras.add(Arrays.asList("countrymanuel", "Deutschland"));
		paras.add(Arrays.asList("js_active", "1"));
		paras.add(Arrays.asList("rubrik", "0"));
		paras.add(Arrays.asList("stadt_key", "90"));
		paras.add(Arrays.asList("stadtmanuel", "München"));
		paras.add(Arrays.asList("type", "0"));

		wgCrawler.crawlMainSearchResultPage(paras);
	}

}
