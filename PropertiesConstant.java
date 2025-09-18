package mx.santander.h2h.monitoreoapi.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de propiedades externalizadas para la aplicación Treasury Service.
 *
 * <p>
 * Esta clase centraliza el acceso a todas las propiedades de configuración
 * definidas en archivos externos como application.yml o application.properties.
 * Utiliza el mecanismo de Spring Boot para mapear automáticamente valores
 * de configuración a campos Java type-safe.
 * </p>
 *
 * <p>
 * Las propiedades se cargan desde el prefijo "application.cors" definido
 * en los archivos de configuración, permitiendo una gestión centralizada
 * y flexible de la configuración de la aplicación sin hardcodear valores.
 * </p>
 *
 * <p>
 * Beneficios de la externalización de propiedades:
 * - Configuración flexible por entorno (dev, test, prod)
 * - Separación entre código y configuración
 * - Validación automática de tipos
 * - Inyección directa en componentes Spring
 * </p>
 *
 * <p>
 * <strong>IMPORTANTE:</strong> Para que las propiedades se carguen correctamente,
 * debe existir la sección correspondiente en application.yml con la estructura
 * jerárquica que coincida con el prefijo configurado.
 * </p>
 *
 * @author Rodrigo RPM
 * @version 1.0
 * @since 1.0
 * @see ConfigurationProperties
 * @see Configuration
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.cors")
public class PropertiesConstant {

    /**
     * Lista de rutas de interceptor configuradas para CORS.
     *
     * <p>
     * Contiene los patrones de URL donde se aplicarán políticas específicas
     * de CORS o interceptación de peticiones. Los valores se cargan desde
     * la propiedad application.cors.interceptorPath en los archivos de
     * configuración de la aplicación.
     * </p>
     *
     * <p>
     * Formato esperado en application.yml:
     * <pre>
     * application:
     *   cors:
     *     interceptorPath:
     *       - /api/**
     *       - /public/**
     *       - /health/**
     * </pre>
     * </p>
     *
     * <p>
     * Los patrones soportan wildcards de Spring:
     * - ? : coincide con un carácter
     * - * : coincide con cero o más caracteres dentro de un segmento
     * - ** : coincide con cero o más segmentos de ruta
     * </p>
     *
     * <p>
     * <strong>Uso típico:</strong>
     * Esta lista se utiliza para configurar interceptores HTTP, filtros
     * de seguridad o políticas de CORS que deben aplicarse únicamente
     * a rutas específicas en lugar de toda la aplicación.
     * </p>
     *
     * @see org.springframework.util.AntPathMatcher
     */
    private List<String> interceptorPath;

    /**
     * Configuración adicional para orígenes permitidos en CORS.
     *
     * <p>
     * Lista opcional de dominios que tienen permitido realizar peticiones
     * cross-origin a esta API. Si no se especifica, se utilizarán los
     * valores por defecto definidos en GlobalCorsConfig.
     * </p>
     *
     * <p>
     * Formato esperado en application.yml:
     * <pre>
     * application:
     *   cors:
     *     allowedOrigins:
     *       - http://localhost:3000
     *       - https://mi-frontend.com
     * </pre>
     * </p>
     */
    private List<String> allowedOrigins;

    /**
     * Configuración de métodos HTTP permitidos para CORS.
     *
     * <p>
     * Lista opcional de métodos HTTP que están permitidos en peticiones
     * cross-origin. Si no se especifica, se utilizarán los valores
     * por defecto (GET, POST, PUT, DELETE, OPTIONS, PATCH).
     * </p>
     */
    private List<String> allowedMethods;

    /**
     * Configuración de cabeceras permitidas para CORS.
     *
     * <p>
     * Lista opcional de cabeceras HTTP que pueden incluirse en peticiones
     * cross-origin. Un valor de "*" permite todas las cabeceras.
     * </p>
     */
    private List<String> allowedHeaders;

    /**
     * Indica si las credenciales están habilitadas para CORS.
     *
     * <p>
     * Cuando es true, permite que las peticiones cross-origin incluyan
     * cookies, headers de autorización y otras credenciales. Por defecto
     * es true para soportar autenticación.
     * </p>
     */
    private Boolean allowCredentials = true;

    /**
     * Tiempo de cacheo en segundos para respuestas preflight CORS.
     *
     * <p>
     * Define cuánto tiempo los navegadores pueden cachear la respuesta
     * de una petición OPTIONS preflight. Un valor mayor mejora el
     * rendimiento reduciendo peticiones repetitivas.
     * </p>
     */
    private Long maxAge = 3600L;
}