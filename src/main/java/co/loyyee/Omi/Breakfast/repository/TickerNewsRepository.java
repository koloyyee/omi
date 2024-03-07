package co.loyyee.Omi.Breakfast.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

//@Repository
public class TickerNewsRepository {

	@Qualifier("breakfastJdbcTemplate")
	final private JdbcTemplate jdbcTemplate;
	@Qualifier("breakfastJdbcClient")
	final private JdbcClient jdbc;

	public TickerNewsRepository(@Qualifier("breakfastJdbcTemplate") JdbcTemplate jdbcTemplate, @Qualifier("breakfastJdbcClient") JdbcClient jdbc) {
		this.jdbcTemplate = jdbcTemplate;
		this.jdbc = jdbc;
	}
//	public static TickerNews mapRow(ResultSet rs, int rowNum) throws SQLException {
//		return new TickerNews(
//		);
//	}
}
