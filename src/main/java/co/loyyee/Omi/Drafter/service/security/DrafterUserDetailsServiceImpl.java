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

@Service
public class DrafterUserDetailsServiceImpl implements UserDetailsService {
  private final static Logger log = LoggerFactory.getLogger(DrafterUserDetailsServiceImpl.class);

  @Autowired
  @Qualifier("drafterJdbcClient")
  private JdbcClient jdbcClient;

  public DrafterUserDetailsServiceImpl() {
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info(username);
    CustomUser customUser =
				jdbcClient
						.sql("select * from users where username = :username")
						.param("username", username)
						.query(CustomUser.class)
						.optional()
						.orElseThrow(() -> new RuntimeException("Username or password incorrect"));
      log.info("{}", customUser);
    UserDetails userDetails = User
        .withUsername(customUser.getUsername()).password(customUser.getPassword()).roles(customUser.getRole()).build();
    log.info("{}", userDetails);
    return userDetails;
  }
  
}
