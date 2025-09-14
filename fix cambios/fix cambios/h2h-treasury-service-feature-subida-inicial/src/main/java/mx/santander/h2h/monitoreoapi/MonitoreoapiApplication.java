package mx.santander.h2h.monitoreoapi;
/*
 * Clase principal para arrancar la aplicación de Monitoreo API.
 *
 * <p><strong>Responsabilidades</strong>:</p>
 * <ul>
 *   <li>Arrancar el contexto de Spring Boot y el escaneo de componentes.</li>
 *   <li>Habilitar la serialización de páginas vía DTO para endpoints paginados.</li>
 * </ul>
 * <p>No contiene lógica de negocio.</p>
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * Clase principal para arrancar la aplicación de Monitoreo API.
 */
@SpringBootApplication
@EnableSpringDataWebSupport(
        pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class MonitoreoapiApplication {
    /**
     * Arranque estándar de Spring Boot.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        SpringApplication.run(MonitoreoapiApplication.class, args);
    }
}
