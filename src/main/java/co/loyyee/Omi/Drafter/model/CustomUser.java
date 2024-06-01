package co.loyyee.Omi.Drafter.model;


import org.springframework.data.annotation.Id;
import org.springframework.lang.NonNull;

/**
 * To create a customer user but work with Spring Security
 * we will need to a "CustomUser" we can either create a record or class
 * <br>
 * For record won't be able to implement UserDetails so at
 * {@link co.loyyee.Omi.Drafter.service.security.DrafterUserDetailsServiceImpl}
 * we will need to setUsername find the example at the bottom of the DrafterUserDetailsServiceImpl
 */
  public record CustomUser(@Id Long id, @NonNull String username, String password, String email, boolean enabled, String role) {
}
