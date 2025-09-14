package mx.santander.h2h.monitoreoapi.config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
public class RestTemplateConfigTest {
    RestTemplateConfig restTemplateConfig = new RestTemplateConfig();

    @Test
    void testGetRestTemplate() {
        RestTemplate result = restTemplateConfig.getRestTemplate();
        Assertions.assertNotNull(result);
    }
}
