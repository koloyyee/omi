package co.loyyee.Omi.Breakfast.repository;

import co.loyyee.Omi.Breakfast.model.TickerNews;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TickerNewsRepository {

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
