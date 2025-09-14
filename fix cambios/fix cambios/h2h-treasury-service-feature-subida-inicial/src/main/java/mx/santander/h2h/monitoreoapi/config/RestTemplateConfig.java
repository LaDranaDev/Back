package mx.santander.h2h.monitoreoapi.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración para habilitar el servicio de restTemplate para poder invocar
 * otros microservios.
 *
 * @author Omar Rosas
 * @since 20/01/23
 */
@Configuration
@ComponentScan("mx.santander.h2h.monitoreoapi.config")
public class RestTemplateConfig {

    /**
     * Se registra el bean de RestTemplate para poder inyectarlo
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
