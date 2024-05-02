package co.loyyee.Omi.Drafter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@Service
public class DrafterUserDetailsServiceImpl implements UserDetailsService {
  private final static Logger log = LoggerFactory.getLogger(DrafterUserDetailsServiceImpl.class);
  private final JdbcClient jdbcClient;

  public DrafterUserDetailsServiceImpl(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        jdbcClient
            .sql("select * from users where username=:username")
            .param("username", username)
            .query(User.class)
            .single();

    User.UserBuilder builder = null;
    if (user != null) {
      builder =
          User.withUsername(username)
              .password(new BCryptPasswordEncoder().encode(user.getPassword()))
              .roles(String.valueOf(user.getAuthorities()));
      }
    log.info(builder.build().toString());
    return builder.build();
  }
}
