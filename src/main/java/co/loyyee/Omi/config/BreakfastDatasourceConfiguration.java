package co.loyyee.Omi.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * Configuration for multiple datasource connection.
 * source: https://www.baeldung.com/spring-boot-configure-multiple-datasources
 *
 * This is an application configuration for multiple connection
 * BreakfastDatasourceConfiguration
 * notice: inside the @ConfigurationProperties() we will specify the name of the datasource
 * that will be direct we related to the <i>application.yml or application.properties</i>,
 * if this is configuration is not set up the application.yml/.properties will <strong>ONLY</strong>
 * recognize spring.datasource.url.
 *
 * e.g.:
 * [application.properties]
 * spring.datasource.breakfast.url = xxxxx
 * [application.yml]
 * spring:
 *  datasource:
 *      breakfast:
 *          url: xxxxxxx
 * */
@Configuration
public class BreakfastDatasourceConfiguration{
    @Bean
    @ConfigurationProperties("spring.datasource.breakfast")
    public DataSourceProperties breakfastDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource breakfastDataSource() {
        return breakfastDataSourceProperties().initializeDataSourceBuilder().build();
    }


    @Bean
    public JdbcTemplate breakastJdbcTemplate(@Qualifier("breakfastDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


    /**
     * schema.sql will not populate the H2 when there are multiple data source,
     * unless is primary (e.g.: sprint.datasource.url=xxxxxx)
     * We need to specify which data source and which .sql file.
     *
     * source: https://stackoverflow.com/questions/51146269/spring-boot-2-multiple-datasources-initialize-schema
     * */
    @Bean
    public DataSourceInitializer breakfastDataSourceInitializer(@Qualifier("breakfastDataSource") DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        /**  filename coherent with your .sql, in this case breakfast **/
        /** h2 **/
//        resourceDatabasePopulator.addScript(new ClassPathResource("schema-h2-breakfast.sql"));
        /** postgres **/
        resourceDatabasePopulator.addScript(new ClassPathResource("schema-pg-breakfast.sql"));

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }
}

