package co.loyyee.Omi.config;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

  @Bean
  SecurityFilterChain drafterSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(auth -> 
            auth
//                .requestMatchers("/drafter/upload/**").permitAll()
                .requestMatchers("/drafter/**").authenticated()
                .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .formLogin(Customizer.withDefaults())
        .build();
  }

  /**
   * Create default user. <br>
   * Register user will be done in the similar fashion but UserDTO or User Model is needed.
   */
  @Bean
  JdbcUserDetailsManager drafterJdbcUserDetailsManager(
      @Qualifier("appliedDataSource") DataSource dataSource) throws SQLException {

    JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
    if (!manager.userExists("david")) {
      UserDetails admin =
          User.withUsername("david").password(encoder().encode("password")).roles("ADMIN").build();
      manager.createUser(admin);
    }

    return manager;
  }

  @Bean
  PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
}
