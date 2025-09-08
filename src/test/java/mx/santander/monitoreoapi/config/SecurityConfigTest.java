package mx.santander.monitoreoapi.config;

import mx.santander.monitoreoapi.exception.GlobalExceptionHandler;
import mx.santander.monitoreoapi.TestPingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TestPingController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@TestPropertySource(properties = {
        // Para que rutas inexistentes sean 404 y no pasen por recursos estáticos
        "spring.mvc.throw-exception-if-no-handler-found=true",
        "spring.web.resources.add-mappings=false",
        // No expongas detalles de error en pruebas por default
        "app.debug.enabled=false"
})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void anyRequest_permitAll_responde404ParaRutaInexistente() throws Exception {
        mockMvc.perform(get("/api/ruta-inexistente-" + System.currentTimeMillis()))
                .andExpect(status().isNotFound());
    }

    @Test
    void anyRequest_permitAll_permiteAccesoSinAutenticacion() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

    @Test
    void anyRequest_permitAll_noRequiereHeaders() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk());
    }

    @Test
    void csrf_disabled_permitePostSinToken() throws Exception {
        // Debe permitir POST sin token CSRF; como el endpoint existe, esperamos 200
        mockMvc.perform(post("/echo").contentType("application/json").content("{\"x\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"x\":1}"));
    }
}
