package mx.santander.h2h.monitoreoapi.config;

import mx.santander.h2h.monitoreoapi.TestPingController;
import mx.santander.h2h.monitoreoapi.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        SecurityConfigTest.WebConfig.class,
        SecurityConfig.class,
        GlobalExceptionHandler.class,
        TestPingController.class,
        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class
})
class SecurityConfigTest {

    @Configuration
    @EnableWebMvc
    static class WebConfig {}

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void anyRequest_permitAll_responde404ParaRutaInexistente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/ruta-inexistente-123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void anyRequest_permitAll_permiteAccesoSinAutenticacion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

    @Test
    void anyRequest_permitAll_noRequiereHeaders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
                .andExpect(status().isOk());
    }

    @Test
    void csrf_disabled_permitePostSinToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/echo")
                        .contentType("application/json")
                        .content("{\"x\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"x\":1}"));
    }
}
