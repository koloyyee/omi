package co.loyyee.Omi.Breakfast.model;
import java.util.List;
public interface Scraper<T>{
		List<T> scrape();
		List<T> getTickerNewsList();
}
