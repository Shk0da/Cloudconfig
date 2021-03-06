package com.github.shk0da.cloudconfig.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.github.shk0da.cloudconfig.web.rest.SshResource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class SshResourceTest {

    private SshResource ssh;
    private MockMvc mock;

    @Before
    public void setup() {
        ssh = Mockito.spy(new SshResource());
        this.mock = MockMvcBuilders.standaloneSetup(ssh).build();
    }

    @Test
    public void eurekaTest() throws Exception {

        // without key
        Mockito.doReturn(null).when(ssh).getPublicKey();
        mock.perform(get("/api/ssh/public_key"))
                .andExpect(status().isNotFound());

        // with key
        Mockito.doReturn("key").when(ssh).getPublicKey();
        mock.perform(get("/api/ssh/public_key"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("key"))
                .andExpect(status().isOk());
    }

}
