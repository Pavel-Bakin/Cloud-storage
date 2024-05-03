package org.example.cloudstorage.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = SecurityConfig.class)
@Import(SecurityConfig.class)
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManagerBuilder auth;

    @Test
    public void accessToPublicResourcesShouldBePermittedWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/public/example"))
                .andExpect(status().isOk());
    }

    @Test
    public void loginPageAccessibleToAll() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void accessToSecuredResourceWithoutAuthenticationShouldFail() throws Exception {
        mockMvc.perform(get("/secured/example"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void authenticationWithValidUserShouldSucceed() throws Exception {
        mockMvc.perform(formLogin().user("username").password("password"))
                .andExpect(authenticated());
    }

    @Test
    public void authenticationWithInvalidUserShouldFail() throws Exception {
        mockMvc.perform(formLogin().user("invalid").password("invalid"))
                .andExpect(unauthenticated());
    }

    @Test
    public void logoutShouldSucceed() throws Exception {
        mockMvc.perform(logout())
                .andExpect(unauthenticated());
    }
}
