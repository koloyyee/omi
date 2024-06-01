package co.loyyee.Omi.Drafter.service.security;

import co.loyyee.Omi.Drafter.model.CustomUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * To work with Spring Security with our CustomUser
 * <br>
 * We will need this class that implements UserDetailsService so Spring Security can look it up.
 *
 * */
@Service
public class DrafterUserDetailsServiceImpl implements UserDetailsService {
  private static final Logger log = LoggerFactory.getLogger(DrafterUserDetailsServiceImpl.class);

  /**
   * Since we have multiple datasource we will need to specified with @Qualifier
   * */
  @Autowired
  @Qualifier("drafterJdbcClient")
  private JdbcClient jdbcClient;

  public DrafterUserDetailsServiceImpl() {}

  /**
   * This is method from the UserDetailsService
   * <br>
   * We can modify it with loadUserByUsername(String email){}
   * <br>
   * the sql can change to ".....where email = :email", params("email", email")
   *
   * */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    /**
     *  when CustomUser class implement UserDetails will allow skip the setUsername or password stage.
     *  if we are using CustomUser record check the example below
     */
    CustomUser user =
        jdbcClient
            .sql("select * from users where username = :username")
            .param("username", username)
            .query(CustomUser.class)
            .optional()
            .orElseThrow(() -> new RuntimeException("Username or password incorrect"));
    UserDetails customUser = User.withUsername(user.username()).password(user.password()).roles(user.role()).build();
    return customUser;
  }
}

