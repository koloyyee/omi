package co.loyyee.YuenDim.Breakfast.service;

import co.loyyee.YuenDim.Breakfast.model.Outlet;
import co.loyyee.YuenDim.Breakfast.model.TickerNews;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Benzinga implements Runnable {
    private List<TickerNews> tickerNewsList;
    private String ticker;
    private Thread thread;

    public Benzinga(String ticker) {
        tickerNewsList = new ArrayList<>();
        this.ticker = ticker;
        thread = new Thread(this);
        thread.start();
    }

    private LocalDate formatDate(String date) {
        DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.ENGLISH);
        LocalDate localDate = LocalDate.parse(date, parseFormatter);
        return localDate;
    }
/**
 * edge cases: Aug 30, 2023, 11:58AM
 * */
    private LocalDateTime recentPastToMins(String recentPast) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        String[] split = recentPast.split(" ");
        int whileAgo;
        try{
            whileAgo = Integer.parseInt(split[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
           whileAgo  = 0;
        }
        String time = split[1];
        LocalDateTime result = switch (time) {
            case "day", "days" -> currentDateTime.minusDays(whileAgo);
            case "hour", "hours" -> currentDateTime.minusHours(whileAgo);
            default -> currentDateTime.minusMinutes(whileAgo);
        };
        return result;
    }

    public List<TickerNews> scrape(String ticker) {
        if (ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker cannot be empty");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("https://www.benzinga.com/quote/");
        sb.append(ticker);
        sb.append("/news");
        String url = sb.toString();

        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla")
                    .get();
            Elements headlines = document.select("a.content-headline");

            for (Element element : headlines) {
                String href = element.attr("href");
                String title = element.select("div.content-title").first().text();
                String[] contentFooter = element.select("div.text-gray-400.text-sm").text().trim().split("-");
                String source = contentFooter[0].trim();
                LocalDateTime issuedDate = recentPastToMins(contentFooter[1].trim());

                TickerNews tickerNews = new TickerNews(ticker, title, href, Outlet.BENZINGA, source, issuedDate);
                tickerNewsList.add(tickerNews);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getTickerNewsList();
    }


    /**
     * Reference to parallelStream and multithreaded scraping
     * source: https://stackoverflow.com/questions/63022139/web-scraping-using-multithreading/63022373#63022373
     * */
    public List<TickerNews> streamScrape() {
        if (ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker cannot be empty");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("https://www.benzinga.com/quote/");
        sb.append(ticker);
        sb.append("/news");
        String url = sb.toString();

        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla")
                    .get();
            Elements headlines = document.select("a.content-headline");

            tickerNewsList = headlines.parallelStream()
                    .map(element -> {
                        String href = element.attr("href");
                        String title = element.select("div.content-title").first().text();
                        String[] contentFooter = element.select("div.text-gray-400.text-sm").text().trim().split("-");
                        String source = contentFooter[0].trim();
                        LocalDateTime issuedDate = recentPastToMins(contentFooter[1].trim());
                        TickerNews tickerNews = new TickerNews(ticker, title, href, Outlet.BENZINGA, source, issuedDate);
                        return tickerNews;
                    }).collect(Collectors.toList());
            return getTickerNewsList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TickerNews> getTickerNewsList(){
        return tickerNewsList;
    }

    public String getTicker() {
        return ticker;
    }

    public Thread getThread() {
        return thread;
    }

    @Override
    public void run() {
        streamScrape();
    }

    public static void main(String[] args) {
        Benzinga b = new Benzinga("AAPL");
        System.out.println(b.getTickerNewsList());

    }

}
