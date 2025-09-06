package mx.santander.monitoreoapi.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalCorsConfigTest {

    @Test
    void corsConfigurer_configuraReglasEsperadas() {
        GlobalCorsConfig cfg = new GlobalCorsConfig();
        WebMvcConfigurer webCfg = cfg.corsConfigurer();
        assertThat(webCfg).isNotNull();

        // Mock de Registry y de la Registration (donde están los métodos encadenados)
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class, RETURNS_SELF);

        // addMapping debe devolver la registration
        when(registry.addMapping("/**")).thenReturn(registration);

        // Ejecutar configuración
        webCfg.addCorsMappings(registry);

        // Verificaciones
        verify(registry).addMapping("/**");
        verify(registration).allowedOriginPatterns("http://localhost:4200", "https://prod-domain.com");
        verify(registration).allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
        verify(registration).allowedHeaders("*");
        verify(registration).allowCredentials(true);
        verify(registration).maxAge(3600L);

        verifyNoMoreInteractions(registry, registration);
    }
}
