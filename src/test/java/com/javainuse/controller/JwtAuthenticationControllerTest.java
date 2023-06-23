package com.javainuse.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javainuse.config.JwtTokenUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

/**
 * Created by Amir on 6/12/2023.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
//@RunWith(SpringJUnit4ClassRunner.class)
//@IntegrationTest
//@WithMockUser(username = "javainuse", password = "password", roles = {"ADNIN", "ADNIN"})
@ActiveProfiles("test")
class JwtAuthenticationControllerTest {
    //@MockBean
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    private String login(String username, String password) throws Exception {
        String requestBody = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        return jsonNode.get("token").asText();
    }

    //in junit4 Before is equal to BeforeEach
    @BeforeEach
    public void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                //so important: remove AuthenticationCredentialsNotFoundException
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .alwaysDo(result -> SecurityContextHolder.setContext(TestSecurityContextHolder.getContext()))
                .build();
        /*Authentication authentication = new UsernamePasswordAuthenticationToken(
                "username", "password", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);*/
    }

    @AfterEach
    public void tearDown() {
        // Code to run after each test
    }

    @Test
    //@WithMockUser("javainuse")
    public void testLogin() throws Exception {
        System.out.println("==================TEST LOGIN==================");
        // login
        String token = login("user", "pass1");
        Assertions.assertNotNull(token);

        mockMvc.perform(MockMvcRequestBuilders.get("/hello")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        token = login("admin", "pass2");
        mockMvc.perform(MockMvcRequestBuilders.get("/hello")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    //@WithMockUser(username="admin",roles={"USER2","ADMIN2"})
    public void testUnauthorized() throws Exception {
        System.out.println("==================TEST Unauthorized==================");
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        mockMvc.perform(MockMvcRequestBuilders.get("/hello")
                        //.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
