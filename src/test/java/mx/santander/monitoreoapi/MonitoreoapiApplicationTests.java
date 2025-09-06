package mx.santander.monitoreoapi;

import mx.santander.monitoreoapi.builder.PagoFiltroBuilder;
import mx.santander.monitoreoapi.repository.IPagoRepository;
import mx.santander.monitoreoapi.service.PagoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;

@SpringBootTest(
		classes = MonitoreoapiApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		properties = {
				"spring.main.banner-mode=off",
				"server.port=0" // por si algún auto-config quiere puerto
		}
)
@Import(MonitoreoapiApplicationTests.MocksConfig.class)
class MonitoreoapiApplicationTests {

	@TestConfiguration
	static class MocksConfig {
		@Bean @Primary IPagoRepository pagoRepository() { return mock(IPagoRepository.class); }
		@Bean @Primary PagoFiltroBuilder pagoFiltroBuilder() { return mock(PagoFiltroBuilder.class); }
	}

	@Autowired
	private ApplicationContext springCtx;

	@Test
	void contextLoads_yExistePagoService() {
		assertThat(springCtx).isNotNull();
		assertThat(springCtx.getBean(PagoService.class)).isNotNull();
	}

	@Test
	void main_noLanzaExcepciones() {
		// Ejecuta el main; al usar MOCK en @SpringBootTest ya hay entorno servlet disponible
		assertThatCode(() ->
				MonitoreoapiApplication.main(new String[] { "--server.port=0", "--spring.main.banner-mode=off" })
		).doesNotThrowAnyException();
	}

	@Test
	void springApplication_arrancaYCierraContexto() {
		SpringApplication app = new SpringApplication(MonitoreoapiApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		// Por defecto es SERVLET; aseguramos que no intente usar un puerto fijo
		app.setDefaultProperties(Map.of("server.port","0"));

		try (ConfigurableApplicationContext ctx = app.run()) {
			assertThat(ctx).isNotNull();
			assertThat(ctx.isActive()).isTrue();
		}
	}
}
