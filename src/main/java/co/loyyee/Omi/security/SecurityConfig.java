package co.loyyee.Omi.security;

import co.loyyee.Omi.Drafter.security.DrafterRsaKeyProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This the JWT Spring Security setup based on the tutorial by Dan Vega. <hr> Spring Security - How
 * to authenticate with username and password <br>
 * <a href="https://www.youtube.com/watch?v=UaB-0e76LdQ">Dan's Tutorial </a>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
  private final DrafterRsaKeyProperties drafterRsaKey;
  private RSAKey rsaKey;

  public SecurityConfig(DrafterRsaKeyProperties drafterRsaKey) {
    this.drafterRsaKey = drafterRsaKey;
  }

  @Bean
  public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    return new ProviderManager(authProvider);
  }

  @Bean
  SecurityFilterChain drafterSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/drafter/private/auth/token")
                    .permitAll()
                    .requestMatchers("/drafter/public/**")
                    .permitAll()
                    .requestMatchers("/drafter/private/**")
                    .authenticated()
            //                .anyRequest().authenticated()
            )
        .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        //        .httpBasic(Customizer.withDefaults())
        //        .formLogin(Customizer.withDefaults())
        .build();
  }

  /**
   * Create default user. <br>
   * Register user will be done in the similar fashion but UserDTO or User Model is needed.
   */
  @Bean
  JdbcUserDetailsManager drafterDefaultUser(@Qualifier("drafterDataSource") DataSource dataSource)
      throws SQLException {

    JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
    if (!manager.userExists("david")) {
      UserDetails admin =
          User.withUsername("david")
              .password(passwordEncoder().encode("password"))
              .roles("ADMIN")
              .build();
      manager.createUser(admin);
    }

    return manager;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * This is the programmatic approach <br>
   * <a
   * href="https://github.com/danvega/jwt-username-password/blob/master/src/main/java/dev/danvega/jwt/security/SecurityConfig.java">
   * SecurityConfig reference</a>
   */
  @Bean
  public JWKSource<SecurityContext> jwkSource() {
    rsaKey = Jwks.generateRsa();
    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  @Bean
  JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks) {
    return new NimbusJwtEncoder(jwks);
  }

  @Bean
      JwtDecoder jwtDecoder() throws JOSEException {
    return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
  }
  /**
   * JWT setup tutorial <br>
   * <a href="https://www.danvega.dev/blog/spring-security-jwt">Dan Vega's Spring Security JWT
   * (backend only)</a> <br>
   * This is the non-programmatic approach. it is replaced new programmatic approach above.
   */
  //  @Bean
  @Deprecated
  JwtDecoder jwtDecoder(String deprecated) {
    return NimbusJwtDecoder.withPublicKey(drafterRsaKey.publicKey()).build();
  }

  //  @Bean
  @Deprecated
  JwtEncoder jwtEncoder() {
    JWK jwk =
        new RSAKey.Builder(drafterRsaKey.publicKey())
            .privateKey(drafterRsaKey.privateKey())
            .build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }
}
