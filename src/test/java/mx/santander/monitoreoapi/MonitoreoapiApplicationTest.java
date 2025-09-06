// src/test/java/mx/santander/monitoreoapi/MonitoreoapiApplicationTest.java
package mx.santander.monitoreoapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatCode;

class MonitoreoapiApplicationTest {

    @Test
    void main_doesNotThrow_withoutWebServer_or_DB() {
        assertThatCode(() -> {
            SpringApplication app = new SpringApplication(MonitoreoapiApplication.class);
            app.setWebApplicationType(WebApplicationType.NONE);              // sin servidor web
            app.setDefaultProperties(Map.of(
                    // arranque perezoso: no crea singletons mientras inicia
                    "spring.main.lazy-initialization", "true",
                    // nada de datasources ni JPA/repos
                    "spring.autoconfigure.exclude", String.join(",",
                            DataSourceAutoConfiguration.class.getName(),
                            HibernateJpaAutoConfiguration.class.getName(),
                            JpaRepositoriesAutoConfiguration.class.getName())
            ));
            app.run(); // sin argumentos adicionales
        }).doesNotThrowAnyException();
    }
}

