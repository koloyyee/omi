package co.loyyee.Omi;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
//@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /*** Breakfast JdbcClient and JdbcTemplate ***/
    @Bean
    JdbcClient breakfastJdbcClient(@Qualifier("breakfastDataSource") DataSource dataSource) {
        return JdbcClient.create(dataSource);
    }

    @Bean
    public JdbcTemplate breakfastJdbcTemplate(@Qualifier("breakfastDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

//    @Bean
//    CommandLineRunner envRunner(){
//        return args -> {
//            System.out.println("Environment is running");
//            System.out.println(System.getenv("PGHOST"));
//            System.out.println(System.getenv("PGPORT"));
//            System.out.println(System.getenv("PGDATABASE"));
//            System.out.println(System.getenv("PGBREAKFAST"));
//            System.out.println(System.getenv("PGMESURE"));
//        };
//    }

    /**
     * The correct way of creating a JdbcClient from Dan Vega Tutorial
     @Bean JdbcClient blogJdbcClient(@Qualifier("blogDataSource") DataSource dataSource) {
     return JdbcClient.create(dataSource);
     }

     @Bean JdbcClient subscriberJdbcClient(@Qualifier("subscriberDataSource") DataSource dataSource) {
     return JdbcClient.create(dataSource);
     }
     */

    /* CommandLineRunner to test the connection
    @Bean
    CommandLineRunner commandLineRunner(PostService postService, SubscriberService subscriberService) {
        return args -> {
            List<Post> posts = postService.findAll();
            System.out.println(posts);
            System.out.println("Subscribers: " + subscriberService.findAll());

        };
    }

    @Bean
    CommandLineRunner blogDsRunner(
            @Qualifier("blogDataSource") DataSource blogDataSource,
            @Qualifier("subscriberDataSource") DataSource subscriberDataSource,
            @Qualifier("breakfastDataSource") DataSource breakfastDataSource
    ) {
        return args -> {
            System.out.println("blogDataSource DataSource is = " + blogDataSource.getConnection().getMetaData().getURL());
            System.out.println("subscriberDataSource DataSource is = " + subscriberDataSource.getConnection().getMetaData().getURL());
            System.out.println("breakfastDataSource DataSource is = " + breakfastDataSource.getConnection().getMetaData().getURL());
        };
    }
*/
}
