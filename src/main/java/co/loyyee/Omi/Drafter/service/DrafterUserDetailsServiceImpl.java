package co.loyyee.Omi.Drafter.service;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@Service
public class DrafterUserDetailsServiceImpl implements UserDetailsService {
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
    return builder.build();
  }
}
