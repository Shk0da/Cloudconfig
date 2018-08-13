package com.github.shk0da.cloudconfig.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.github.shk0da.cloudconfig.security.jwt.TokenProvider;
import com.github.shk0da.cloudconfig.web.rest.UserJWTController;
import com.github.shk0da.cloudconfig.web.rest.vm.LoginVM;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserJWTControllerTest {

    private TokenProvider tokenProvider;
    private AuthenticationManager authenticationManager;
    private MockMvc mock;

    @Before
    public void setup() {
        tokenProvider = Mockito.mock(TokenProvider.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        UserJWTController control = new UserJWTController(tokenProvider, authenticationManager);
        this.mock = MockMvcBuilders.standaloneSetup(control).build();
    }

    @Test
    public void normalAuthentication() throws Exception {
        // Normal authentication
        LoginVM vm = new LoginVM();
        vm.setUsername("admin");
        vm.setPassword("admin");
        vm.setRememberMe(true);

        Mockito.doReturn(new UsernamePasswordAuthenticationToken(vm.getUsername(), vm.getPassword()))
                .when(authenticationManager)
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));

        Mockito.doReturn("fakeToken")
                .when(tokenProvider)
                .createToken(Mockito.any(Authentication.class), Mockito.anyBoolean());

        mock.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.ALL)
                .content(new ObjectMapper().writeValueAsString(vm)))
                .andExpect(content().string("{\"id_token\":\"fakeToken\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void authenticationException() throws Exception {
        // Authentication exception throws
        Mockito.doThrow(new AuthenticationException(null) {
        })
                .when(authenticationManager)
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));

        MvcResult res = mock.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.ALL)
                .content("{\"username\":\"fakeUsername\",\"password\":\"fakePassword\",\"rememberMe\":false}"))
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertTrue(res.getResponse().getContentAsString().startsWith("{\"AuthenticationException\""));
    }

    @Test
    public void badCredentials() throws Exception {
        LoginVM vm = new LoginVM();
        vm.setUsername("badcred");
        vm.setPassword("badcred");
        vm.setRememberMe(true);

        // Authentication exception throws
        Mockito.doThrow(new AuthenticationException("Bad credentials") {
        })
                .when(authenticationManager)
                .authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));

        mock.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.ALL)
                .content(new ObjectMapper().writeValueAsString(vm)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("{\"AuthenticationException\":\"Bad credentials\"}"));
    }

    @Test
    public void getIdTokenTest() throws Exception {
        assertNotNull(new UserJWTController.JWTToken("id").getIdToken());
        assertEquals("id", new UserJWTController.JWTToken("id").getIdToken());
        assertNull(new UserJWTController.JWTToken(null).getIdToken());
    }

    @Test
    public void setIdTokenTest() throws Exception {
        UserJWTController.JWTToken token = new UserJWTController.JWTToken("id");
        assertNotNull(token.getIdToken());

        assertNotEquals("id2", token.getIdToken());
        token.setIdToken("id2");
        assertEquals("id2", token.getIdToken());

        token.setIdToken(null);
        assertNull(token.getIdToken());

        token = new UserJWTController.JWTToken(null);
        token.setIdToken(null);
        assertNull(token.getIdToken());
    }
}
