package com.huiying.web.read.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * this class is used to download web pages
 * 
 * @author dhuiying
 *
 */

public class DownloadManager {

	Properties props = System.getProperties();

	public DownloadManager(String proxyHost, String proxyPort) {
		this.props.put("http.proxyHost", proxyHost);
		this.props.put("http.proxyPort", proxyPort);
	}

	public DownloadManager() {

	}

	public List<String> DownloadWebPage(String url) {
		InputStream is = null;
		BufferedReader br = null;
		List<String> content = new ArrayList<String>();
		String line = null;
		URL urlAddress = null;

		while (true) {
			try {
				urlAddress = new URL(url);
				URLConnection urlc = urlAddress.openConnection();
				System.out.println(urlAddress);
				urlc.setConnectTimeout(10000);
				urlc.setReadTimeout(10000);
				urlc.setRequestProperty("User-Agent",
						"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:35.0) Gecko/20100101 Firefox/35.0");
				urlc.setRequestProperty("Connection", "Keep-alive");
				urlc.setRequestProperty(
						"Cookie",
						"__gads=ID=1a70a134abb6b2d7:T=1420754084:S=ALNI_MYTsJuCVZ9sWZikYTtlNRfJ2z2hCQ; __utma=227182235.2135999470.1420754084.1421590583.1421595511.11; __utmz=227182235.1420754085.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __uvt=; uvts=2YAbwGQcBqjKgJOl; last_city=90; last_cat=0; PHPSESSID=al3tedhekl72s2662erf2te8j1; tablet_cookie=2; __utmc=227182235; POPUPCHECK=1421659485787");
				is = urlc.getInputStream();
				// is = url.openStream(); // throws an IOException
				br = new BufferedReader(new InputStreamReader(is));

				while ((line = br.readLine()) != null) {
					content.add(line);
				}
				return content;
			} catch (MalformedURLException mue) {
				mue.printStackTrace();
				return null;
			} catch (ConnectException ce) {
				ce.printStackTrace();
				try {
					Thread.sleep(20000); // do nothing for 20000 miliseconds (20
											// second)
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				try {
					Thread.sleep(10000); // do nothing for 10000 miliseconds (10
											// second)
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} finally {
				try {
					if (br != null)
						br.close();
					if (is != null)
						is.close();
				} catch (IOException ioe) {
					// nothing to see here
					ioe.printStackTrace();
				}
			}
		}

	}

	/**
	 * simulate a post request
	 * 
	 * @param url
	 * @param paras
	 * @return the response html
	 */
	public List<String> doPost(String url, List<List<String>> paras) {
		List<String> content = new ArrayList<String>();
		OutputStreamWriter wr = null;
		BufferedReader rd = null;
		while (true) {
			try {
				// Construct the post data structure
				String data = null;
				for (int i = 0; i < paras.size(); i++) {
					if (i == 0)// first parameter
					{
						data = URLEncoder.encode(paras.get(i).get(0), "UTF-8") + "="
								+ URLEncoder.encode(paras.get(i).get(1), "UTF-8");
					} else {
						data += "&" + URLEncoder.encode(paras.get(i).get(0), "UTF-8") + "="
								+ URLEncoder.encode(paras.get(i).get(1), "UTF-8");
					}
				}

				// Send data
				URL urlAddress = new URL(url);
				URLConnection conn = urlAddress.openConnection();
				// conn.setRequestProperty("User-Agent",
				// "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:35.0) Gecko/20100101 Firefox/35.0");
				conn.setDoOutput(true);
				wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(data);
				wr.flush();

				// Get the response
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				// rd = new BufferedReader(new
				// InputStreamReader(conn.getInputStream(),"utf-8"));
				String line;
				while ((line = rd.readLine()) != null) {
					// Process line...
					content.add(line);
				}
				return content;
			} catch (MalformedURLException mue) {
				mue.printStackTrace();
				return null;
			} catch (ConnectException ce) {
				ce.printStackTrace();
				try {
					Thread.sleep(20000); // do nothing for 20000 miliseconds (20
											// second)
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
				try {
					Thread.sleep(10000); // do nothing for 10000 miliseconds (10
											// second)
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} finally {
				try {
					if (wr != null)
						wr.close();
					if (rd != null)
						rd.close();
				} catch (IOException ioe) {
					// nothing to see here
					ioe.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		DownloadManager dm = new DownloadManager("124.67.253.138", "808");

		/* test method DownloadWebPage */
		// List<String> content = dm.DownloadWebPage("http://wg-gesucht.de");

		/* test method doPost */
		List<List<String>> paras = new ArrayList<List<String>>();
		paras.add(Arrays.asList("countrymanuel", "Deutschland"));
		paras.add(Arrays.asList("js_active", "1"));
		paras.add(Arrays.asList("rubrik", "0"));
		paras.add(Arrays.asList("stadt_key", "90"));
		paras.add(Arrays.asList("stadtmanuel", "München"));
		paras.add(Arrays.asList("type", "0"));
		List<String> content = dm.doPost("http://www.wg-gesucht.de", paras);

		for (int i = 0; i < content.size(); i++) {
			System.out.println(content.get(i));
		}
	}

}
