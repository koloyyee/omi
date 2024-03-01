package co.loyyee.Omi;

import co.loyyee.Omi.config.DrafterConfigurationProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(DrafterConfigurationProperties.class)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	CommandLineRunner runner() {
		return args -> {
			System.out.println(System.getenv("PGHOST"));
			System.out.println(System.getenv("PGPORT"));
			System.out.println(System.getenv("PGDATABASE"));
			System.out.println(System.getenv("PGUSER"));
			System.out.println(System.getenv("PGPASSWORD"));
			System.out.println(System.getenv("PGBREAKFAST"));
			System.out.println(System.getenv("PGMESURE"));
		};
	}

}
