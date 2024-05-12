package co.loyyee.Omi.Drafter.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.loyyee.Omi.Drafter.service.security.TokenService;
import co.loyyee.Omi.config.DataSourceConfiguration;
import co.loyyee.Omi.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * This is a Mock test for the private auth endpoints<br>
 * for the @Import since we have more than 1 data source, the @Qualifier couldn't find
 * drafterDatasource unless we include the DataSourceConfiguration class
 */
@WebMvcTest({DraftPrivateController.class, AuthController.class})
@Import({DataSourceConfiguration.class, SecurityConfig.class, TokenService.class})
class DraftPrivateControllerTest {

  @Autowired MockMvc mvc;

  @Test
  void shouldGetUnauthorizedWithPrivateRoot() throws Exception {
    this.mvc.perform(get("/drafter/private")).andExpect(status().isUnauthorized());
  }

  @Test
  void shouldWelcomeBackPrincipalIsHereAfterAuthenticated() throws Exception {

    MvcResult result =
        this.mvc
            .perform(post("/drafter/private/auth/token").with(httpBasic("david", "password")))
            .andExpect(status().isOk())
            .andReturn();
    String token = result.getResponse().getContentAsString();
    this.mvc
        .perform(get("/drafter/private").header("Authorization", "Bearer " + token))
        .andExpect(content().string("Welcome back david"));
  }

  @Test
  @WithMockUser
  public void rootWithMockUserStatusIsOK() throws Exception {
    this.mvc.perform(get("/drafter/private")).andExpect(status().isOk());
  }
}
