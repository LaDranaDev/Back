package mx.santander.h2h.monitoreoapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


/**
 * Configuración de OpenAPI 3.0 para la documentación automática de Swagger UI.
 *
 * <p>
 * Esta clase define los metadatos y configuraciones principales para la generación
 * automática de documentación de APIs REST utilizando la especificación OpenAPI 3.0.
 * Swagger UI utiliza esta configuración para presentar una interfaz interactiva
 * que permite a los desarrolladores explorar y probar los endpoints disponibles.
 * </p>
 *
 * <p>
 * La documentación generada incluye:
 * - Información general de la API (título, versión, descripción)
 * - Detalles de contacto del equipo de desarrollo
 * - Información de licencia y términos de uso
 * - Servidores disponibles para testing
 * - Esquemas de datos y modelos utilizados
 * </p>
 *
 * <p>
 * <strong>Acceso a la documentación:</strong>
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 * </p>
 *
 * @author Rodrigo RPM
 * @version 1.0
 * @since 1.0
 * @see OpenAPIDefinition
 * @see Info
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Monitoreo API - Santander",
                version = "1.0.0",
                description = """
            API REST para el sistema de monitoreo de servicios empresariales de Santander.
            
            Esta API proporciona endpoints para:
            • Monitoreo en tiempo real de servicios y aplicaciones
            • Gestión de alertas y notificaciones
            • Consulta de métricas de rendimiento y disponibilidad
            • Administración de configuraciones de monitoreo
            • Reportes y análisis de datos históricos
            
            La API sigue los estándares REST y utiliza JSON para el intercambio de datos.
            Incluye autenticación basada en tokens y control de acceso.
            """,
                contact = @Contact(
                        name = "Equipo de Desarrollo - Monitoreo API",
                        email = "desarrollo.monitoreo@santander.com",
                        url = "https://santander.com/api-support"
                ),
                license = @License(
                        name = "Licencia Corporativa Santander",
                        url = "https://santander.com/licenses"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Servidor de Desarrollo Local"
                ),
                @Server(
                        url = "https://api-dev.santander.com",
                        description = "Servidor de Desarrollo"
                ),
                @Server(
                        url = "https://api-qa.santander.com",
                        description = "Servidor de QA/Testing"
                ),
                @Server(
                        url = "https://api.santander.com",
                        description = "Servidor de Producción"
                )
        }
)
/**
 * Componente para la api
 * <p>aqui armamos una estructura
 * aqui vemos eso

 * aqui puedes checar informacion.</p>
 * <p> con swagger encontramos.</p>
 */

@Configuration
public class OpenApiConfig {

    /**
     * Constructor por defecto de la configuración OpenAPI.
     *
     * <p>
     * Este constructor se ejecuta durante la inicialización del contexto de Spring,
     * aplicando automáticamente la configuración de OpenAPI definida en las anotaciones
     * de clase. No requiere configuración adicional ya que toda la información
     * se especifica de forma declarativa mediante anotaciones.
     * </p>
     *
     * <p>
     * La configuración se registra automáticamente en el contexto de Spring Boot
     * y queda disponible para ser utilizada por Swagger UI y otros componentes
     * que requieran acceso a los metadatos de la API.
     * </p>
     */
    public OpenApiConfig() {
        // Constructor por defecto - La configuración se aplica mediante anotaciones
        // No se requiere lógica adicional ya que OpenAPI se configura declarativamente
    }
}