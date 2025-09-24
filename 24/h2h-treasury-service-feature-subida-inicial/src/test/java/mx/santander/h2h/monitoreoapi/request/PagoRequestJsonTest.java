package mx.santander.h2h.monitoreoapi.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.santander.h2h.monitoreoapi.model.request.PagoRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PagoRequestJsonTest {

    private final ObjectMapper om = new ObjectMapper();

    @Test
    void json_unwrapped_flatten_ok() throws Exception {
        PagoRequest r = new PagoRequest();
        r.setOperacion("SPEI");
        r.setDivisa("MXN");
        r.setImporte(new BigDecimal("10.00"));

        PagoRequest.RangoFechas rf = new PagoRequest.RangoFechas();
        // fechas nulas (opcionales) – solo checamos *flatten*
        r.setRangoFechas(rf);

        PagoRequest.Cuentas ctas = new PagoRequest.Cuentas();
        ctas.setCuentaCargo("123456789012");
        ctas.setCuentaAbono("987654321098");
        r.setCuentas(ctas);

        String json = om.writeValueAsString(r);

        // Campos de nivel superior “aplanados”
        assertThat(json).contains("operacion");
        assertThat(json).contains("divisa");
        assertThat(json).contains("cuentaCargo");
        assertThat(json).contains("cuentaAbono");

        // No debe serializar toString() ni los valores enmascarados
        assertThat(json).doesNotContain("****");
    }
}
