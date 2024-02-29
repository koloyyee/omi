package co.loyyee.Omi.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * For reference why this configuration is needed:
 * @see co.loyyee.Omi.config.BreakfastDatasourceConfiguration
 *
 * Opt out by commenting out the @Configuration annotation.
 * */
@Configuration
public class MesureDatasourceConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.mesure")
    public DataSourceProperties mesureDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Qualifier
    public DataSource mesureDataSource() {
        return mesureDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public JdbcTemplate mesureJdbcTemplate(@Qualifier("mesureDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Why we need an initializer? see reference.
     * @see co.loyyee.Omi.config.BreakfastDatasourceConfiguration#breakfastDataSourceInitializer(DataSource)
     * */
    @Bean(name="mesureDataSourceInitializer")
    public DataSourceInitializer mesureDataSourceInitializer(@Qualifier("mesureDataSource") DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
//        resourceDatabasePopulator.addScript(new ClassPathResource("schema-h2-mesure.sql"));
//        resourceDatabasePopulator.addScript(new ClassPathResource("schema-pg-mesure.sql"));

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }
}
