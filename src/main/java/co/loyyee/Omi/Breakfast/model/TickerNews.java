package co.loyyee.Omi.Breakfast.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record TickerNews(
		@Id int id,
		String ticker,
		String title,
		String href,
		Outlet outlet,
		String source,
		LocalDateTime issuedDatetime) {
	public TickerNews(String ticker, String title, String href, Outlet outlet, String source, LocalDateTime issuedDatetime) {
		this(0, ticker, title, href, outlet, source, issuedDatetime);
	}
}
