package co.loyyee.YuenDim.Breakfast.model;

import java.time.LocalDateTime;

public record TickerNews(String ticker, String title, String href, Outlet outlet, String source, LocalDateTime issuedDatetime) {

}