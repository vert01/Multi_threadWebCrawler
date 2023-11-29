package OSFinal;

import java.util.ArrayList;

public class MainCrawler {
	
	public static void main(String[] args) {
		//Link List for Web Crawler
		ArrayList<MultiThreadCrawler> link = new ArrayList<>();
		link.add(new MultiThreadCrawler("https://www.mayoclinic.org", 1));
		link.add(new MultiThreadCrawler("https://www.npr.org", 2));
		link.add(new MultiThreadCrawler("https://www.britannica.com", 3));
		link.add(new MultiThreadCrawler("https://www.nih.gov", 4));
		link.add(new MultiThreadCrawler("https://www.espn.com", 5));
		link.add(new MultiThreadCrawler("https://www.webmd.com", 6));
		link.add(new MultiThreadCrawler("https://www.nypost.com", 7));
		link.add(new MultiThreadCrawler("https://www.npr.org", 8));
		link.add(new MultiThreadCrawler("https://www.nytimes.com", 9));
		link.add(new MultiThreadCrawler("https://www.wikihow.com", 10));
	
		for(MultiThreadCrawler crawl : link) {
			try { 
				crawl.getThread().join();
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
