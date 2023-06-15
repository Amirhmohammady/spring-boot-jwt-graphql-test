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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by Amir on 6/12/2023.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@IntegrationTest
//@WithMockUser(username = "javainuse", password = "password", roles = {"ADNIN", "ADNIN"})
//@ActiveProfiles("test")
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

    //in junit4 Before is equal to BeforeEach
    @BeforeEach
    public void setUp() {
        System.out.println("++++++++++++++++++++++++++++++Before");
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        //Mockito.when(bookRepository.deleteById((long) 1)).thenReturn();
        //Mockito.when(userDetailsService.loadUserByUsername("user1")).then();
    }

    @AfterEach
    public void tearDown() {
        System.out.println("------------------------------After");
        // Code to run after each test
    }

    @Test
    //@WithMockUser("javainuse")
    public void testLogin() throws Exception {
        System.out.println("++++++++++++++++++++++++++++++Test");
        // Arrange
        String username = "javainuse";
        String password = "password";
        String requestBody = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

        //System.out.println(new MyInMemoryUserDetailsService().users().loadUserByUsername("user").getUsername());
        System.out.println(userDetailsService.loadUserByUsername(username).getUsername());
        System.out.println(userDetailsService.loadUserByUsername(username).getPassword());
        System.out.println(passwordEncoder.encode(password));
        System.out.println(passwordEncoder.matches(password, userDetailsService.loadUserByUsername(username).getPassword()));
        // Act
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Extract JWT token from response body
        String responseContent = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        String token = jsonNode.get("token").asText();
        System.out.println("++++++++++++++++");
        System.out.println(token);
        System.out.println("++++++++++++++++");
        // Assert
        Assertions.assertNotNull(token);

        Thread.sleep(5000);
        // Use the token to authenticate subsequent requests
            /*AuthenticationSuccessEvent event = new AuthenticationSuccessEvent();
            SecurityContext ctx = SecurityContextHolder.createEmptyContext();
            SecurityContextHolder.setContext(ctx);
            ctx.setAuthentication(event.getAuthentication());*/
        //8mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/hello")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        /*mockMvc.perform(MockMvcRequestBuilders.get("/hello").header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());*/
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }
}
