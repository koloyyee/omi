package co.loyyee.Omi.Breakfast.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

//@Repository
public class TickerNewsRepository {

	@Autowired
	@Qualifier("breakfastJdbcTemplate")
	final private JdbcTemplate jdbcTemplate;
	final private JdbcClient jdbc;

	public TickerNewsRepository(JdbcTemplate jdbcTemplate, JdbcClient jdbc) {
		this.jdbcTemplate = jdbcTemplate;
		this.jdbc = jdbc;
	}
//	public static TickerNews mapRow(ResultSet rs, int rowNum) throws SQLException {
//		return new TickerNews(
//		);
//	}
}
