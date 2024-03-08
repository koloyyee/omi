package co.loyyee.Omi.Breakfast.repository;

import co.loyyee.Omi.Breakfast.model.CompanyTicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Repository for CRUD of public company ticker
 * CIK
 * Ticker
 * Title
 * <p>
 * We are using JDBC Client to simply the CRUD processes but still using SQL.
 */
//@Repository
public class CompanyTickerRepository {

    final private static Logger log = LoggerFactory.getLogger(CompanyTickerRepository.class);
    final private JdbcTemplate jdbcTemplate;
    final private JdbcClient jdbc;

    public CompanyTickerRepository(@Qualifier("breakfastJdbcTemplate") JdbcTemplate jdbcTemplate, @Qualifier("breakfastJdbcClient") JdbcClient jdbc) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbc = jdbc;
    }

    public static CompanyTicker mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CompanyTicker(
                rs.getInt("id"),
                rs.getString("cik_str"),
                rs.getString("ticker"),
                rs.getString("title"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
    }

    public List<CompanyTicker> findAll() {
//        String sql = "SELECT * FROM CompanyTicker"; // jdbcTemplate
//        return jdbcTemplate.query(sql, CompanyTickerRepository::mapRow); // jdbcTemplate
        return jdbc.sql("SELECT * FROM CompanyTicker").query(CompanyTicker.class).list();
    }

    public Optional<CompanyTicker> findByCIK(String cikString) {
//        String sql = "SELECT * FROM CompanyTicker WHERE cik = ?";// jdbcTemplate
//        return jdbcTemplate.queryForObject(sql, CompanyTickerRepository::mapRow, cik); // jdbcTemplate
        return jdbc.sql("SELECT * FROM CompanyTicker WHERE cik_str = :cik_str")
                .param(cikString)
                .query(CompanyTicker.class)
                .optional();

    }

    public Optional<CompanyTicker> findByTicker(String ticker) {
//        String sql = "SELECT * FROM CompanyTicker WHERE ticker = ?"; // jdbcTemplate
//        return jdbcTemplate.queryForObject(sql, CompanyTickerRepository::mapRow, ticker);
        return jdbc.sql("SELECT * FROM CompanyTicker WHERE ticker = :ticker")
                .param(ticker)
                .query(CompanyTicker.class)
                .optional();
    }

    public Optional<CompanyTicker> findByTitle(String title) {
//        String sql = "SELECT * FROM CompanyTicker WHERE title= ?"; // jdbcTemplate
//        return jdbcTemplate.queryForObject(sql, CompanyTickerRepository::mapRow, title);
        return jdbc.sql("SELECT * FROM CompanyTicker WHERE ticker = :title")
                .param(title)
                .query(CompanyTicker.class)
                .optional();
    }

    public int save(CompanyTicker companyTicker) {
//        String sql = "INSERT INTO CompanyTicker VALUES ( ?, ?, ?, ?)"; // jdbcTemplate
//        return jdbcTemplate.update(sql, companyTicker.cik(), companyTicker.ticker(), companyTicker.title());

        int numOfRowsAffected = jdbc.sql(
                        """
                                INSERT INTO CompanyTicker
                                ( cik_str, ticker, title)
                                VALUES (:cik_str , :ticker, :title)
                                """)
                .param(companyTicker.cikStr())
                .param(companyTicker.ticker())
                .param(companyTicker.title())
                .update();


        log.info(numOfRowsAffected + " rows affected");
        return numOfRowsAffected;
    }

    /**
     * saveAll method to save all the company tickers
     * piping company tickers from JSON to database
     * <p>
     * Using JDBC Template batchUpdate since JDBC Client does not support batchUpdate.
     *
     * @param tickers - list of company tickers
     * @see JdbcTemplate#batchUpdate(String, Collection, int, ParameterizedPreparedStatementSetter)
     * @see CompanyTicker
     */
    @Transactional
    public void saveAll(List<CompanyTicker> tickers) {

        /** h2 **/
        String hrSql = """
                INSERT INTO CompanyTicker 
                ( cik_str, ticker, title, created_at ) 
                VALUES (?, ?, ?, CURRENT_TIMESTAMP() )
                """;
        String pgSql = """
                INSERT INTO CompanyTicker 
                ( cik_str, ticker, title, created_at ) 
                VALUES (?, ?, ?, NOW() )
                """;

        jdbcTemplate.batchUpdate(
                pgSql,
                tickers,
                tickers.size(),
                (ps, ticker) -> {
                    ps.setString(1, ticker.cikStr());
                    ps.setString(2, ticker.ticker());
                    ps.setString(3, ticker.title());
                }
        );
    }
}


