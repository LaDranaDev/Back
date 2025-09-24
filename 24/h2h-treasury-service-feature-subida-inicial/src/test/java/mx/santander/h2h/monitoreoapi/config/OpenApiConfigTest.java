package mx.santander.h2h.monitoreoapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {

    @Test
    void clase_tieneOpenApiDefinition_yInfoValido() {
        OpenAPIDefinition def = OpenApiConfig.class.getAnnotation(OpenAPIDefinition.class);
        assertThat(def).isNotNull();
        Info info = def.info();
        assertThat(info.title()).isEqualTo("Monitoreo API - Santander");
        assertThat(info.version()).isEqualTo("1.0.0");

        // Ejecutar el ctor para cubrir líneas
        assertThat(new OpenApiConfig()).isNotNull();
    }
}
