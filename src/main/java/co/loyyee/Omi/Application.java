package co.loyyee.Omi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;


//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@SpringBootApplication
public class Application {

    private final static Logger log = LoggerFactory.getLogger(Application.class);

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
    CommandLineRunner envRunner(){
        return args -> {
            System.out.println("Environment is running");
            System.out.printf ("jdbc:postgresql://%s:%s/%s%n", System.getenv("PGHOST"), System.getenv("PGPORT"), System.getenv("PGDATABASE"));
            System.out.println("BRKF: " + System.getenv("PGBREAKFAST"));
            System.out.println("MESURE: " + System.getenv("PGMESURE"));
        };
    }
    @Bean
    CommandLineRunner blogDsRunner(
//            @Qualifier("blogDataSource") DataSource blogDataSource,
//            @Qualifier("subscriberDataSource") DataSource subscriberDataSource,
            @Qualifier("breakfastDataSource") DataSource breakfastDataSource,
             DataSource dataSource
    ) {
        return args -> {
//            System.out.println("blogDataSource DataSource is = " + blogDataSource.getConnection().getMetaData().getURL());
//            System.out.println("subscriberDataSource DataSource is = " + subscriberDataSource.getConnection().getMetaData().getURL());
            System.out.println("breakfastDataSource DataSource is = " + breakfastDataSource.getConnection().getMetaData().getURL());
            System.out.println("dataSource DataSource is = " + dataSource.getConnection().getMetaData().getURL());
//            System.out.println("OPENAI: " + System.getenv("OPENAI_KEY"));
        };
    }

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
