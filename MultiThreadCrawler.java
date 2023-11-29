package OSFinal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.List;

public class MultiThreadCrawler implements Runnable {

    private static final int MAX_DEPTH = 3;
    private Thread thread;
    private String firstLink;
    private VisitedUrlManager visitedUrlManager;
    private int ID;

    public MultiThreadCrawler(String link, int num) {
        System.out.println("Crawler created!");
        firstLink = link;
        ID = num;

        visitedUrlManager = new VisitedUrlManager();
        visitedUrlManager.loadVisitedUrls();

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        crawl(1, firstLink);
        synchronized (visitedUrlManager) {
            visitedUrlManager.saveVisitedUrls();
        }
    }

    private void crawl(int level, String url) {
        if (level > MAX_DEPTH) {
            return;
        }

        Document doc = request(url);

        if (doc != null) {
            for (Element link : doc.select("a[href]")) {
                String nextLink = link.absUrl("href");
                if (!visitedUrlManager.isVisited(nextLink)) {
                    crawl(level + 1, nextLink);
                }
            }
        }
    }

    private Document request(String url) {
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            if (con.response().statusCode() == 200) {
                System.out.println("\n**Bot ID:" + ID + " Received Webpage at " + url);

                String title = doc.title();
                System.out.println(title);
                visitedUrlManager.addVisitedUrl(url);

                return doc;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Thread getThread() {
        return thread;
    }

    public List<String> getVisitedUrls() {
        return visitedUrlManager.getVisitedUrls();
    }
}

class VisitedUrlManager {
    private static final String VISITED_URLS_FILE = "visited_urls.txt";
    private List<String> visitedUrls;

    public VisitedUrlManager() {
        visitedUrls = new ArrayList<>();
    }

    public void loadVisitedUrls() {
        try {
            File file = new File(VISITED_URLS_FILE);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        visitedUrls.add(line);
                    }
                }
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void saveVisitedUrls() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VISITED_URLS_FILE))) {
            for (String url : visitedUrls) {
                writer.write(url);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addVisitedUrl(String url) {
        visitedUrls.add(url);
    }

    public boolean isVisited(String url) {
        return visitedUrls.contains(url);
    }

    public List<String> getVisitedUrls() {
        return new ArrayList<>(visitedUrls);
    }
}

