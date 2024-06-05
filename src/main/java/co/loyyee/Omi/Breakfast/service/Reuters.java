package co.loyyee.Omi.Breakfast.service;

import co.loyyee.Omi.Breakfast.model.Scrapable;
import co.loyyee.Omi.Breakfast.model.TickerNews;

import java.util.List;

public class Reuters implements Scrapable<TickerNews> {
	@Override
	public List<TickerNews> scrape() {
		return List.of();
	}
	
	@Override
	public List<TickerNews> getList() {
		return List.of();
	}
	
	@Override
	public Thread getThread() {
		return null;
	}
	
	/**
	 * Runs this operation.
	 */
	@Override
	public void run() {
	
	}
}
