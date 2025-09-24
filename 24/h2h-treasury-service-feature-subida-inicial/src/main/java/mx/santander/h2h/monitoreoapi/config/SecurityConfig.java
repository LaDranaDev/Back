package mx.santander.h2h.monitoreoapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de Spring Security para la aplicación de monitoreo.
 *
 * <p>
 * Esta clase define las políticas de seguridad y autenticación para todos los endpoints
 * de la API REST. Configura aspectos fundamentales como CORS, CSRF, autorización
 * de requests y filtros de seguridad que se aplicarán a las peticiones entrantes.
 * </p>
 *
 * <p>
 * La configuración actual está optimizada para entornos de desarrollo y testing,
 * permitiendo acceso libre a todos los endpoints. Para entornos productivos
 * se debe implementar autenticación robusta y control de acceso granular.
 * </p>
 *
 * <p>
 * Características de seguridad configuradas:
 * - Integración con configuración CORS global
 * - Deshabilitación de CSRF para APIs REST stateless
 * - Configuración de cadena de filtros de seguridad
 * - Políticas de autorización por endpoint
 * </p>
 *
 * <p>
 * <strong>ADVERTENCIA:</strong> Esta configuración permite acceso sin autenticación.
 * En producción debe implementarse autenticación JWT, OAuth2 o similar.
 * </p>
 *
 * @author Rodrigo RPM
 * @version 1.0
 * @since 1.0
 * @see SecurityFilterChain
 * @see HttpSecurity
 */
@Configuration
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad para la aplicación API.
     *
     * <p>
     * Este método define la configuración completa de seguridad que se aplicará
     * a todas las peticiones HTTP entrantes. Establece políticas de CORS,
     * CSRF, autenticación y autorización que determinan cómo se procesan
     * y validan las requests antes de llegar a los controladores.
     * </p>
     *
     * <p>
     * Configuración de seguridad aplicada:
     * - CORS: Utiliza la configuración global definida en GlobalCorsConfig
     * - CSRF: Deshabilitado para APIs REST que no mantienen estado de sesión
     * - Autorización: Acceso permitido a todos los endpoints (solo desarrollo)
     * - Filtros: Cadena estándar de Spring Security sin autenticación
     * </p>
     *
     * <p>
     * <strong>Consideraciones de producción:</strong>
     * Para entornos productivos se recomienda:
     * - Implementar autenticación JWT o OAuth2
     * - Configurar roles y permisos granulares
     * - Habilitar HTTPS obligatorio
     * - Implementar rate limiting y protección DDoS
     * </p>
     *
     * @param http objeto HttpSecurity para configurar las políticas de seguridad web
     *             Proporciona DSL fluido para definir reglas de seguridad HTTP
     * @return SecurityFilterChain configurada con las políticas de seguridad definidas
     *         Lista para procesar requests entrantes según las reglas establecidas
     * @throws Exception si ocurre error durante la configuración de seguridad
     *                  Puede incluir errores de configuración o dependencias faltantes
     */
    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        // Configuración de la cadena de seguridad para endpoints de API REST
        http
                // Aplicación de configuración CORS global definida en GlobalCorsConfig
                .cors(Customizer.withDefaults())

                // Deshabilitación de CSRF para APIs REST stateless
                // Las APIs REST no mantienen estado de sesión y utilizan tokens
                .csrf(csrf -> csrf.disable())

                // Configuración de reglas de autorización para requests HTTP
                .authorizeHttpRequests(auth -> auth
                        // TEMPORAL: Acceso libre para desarrollo y testing
                        .anyRequest().permitAll()
                );

        // Construcción y retorno de la cadena de filtros configurada
        return http.build();
    }
}

