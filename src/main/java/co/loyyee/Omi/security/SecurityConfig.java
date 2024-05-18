package co.loyyee.Omi.security;

import co.loyyee.Omi.Drafter.model.CustomUser;
import co.loyyee.Omi.Drafter.security.DrafterRsaKeyProperties;
import co.loyyee.Omi.Drafter.service.security.DrafterUserDetailsServiceImpl;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.sql.SQLException;
import java.util.List;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
//  private final DrafterUserDetailsServiceImpl userDetailsService;
  private RSAKey rsaKey; // for programmatic approach
  private final String frontendUrl = System.getenv("FRONTEND_URL");

  public SecurityConfig(DrafterRsaKeyProperties drafterRsaKey) {
    this.drafterRsaKey = drafterRsaKey;
	}

  @Bean
  SecurityFilterChain drafterSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/drafter/private/auth/token")
                    .permitAll()
                    .requestMatchers("/drafter/public/**")
                    .permitAll()
                    .requestMatchers("/drafter/private/**")
                    .authenticated()
            )
        .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        //        .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
        .build();
  }

  /**
   * Create default user. <br>
   * Register user will be done in the similar fashion but UserDTO or User Model is needed.
   */
//  @Bean
  JdbcUserDetailsManager drafterDefaultUser(@Qualifier("drafterDataSource") DataSource dataSource)
      throws SQLException {
    JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
    if (!manager.userExists("david")) {
      UserDetails defaultAdmin = new CustomUser(
          null,
          "david",
          passwordEncoder().encode("password"),
          "admin@example.com",
          true,
          "ADMIN"
      );
      //      UserDetails admin =
      //          User.withUsername(defaultAdmin.getUsername())
      //              .password(defaultAdmin.getPassword())
      //              .roles(defaultAdmin.getRole())
      //              .build();
      manager.createUser(defaultAdmin);
    }

    return manager;
  }

  /**
   * This method is return a UserDetailService based on our JdbcUserDetailsManager
   * */
//  @Bean
//  public UserDetailsService userDetailsService(
//      @Qualifier("drafterDataSource") DataSource dataSource) {
//    JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
//
//    // Optional: customize SQL queries
//    manager.setUsersByUsernameQuery(
//        "SELECT username, password, enabled FROM users WHERE username = ?");
//    manager.setAuthoritiesByUsernameQuery(
//        "SELECT username, authority FROM authorities WHERE username = ?");
//
//    return manager;
//  }
/**
 * Own implementation still work on.
 * */
  @Bean
  public DrafterUserDetailsServiceImpl drafterUserDetailsService() {
    return new DrafterUserDetailsServiceImpl() ;
  }
  

  @Bean
  PasswordEncoder passwordEncoder() {
    // when decoding, the password need to start with an id, in this case {bcrypt}
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(@Qualifier("drafterUserDetailsService") UserDetailsService userDetailsService) {
    var authProvider = new DaoAuthenticationProvider();
//    authProvider.setUserDetailsService(this.userDetailsService);
    authProvider.setUserDetailsService(userDetailsService);
    return new ProviderManager(authProvider);
  }



  /**
   * JWT setup tutorial <br>
   * <a href="https://www.danvega.dev/blog/spring-security-jwt">Dan Vega's Spring Security JWT
   * (backend only)</a> <br>
   * This is the non-programmatic approach. it is replaced new programmatic approach above.
   */
    @Bean
  JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(drafterRsaKey.publicKey()).build();
  }

    @Bean
  JwtEncoder jwtEncoder() {
    JWK jwk =
        new RSAKey.Builder(drafterRsaKey.publicKey())
            .privateKey(drafterRsaKey.privateKey())
            .build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }
  
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
//    configuration.setAllowedOrigins(List.of(this.frontendUrl));
    configuration.setAllowedOrigins(List.of(frontendUrl, "*"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
  
  /**
   * This is the programmatic approach <br>
   * <a
   * href="https://github.com/danvega/jwt-username-password/blob/master/src/main/java/dev/danvega/jwt/security/SecurityConfig.java">
   * SecurityConfig reference</a>
   */
//  @Bean
//  public JWKSource<SecurityContext> jwkSource() {
//    rsaKey = Jwks.generateRsa();
//    JWKSet jwkSet = new JWKSet(rsaKey);
//    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
//  }

//  @Bean
//  JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwks) {
//    return new NimbusJwtEncoder(jwks);
//  }

//  @Bean
//  JwtDecoder jwtDecoder() throws JOSEException {
//    return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
//  }
}
