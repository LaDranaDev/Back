package mx.santander.monitoreoapi.request;

import mx.santander.monitoreoapi.model.request.PagoRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PagoRequestMaskingTest {

    @Test
    void cuentas_toString_enmascara_ultimos4() {
        PagoRequest.Cuentas c = new PagoRequest.Cuentas();
        c.setCuentaCargo("12345678901234");
        c.setCuentaAbono("00000000000001");

        String s = c.toString();
        assertThat(s).contains("****1234");
        assertThat(s).contains("****0001");
        assertThat(s).doesNotContain("12345678901234");
        assertThat(s).doesNotContain("00000000000001");
    }

    @Test
    void cuentas_toString_nulos_o_cortos() {
        PagoRequest.Cuentas c = new PagoRequest.Cuentas();
        c.setCuentaCargo(null);
        c.setCuentaAbono("12");
        String s = c.toString();
        assertThat(s).contains("****"); // ambos casos devuelven ****
    }

    @Test
    void identificadores_toString_sanea_y_enmascara() {
        PagoRequest.Identificadores ids = new PagoRequest.Identificadores();
        ids.setCanal("WEB\r\nADMIN"); // CRLF se limpia
        ids.setTransactionId("ABC123456");

        String s = ids.toString();
        assertThat(s).contains("WEBADMIN"); // sin CRLF
        assertThat(s).contains("***456");   // solo últimos 3 visibles
        assertThat(s).doesNotContain("ABC123456");
    }
}
