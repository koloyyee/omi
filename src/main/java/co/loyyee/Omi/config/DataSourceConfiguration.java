package co.loyyee.Omi.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.init.DataSourceScriptDatabaseInitializer;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.List;

/**
 * <h2>Configuration for multiple datasources</h2>
 * <hr>
 *
 * <p> Dan Vega's tutorial on how to correctly configure multiple datasources in Spring Boot
 * reference: <a href="https://www.youtube.com/watch?v=ZKYFGuukhT4">Dan Vega's tutorial </a>
 * </p>
 * <br>
 * <h4>This is an application configuration for multiple connection</h4>
 * <em>DatasourceConfiguration</em>
 * <p>notice: inside the @ConfigurationProperties() we will specify the name of the datasource
 * that will be direct we related to the <i>application.yml or application.properties</i>,
 * if this is configuration is not set up the application.yml/.properties will <strong>ONLY</strong>
 * recognize <strong>app.datasource.xxxx</strong>
 *</p>
 *  <div>
 *  e.g.:
 *  [application.properties]
 *   app.datasource.breakfast.url=jdbc:postgresql://localhost:5432/breakfast
 *   app.datasource.breakfast.username=postgres
 *   app.datasource.breakfast.password=password
 *   app.datasource.breakfast.driver-class-name=org.postgresql.Driver
 *   <br>
 *  [application.yml]
 *  app:
 *      datasource:
 *          breakfast:
 *              url: jdbc:postgresql://localhost:5432/breakfast
 *              name: breakfast
 *              username: postgres
 *              password: password
 *              driverName: org.postgresql.Driver
 *    </div>
 *
 * Important Note:
 * @Primary is the annotation that will tell Spring Boot which datasource to use as the default
 * Be specific about @Qualifier is to specify the name of the DataSource, DataSourceProperties, or any other bean
 * */
//@Configuration(proxyBeanMethods = false)
public class DataSourceConfiguration {
    // Breakfast
    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.breakfast")
    public DataSourceProperties breakfastDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean
    @Primary
    public HikariDataSource breakfastDataSource( @Qualifier("breakfastDataSourceProperties") DataSourceProperties breakfastDataSourceProperties) {
        return breakfastDataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean
    DataSourceScriptDatabaseInitializer breakfastDataSourceInitializer(@Qualifier("breakfastDataSource") DataSource datasource) {
        var settings = new DatabaseInitializationSettings();
        // pg settings
        settings.setSchemaLocations(List.of("classpath:breakfast-pg-schema.sql"));
        settings.setMode(DatabaseInitializationMode.ALWAYS);
//        settings.setSchemaLocations(List.of("classpath:breakfast-h2-schema.sql"));
//        settings.setMode(DatabaseInitializationMode.EMBEDDED);
        return new DataSourceScriptDatabaseInitializer(datasource, settings);
    }
    // Programmatically creating a DataSource
//    @Bean
//    public DataSource breakfastDataSource(@Qualifier("breakfastDataSourceProperties") DataSourceProperties breakfastDataSourceProperties) {
//        return DataSourceBuilder.create()
//                .url(breakfastDataSourceProperties.getUrl())
//                .username(breakfastDataSourceProperties.getUsername())
//                .password(breakfastDataSourceProperties.getPassword())
//                .build();
//    }

/**
    // Blog
    @Bean
    @ConfigurationProperties("app.datasource.blog")
    public DataSourceProperties blogDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public HikariDataSource blogDataSource(@Qualifier("blogDataSourceProperties")  DataSourceProperties blogDataSourceProperties) {
        return blogDataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean
    DataSourceScriptDatabaseInitializer blogDataSourceInitializer(@Qualifier("blogDataSource") DataSource datasource) {
        // h2 settings
        var settings = new DatabaseInitializationSettings();
        settings.setSchemaLocations(List.of("classpath:blog-schema.sql"));
        settings.setMode(DatabaseInitializationMode.EMBEDDED);
        return new DataSourceScriptDatabaseInitializer(datasource, settings);
    }
    // Subscriber
    @Bean
    @ConfigurationProperties("app.datasource.subscriber")
    public DataSourceProperties subscriberDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource subscriberDataSource(@Qualifier("subscriberDataSourceProperties") DataSourceProperties subscriberDataSourceProperties) {
        return DataSourceBuilder.create()
                .url(subscriberDataSourceProperties.getUrl())
                .username(subscriberDataSourceProperties.getUsername())
                .password(subscriberDataSourceProperties.getPassword())
                .build();
    }
    @Bean
    DataSourceScriptDatabaseInitializer subscriberDataSourceInitializer(@Qualifier("subscriberDataSource") DataSource datasource) {
        // h2 settings
        var settings = new DatabaseInitializationSettings();
        settings.setSchemaLocations(List.of("classpath:subscriber-schema.sql"));
        settings.setMode(DatabaseInitializationMode.EMBEDDED);
        return new DataSourceScriptDatabaseInitializer(datasource, settings);
    }
*/
}
