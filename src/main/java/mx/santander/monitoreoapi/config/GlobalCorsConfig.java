package mx.santander.monitoreoapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * CORS seguro: sin comodines, orígenes por propiedad y credenciales deshabilitadas.
 * Fortify odia '*' en allowedOrigins/allowedHeaders.
 *
 * Front/QA: cambien la propiedad app.cors.allowed-origins en su application-<env>.yml.
 */
@Configuration
public class GlobalCorsConfig {

    /**
     * Orígenes permitidos (separados por coma), p.ej.:
     *  app.cors.allowed-origins=https://front.miempresa.mx,https://admin.miempresa.mx
     *
     * Nota: Dejar vacío deniega CORS por completo (seguro por defecto).
     */
    @Value("${app.cors.allowed-origins:}")
    private String allowedOriginsProp;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();

        // Parseo seguro de orígenes (sin comodines)
        List<String> origins = Arrays.stream(allowedOriginsProp.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList(); // <- inmodificable; OK para pasar a CorsConfiguration

        c.setAllowedOrigins(origins);

        // Métodos explícitos (sin '*')
        c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Headers explícitos (sin '*')
        c.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With",
                "Cache-Control",
                "Pragma"
        ));

        // Headers expuestos (descargas, etc.)
        c.setExposedHeaders(List.of("Content-Disposition"));

        // Mantener en false salvo que uses cookies/sesión entre dominios
        c.setAllowCredentials(false);

        // Preflight cache
        c.setMaxAge(Duration.ofHours(1));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", c);
        return source;
    }
}
