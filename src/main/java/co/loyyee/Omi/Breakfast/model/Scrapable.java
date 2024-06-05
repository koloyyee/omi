package co.loyyee.Omi.Breakfast.model;
import java.util.List;

public interface Scrapable<T> extends Runnable{
		List<T> scrape();
		List<T> getList();
	Thread getThread();
}
