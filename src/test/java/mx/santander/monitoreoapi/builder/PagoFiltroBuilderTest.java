package mx.santander.monitoreoapi.builder;

import mx.santander.monitoreoapi.exception.FiltroInvalidoException;
import mx.santander.monitoreoapi.model.request.PagoDetalleFiltroDTO;
import mx.santander.monitoreoapi.model.request.PagoRequest;
import mx.santander.monitoreoapi.model.request.PagoRequest.Cuentas;
import mx.santander.monitoreoapi.model.request.PagoRequest.Identificadores;
import mx.santander.monitoreoapi.model.request.PagoRequest.RangoFechas;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class PagoFiltroBuilderTest {

    private PagoRequest crearRequestValido() {
        PagoRequest req = new PagoRequest();

        Cuentas ctas = new Cuentas();
        ctas.setCuentaAbono("1234567890");
        ctas.setCuentaCargo("0987654321");
        req.setCuentas(ctas);

        RangoFechas rf = new RangoFechas();
        rf.setFechaInicio(LocalDate.of(2025, 8, 24));
        rf.setFechaFin(LocalDate.of(2025, 8, 25));
        req.setRangoFechas(rf);

        Identificadores ids = new Identificadores();
        ids.setTransactionId("99");
        req.setIdentificadores(ids);

        req.setOperacion("EN");
        req.setImporte(new BigDecimal("10.00"));
        req.setReferenciaCanal("REF-001");
        req.setDivisa("MXN");
        req.setTipoPago("SPEI");
        req.setEstatus("PR"); // tipo String

        return req;
    }

    @Test
    void build_mapeaCampos_yNormalizaFechas() {
        PagoFiltroBuilder builder = new PagoFiltroBuilder();
        PagoRequest req = crearRequestValido();

        PagoDetalleFiltroDTO dto = builder.build(req);

        assertThat(dto.getCuentaAbono()).isEqualTo("1234567890");
        assertThat(dto.getCuentaCargo()).isEqualTo("0987654321");
        assertThat(dto.getOperacion()).isEqualTo("EN");
        assertThat(dto.getImporte()).isEqualByComparingTo("10.00");
        assertThat(dto.getReferenciaCanal()).isEqualTo("REF-001");
        assertThat(dto.getDivisa()).isEqualTo("MXN");
        assertThat(dto.getTipoPago()).isEqualTo("SPEI");
        assertThat(dto.getEstatus()).isEqualTo("PR");
        assertThat(dto.getTransactionId()).isEqualTo(99L);

        LocalDateTime iniEsperado = LocalDate.of(2025, 8, 24).atStartOfDay();
        LocalDateTime finEsperado = LocalDate.of(2025, 8, 25).atTime(LocalTime.MAX);

        assertThat(dto.getRangoFechas()).isNotNull();
        assertThat(dto.getRangoFechas().getFechaInicio()).isEqualTo(iniEsperado);
        assertThat(dto.getRangoFechas().getFechaFin()).isEqualTo(finEsperado);
    }

    @Test
    void build_lanzaExcepcionCuandoTransactionIdNoEsNumerico() {
        PagoFiltroBuilder builder = new PagoFiltroBuilder();
        PagoRequest req = crearRequestValido();
        req.getIdentificadores().setTransactionId("no-num");

        assertThatThrownBy(() -> builder.build(req))
                .isInstanceOf(FiltroInvalidoException.class)
                .hasMessageContaining("transactionId inválido");
    }

    @Test
    void build_conSoloFechaInicio_normalizaSoloInicio() {
        PagoFiltroBuilder builder = new PagoFiltroBuilder();
        PagoRequest req = new PagoRequest();

        PagoRequest.RangoFechas rf = new PagoRequest.RangoFechas();
        rf.setFechaInicio(LocalDate.of(2025, 8, 24));
        req.setRangoFechas(rf);

        PagoDetalleFiltroDTO dto = builder.build(req);

        assertThat(dto.getRangoFechas().getFechaInicio())
                .isEqualTo(LocalDate.of(2025, 8, 24).atStartOfDay());
        assertThat(dto.getRangoFechas().getFechaFin()).isNull();
    }

    @Test
    void build_conSoloFechaFin_normalizaSoloFin() {
        PagoFiltroBuilder builder = new PagoFiltroBuilder();
        PagoRequest req = new PagoRequest();

        PagoRequest.RangoFechas rf = new PagoRequest.RangoFechas();
        rf.setFechaFin(LocalDate.of(2025, 8, 25));
        req.setRangoFechas(rf);

        PagoDetalleFiltroDTO dto = builder.build(req);

        assertThat(dto.getRangoFechas().getFechaInicio()).isNull();
        assertThat(dto.getRangoFechas().getFechaFin())
                .isEqualTo(LocalDate.of(2025, 8, 25).atTime(LocalTime.MAX));
    }

    @Test
    void build_conRangoFechasNull_dejaFechasNull() {
        PagoFiltroBuilder builder = new PagoFiltroBuilder();
        PagoRequest req = new PagoRequest();
        req.setRangoFechas(null); // explícitamente null

        PagoDetalleFiltroDTO dto = builder.build(req);

        assertThat(dto.getRangoFechas()).isNotNull();
        assertThat(dto.getRangoFechas().getFechaInicio()).isNull();
        assertThat(dto.getRangoFechas().getFechaFin()).isNull();
    }

    @Test
    void build_transactionIdNull_noAsignaNada() {
        PagoFiltroBuilder builder = new PagoFiltroBuilder();
        PagoRequest req = new PagoRequest();

        PagoRequest.Identificadores ids = new PagoRequest.Identificadores();
        ids.setTransactionId(null); // caso explícito
        req.setIdentificadores(ids);

        PagoDetalleFiltroDTO dto = builder.build(req);
        assertThat(dto.getTransactionId()).isNull();
    }

    @Test
    void build_transactionIdVacio_noAsignaNada() {
        PagoFiltroBuilder builder = new PagoFiltroBuilder();
        PagoRequest req = new PagoRequest();

        PagoRequest.Identificadores ids = new PagoRequest.Identificadores();
        ids.setTransactionId("   "); // string en blanco
        req.setIdentificadores(ids);

        PagoDetalleFiltroDTO dto = builder.build(req);
        assertThat(dto.getTransactionId()).isNull();
    }


    @Test
    void build_permiteCamposNulos_yMantieneRangoFechasNoNulo() {
        PagoFiltroBuilder builder = new PagoFiltroBuilder();
        PagoRequest req = new PagoRequest(); //  nulo

        PagoDetalleFiltroDTO dto = builder.build(req);

        assertThat(dto.getCuentaAbono()).isNull();
        assertThat(dto.getCuentaCargo()).isNull();
        assertThat(dto.getOperacion()).isNull();
        assertThat(dto.getImporte()).isNull();
        assertThat(dto.getReferenciaCanal()).isNull();
        assertThat(dto.getDivisa()).isNull();
        assertThat(dto.getTipoPago()).isNull();
        assertThat(dto.getEstatus()).isNull();
        assertThat(dto.getTransactionId()).isNull();

        assertThat(dto.getRangoFechas()).isNotNull();
        assertThat(dto.getRangoFechas().getFechaInicio()).isNull();
        assertThat(dto.getRangoFechas().getFechaFin()).isNull();




    }
}
