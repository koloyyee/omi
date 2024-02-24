package co.loyyee.Omi.Breakfast.service;

import co.loyyee.Omi.Breakfast.model.Outlet;
import co.loyyee.Omi.Breakfast.model.Scraper;
import co.loyyee.Omi.Breakfast.model.TickerNews;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Finviz implements Runnable, Scraper<TickerNews> {
    private List<TickerNews> tickerNewsList;
    private String ticker;

    private Thread thread;
    public Finviz(String ticker) {
        tickerNewsList = new ArrayList<>();
        this.ticker = ticker;
        thread = new Thread(this);
        thread.start();
        thread = Thread.ofVirtual().start(this);
    }
    /**
     * Turning e.g: String Feb-01-24 and 15:33PM into LocalDateTime
     * @param date The string format date e.g.: Feb-01-24
     * @param time The string format time e.g.: 03:33PM
     * @return a format to LocalDateTime as issuedDate
     * */
    private LocalDateTime formatDateTime(String date, String time ) {
            LocalDate lDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MMM-dd-yy", Locale.ENGLISH));
            LocalTime lTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mma",  Locale.ENGLISH));
            return lDate.atTime(lTime);
    }
    public List<TickerNews> scrape() {
        StringBuilder sb = new StringBuilder();
        sb.append("https://finviz.com/quote.ashx?t=");
        String ending = "&p=d";
        sb.append(ticker);
        sb.append(ending);
        String url = sb.toString();
        try {
            Document document = Jsoup.connect(url).get();
            Elements newsTable = document.select("table#news-table");
            Elements trs = newsTable.select("tr");

            /** Obstacles:
             * The news date come is a string "[date] [time]"
             * but if the news is on the same date it will only show the time.
             * e.g.:
             * full - "Dec-01-23 05:53PM"
             * same date different time: "06:00AM"
             * **/
            String date = ""; // hold this in memory but outside of loop ready to be mutated.
            LocalDateTime issuedDateTime = null;
            for (Element tr : trs) {
                Element a = tr.select("a").first();
                String href = a.attr("href");
                String title = a.html();

                Elements td = tr.select("td[align='right']");
                for (Element dates : td) {
                    String[] datetime = dates.text().split(" ");
                    /** Handle full date */
                    if (datetime.length > 1) {
                        date = datetime[0];
                        if (date.contains("Today")) {
                            date = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM-dd-yy"));
                        }
                        issuedDateTime = formatDateTime(date, datetime[1]);
                    } else {
                        /** Handle time only */
                        issuedDateTime = formatDateTime(date, datetime[0]);
                    }
                }
                TickerNews tickerNews = new TickerNews(ticker, title, href, Outlet.FINVIZ, "none", issuedDateTime);
                tickerNewsList.add(tickerNews);

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getTickerNewsList();
    }

    public String getTicker() {
        return ticker;
    }

    public Thread getThread() {
        return thread;
    }

    public List<TickerNews> getTickerNewsList() {
        return tickerNewsList;
    }

    @Override
    public void run() {
        scrape();
    }

//    public static void main(String[] args) {
//        Finviz a = new Finviz("GOOG");
//        a.scrape();
//        System.out.println(a.getTickerNews());
//    }
}