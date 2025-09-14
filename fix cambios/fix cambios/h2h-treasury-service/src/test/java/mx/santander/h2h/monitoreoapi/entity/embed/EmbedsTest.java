package mx.santander.h2h.monitoreoapi.entity.embed;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import mx.santander.h2h.monitoreoapi.model.entity.embed.*;
import org.junit.jupiter.api.Test;

class EmbedsTest {

    @Test
    void cubrirMontos() {
        Montos m = new Montos();
        m.setImporte(new BigDecimal("123.45"));
        m.setImporteAbono(new BigDecimal("67.89"));

        assertThat(m.getImporte()).isEqualByComparingTo("123.45");
        assertThat(m.getImporteAbono()).isEqualByComparingTo("67.89");
    }

    @Test
    void cubrirOrdenante() {
        Ordenante o = new Ordenante();
        o.setCuenta("0123456789");
        o.setNombre("ORDENANTE SA");
        o.setDivisa("MXN");
        o.setBic("BICORDMX");
        o.setBucCliente("BUC123");

        assertThat(o.getCuenta()).isEqualTo("0123456789");
        assertThat(o.getNombre()).isEqualTo("ORDENANTE SA");
        assertThat(o.getDivisa()).isEqualTo("MXN");
        assertThat(o.getBic()).isEqualTo("BICORDMX");
        assertThat(o.getBucCliente()).isEqualTo("BUC123");
    }

    @Test
    void cubrirReceptor() {
        Receptor r = new Receptor();
        r.setCuenta("9876543210");
        r.setNombre("RECEPTOR SA");
        r.setCuentaBanco("BANXICO");
        r.setNumeroCuenta("0011223344");
        r.setTipoCuenta("CLABE");
        r.setNombreBeneficiario("BENEF SA");
        r.setCiudad("CDMX");
        r.setPais("MX");
        r.setCalle("Av. Principal 123");
        r.setCp("01000");

        assertThat(r.getCuenta()).isEqualTo("9876543210");
        assertThat(r.getNombre()).isEqualTo("RECEPTOR SA");
        assertThat(r.getCuentaBanco()).isEqualTo("BANXICO");
        assertThat(r.getNumeroCuenta()).isEqualTo("0011223344");
        assertThat(r.getTipoCuenta()).isEqualTo("CLABE");
        assertThat(r.getNombreBeneficiario()).isEqualTo("BENEF SA");
        assertThat(r.getCiudad()).isEqualTo("CDMX");
        assertThat(r.getPais()).isEqualTo("MX");
        assertThat(r.getCalle()).isEqualTo("Av. Principal 123");
        assertThat(r.getCp()).isEqualTo("01000");
    }

    @Test
    void cubrirRespuestaApp() {
        RespuestaApp ra = new RespuestaApp();
        ra.setDescripcion("OK");
        ra.setCodigo("00");
        ra.setFlagEnvio("Y");
        ra.setIpCanal("10.10.10.10");

        assertThat(ra.getDescripcion()).isEqualTo("OK");
        assertThat(ra.getCodigo()).isEqualTo("00");
        assertThat(ra.getFlagEnvio()).isEqualTo("Y");
        assertThat(ra.getIpCanal()).isEqualTo("10.10.10.10");
    }

    @Test
    void cubrirFechas() {
        Fechas f = new Fechas();
        LocalDateTime ahora = LocalDateTime.of(2025, 8, 25, 10, 10, 0);
        f.setEnvio(ahora);
        f.setRespApp(ahora.plusMinutes(1));
        f.setUltimaMod(ahora.plusMinutes(2));
        f.setCarga(ahora.minusDays(1));

        assertThat(f.getEnvio()).isEqualTo(ahora);
        assertThat(f.getRespApp()).isEqualTo(ahora.plusMinutes(1));
        assertThat(f.getUltimaMod()).isEqualTo(ahora.plusMinutes(2));
        assertThat(f.getCarga()).isEqualTo(ahora.minusDays(1));
    }

    @Test
    void cubrirIdentificadores() {
        Identificadores id = new Identificadores();
        id.setReferenciaUnica("REF-UNICA-123");
        id.setCanal("WEB");
        id.setTipoPago("SPEI");
        id.setTipoOperacion("EN");
        id.setDivisa("MXN");
        id.setTransactionId(99);
        id.setTipoProducto("SPID");

        assertThat(id.getReferenciaUnica()).isEqualTo("REF-UNICA-123");
        assertThat(id.getCanal()).isEqualTo("WEB");
        assertThat(id.getTipoPago()).isEqualTo("SPEI");
        assertThat(id.getTipoOperacion()).isEqualTo("EN");
        assertThat(id.getDivisa()).isEqualTo("MXN");
        assertThat(id.getTransactionId()).isEqualTo(99);
        assertThat(id.getTipoProducto()).isEqualTo("SPID");
    }

    @Test
    void cubrirIntermediario() {
        Intermediario i = new Intermediario();
        i.setNombre("BANK INT");
        i.setCiudad("MADRID");
        i.setPais("ES");
        i.setBic("BICESMMX");

        assertThat(i.getNombre()).isEqualTo("BANK INT");
        assertThat(i.getCiudad()).isEqualTo("MADRID");
        assertThat(i.getPais()).isEqualTo("ES");
        assertThat(i.getBic()).isEqualTo("BICESMMX");
    }
}
