package co.loyyee.Omi.Breakfast.service;

import co.loyyee.Omi.Breakfast.model.Scrapable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * A multithreaded scraping all started.
 */
public class ScrapeAll {
    final static private Logger log = LoggerFactory.getLogger(ScrapeAll.class);

    public static void main(String[] args) {
        String[] tickers = new String[] { "AAPL", "GOOG", "META", "BRK-A" };
        ArrayList<Scrapable> scrapList = new ArrayList<>();

        for (String ticker : tickers) {
            scrapList.add(new Finviz(ticker));
            scrapList.add(new Benzinga(ticker));
        }
        for ( Scrapable item : scrapList) {
            
            try {
                item.getThread().join();
                log.info(item.getList().toString());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void vThread() throws InterruptedException, ExecutionException {
        String[] tickers = new String[] { "AAPL", "GOOG", "META", "BRK-A" };
        List<Callable<Finviz>> fscrapers = new ArrayList<>();
        List<Callable<Benzinga>> bscrapers = new ArrayList<>();

        for (String ticker : tickers) {
            fscrapers.add(() -> new Finviz(ticker));
            bscrapers.add(() -> new Benzinga(ticker));
        }

        ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();
        for (Future<Finviz> f : service.invokeAll(fscrapers)) {
            Finviz finviz = f.get();
            finviz.getList().forEach(System.out::println);
        }
    }
}
