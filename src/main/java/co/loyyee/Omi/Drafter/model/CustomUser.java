package co.loyyee.Omi.Drafter.model;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUser implements UserDetails {
  private @Id Long id;
  private String username;
  private  String password;
  private String email;
  private boolean enabled;
  private String role;
  
  public CustomUser(Long id, String username, String password, String email, boolean enabled, String role) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.enabled = enabled;
    this.role = role;
  }
  
  public Long getId() {
    return id;
  }
  
  public String getEmail() {
    return email;
  }
  
  public String getRole() {
    return role;
  }
  
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role));
  }
  
  @Override
  public String getPassword() {
    return password;
  }
  
  @Override
  public String getUsername() {
    return username;
  }
  
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
  
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }
  
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
  
  @Override
  public boolean isEnabled() {
    return enabled;
  }
  
  @Override
  public String toString() {
    return "CustomUser{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", email='" + email + '\'' +
        ", enabled=" + enabled +
        ", role='" + role + '\'' +
        '}';
  }
}
