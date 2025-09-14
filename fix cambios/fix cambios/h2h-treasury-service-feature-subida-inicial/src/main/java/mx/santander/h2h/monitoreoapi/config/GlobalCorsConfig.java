package mx.santander.h2h.monitoreoapi.config;

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
 * Configuración global de CORS (Cross-Origin Resource Sharing) para la aplicación.
 *
 * <p>
 * Esta clase define las políticas de intercambio de recursos entre orígenes distintos,
 * permitiendo que aplicaciones web ejecutándose en diferentes dominios puedan
 * interactuar con la API de manera segura y controlada.
 * </p>
 *
 * <p>
 * La configuración establece:
 * - Orígenes permitidos para realizar peticiones
 * - Métodos HTTP autorizados (GET, POST, PUT, DELETE, etc.)
 * - Headers personalizados permitidos
 * - Configuración de credenciales y tiempo de caché
 * </p>
 *
 * <p>
 * <strong>Importante:</strong> Esta configuración está optimizada para desarrollo.
 * En entornos productivos se debe restringir adecuadamente los orígenes permitidos.
 * </p>
 *
 * @author Rodrigo RPM
 * @version 1.0
 * @since 1.0
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
