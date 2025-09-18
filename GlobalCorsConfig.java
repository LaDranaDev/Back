package mx.santander.h2h.monitoreoapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de CORS (Cross-Origin Resource Sharing) para la aplicación.
 *
 * <p>
 * Esta clase implementa las políticas de CORS que permiten a aplicaciones web
 * ejecutándose en diferentes dominios acceder a los recursos de esta API REST.
 * Define qué orígenes, métodos HTTP y cabeceras están permitidos para las
 * peticiones cross-origin.
 * </p>
 *
 * <p>
 * La configuración actual está optimizada para entornos de desarrollo, permitiendo
 * acceso desde aplicaciones frontend corriendo en puertos locales comunes.
 * Para producción se debe restringir a dominios específicos por seguridad.
 * </p>
 *
 * <p>
 * Características CORS configuradas:
 * - Orígenes permitidos: localhost en puertos 3000, 4200 y 8080
 * - Métodos HTTP: GET, POST, PUT, DELETE, OPTIONS
 * - Soporte completo para cabeceras personalizadas
 * - Habilitación de credenciales para autenticación
 * </p>
 *
 * <p>
 * <strong>IMPORTANTE:</strong> En entornos productivos se debe configurar
 * orígenes específicos en lugar de wildcards por razones de seguridad.
 * </p>
 *
 * @author Rodrigo RPM
 * @version 1.0
 * @since 1.0
 * @see WebMvcConfigurer
 * @see CorsRegistry
 */
@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

    /**
     * Configura las políticas de CORS para todos los endpoints de la API.
     *
     * <p>
     * Este método define las reglas de acceso cross-origin que se aplicarán
     * a todas las peticiones HTTP entrantes. Establece qué dominios externos
     * pueden acceder a los recursos, qué métodos HTTP están permitidos y
     * qué cabeceras pueden incluirse en las peticiones.
     * </p>
     *
     * <p>
     * Configuración aplicada:
     * - Mapeo de rutas: Todas las rutas que empiecen con /api/
     * - Orígenes permitidos: Aplicaciones frontend en desarrollo local
     * - Métodos HTTP: Operaciones CRUD completas más OPTIONS para preflight
     * - Cabeceras: Acceso irrestricto para máxima compatibilidad
     * - Credenciales: Habilitadas para soportar autenticación
     * </p>
     *
     * <p>
     * <strong>Consideraciones de seguridad:</strong>
     * Para entornos productivos se recomienda:
     * - Especificar dominios exactos en lugar de localhost
     * - Restringir cabeceras a las estrictamente necesarias
     * - Implementar validación adicional de orígenes
     * - Configurar timeouts y límites de tamaño
     * </p>
     *
     * @param registry objeto CorsRegistry para configurar las políticas CORS
     *                Proporciona DSL fluido para definir reglas de acceso cross-origin
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configuración de CORS para todos los endpoints de la API REST
        registry.addMapping("/api/**")

                // Orígenes permitidos para acceso cross-origin
                // Incluye puertos comunes para desarrollo frontend
                .allowedOrigins(
                        "http://localhost:3000",    // React development server
                        "http://localhost:4200",    // Angular development server
                        "http://localhost:8080",    // Spring Boot o otros frameworks
                        "http://localhost:5173",    // Vite development server
                        "http://localhost:3001"     // Puertos alternativos
                )

                // Métodos HTTP permitidos para peticiones cross-origin
                // Incluye operaciones CRUD completas y OPTIONS para preflight
                .allowedMethods(
                        "GET",      // Consultas y recuperación de datos
                        "POST",     // Creación de recursos
                        "PUT",      // Actualización completa de recursos
                        "DELETE",   // Eliminación de recursos
                        "OPTIONS",  // Peticiones preflight para CORS
                        "PATCH"     // Actualización parcial de recursos
                )

                // Cabeceras permitidas en peticiones cross-origin
                // Configuración permisiva para máxima compatibilidad con frontends
                .allowedHeaders("*")

                // Cabeceras expuestas al cliente en las respuestas
                // Útil para permitir acceso a metadatos de respuesta
                .exposedHeaders(
                        "Authorization",
                        "Content-Type",
                        "X-Total-Count",
                        "X-Page-Count"
                )

                // Habilitación de credenciales (cookies, headers de auth)
                // Necesario para peticiones que incluyen autenticación
                .allowCredentials(true)

                // Tiempo de cacheo de respuestas preflight en segundos
                // Mejora performance reduciendo peticiones OPTIONS repetitivas
                .maxAge(3600);
    }
}