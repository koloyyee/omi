package co.loyyee.Omi.Breakfast.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A multithreaded scraping all started.
 * */
public class ScrapeAll {
    final static private Logger log = LoggerFactory.getLogger(ScrapeAll.class);

    public static void main(String[] args) {
        String[] tickers = new String[]{"AAPL", "GOOG", "META", "BRK-A"};
        ArrayList<Finviz> fscrapers = new ArrayList<>();
        ArrayList<Benzinga> bscrapers = new ArrayList<>();

        for(String ticker : tickers) {
            fscrapers.add(new Finviz(ticker));
            bscrapers.add(new Benzinga(ticker));
        }
        List<Finviz> combinedList = new ArrayList<>(fscrapers);
        for(Benzinga b : bscrapers)  {
            try {
                b.getThread().join();
                log.info(b.getTickerNewsList().toString());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

//        for(Finviz f : fscrapers) {
//            try {
//                f.getThread().join();
//                log.info(f.getTickerNewsList().toString());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            }
//        }
    }
}

