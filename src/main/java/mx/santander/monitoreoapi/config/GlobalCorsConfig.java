package mx.santander.monitoreoapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
     * Configura las políticas CORS globales para toda la aplicación.
     *
     * <p>
     * Este método crea un configurador web MVC que define las reglas de CORS
     * aplicables a todos los endpoints de la aplicación. La configuración permite
     * peticiones desde orígenes específicos con métodos HTTP definidos.
     * </p>
     *
     * <p>
     * Configuración aplicada:
     * - Mapeo: Todos los endpoints ("/**")
     * - Orígenes: localhost:4200 (desarrollo) y otros configurables
     * - Métodos: GET, POST, PUT, DELETE, PATCH, OPTIONS
     * - Headers: Todos permitidos (*)
     * - Credenciales: Habilitadas para autenticación
     * - Cache: 3600 segundos (1 hora)
     * </p>
     *
     * @return WebMvcConfigurer configurado con las políticas CORS definidas
     *         para permitir intercambio seguro de recursos entre orígenes
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * Añade las configuraciones de mapeo CORS al registro global.
             *
             * <p>
             * Define las reglas específicas de CORS que se aplicarán a las peticiones
             * entrantes, incluyendo validación de orígenes, métodos permitidos y
             * configuración de headers de respuesta apropiados.
             * </p>
             *
             * @param registry registro de configuraciones CORS donde se definen
             *                las reglas de intercambio de recursos entre dominios
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Configuración CORS para todos los endpoints de la aplicación
                registry.addMapping("/**")
                        // Definición de patrones de origen permitidos para peticiones
                        .allowedOriginPatterns(
                                "http://localhost:4200",   // Entorno de desarrollo Angular/React
                                "https://prod-domain.com"  // Dominio de producción (configurar según necesidad)
                        )
                        // Métodos HTTP autorizados para peticiones cross-origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        // Permitir todos los headers personalizados en las peticiones
                        .allowedHeaders("*")
                        // Habilitar envío de credenciales (cookies, headers de autorización)
                        .allowCredentials(true)
                        // Tiempo de caché para preflight requests (1 hora)
                        .maxAge(3600);
            }
        };
    }
}