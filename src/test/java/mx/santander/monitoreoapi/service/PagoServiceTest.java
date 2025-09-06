package mx.santander.monitoreoapi.service;

import mx.santander.monitoreoapi.builder.PagoFiltroBuilder;
import mx.santander.monitoreoapi.model.request.PagoDetalleFiltroDTO;
import mx.santander.monitoreoapi.model.request.PagoRequest;
import mx.santander.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import mx.santander.monitoreoapi.model.response.PagoDetalleDTO;
import mx.santander.monitoreoapi.repository.IPagoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock IPagoRepository pagoRepository;

    // Usa el builder REAL para que mappee los campos (no lo stubbeamos)
    @Spy PagoFiltroBuilder filtroBuilder = new PagoFiltroBuilder();

    @InjectMocks PagoService pagoService;

    private Pageable pageable() {
        return PageRequest.of(0, 20, Sort.by("id").descending());
    }

    private PagoRequest buildRequestValido() {
        PagoRequest req = new PagoRequest();
        req.setOperacion("EN");
        req.setDivisa("MXN");
        req.setTipoPago("SPEI");
        req.setTipoOperacion("PAGO");
        req.setImporte(new BigDecimal("10.00"));
        req.setReferenciaCanal("REF-001");

        var cuentas = new PagoRequest.Cuentas();
        cuentas.setCuentaAbono("1234567890");
        cuentas.setCuentaCargo("9876543210");
        req.setCuentas(cuentas);

        var ids = new PagoRequest.Identificadores();
        ids.setCanal("H2H");
        ids.setTransactionId("99");
        req.setIdentificadores(ids);

        var rango = new PagoRequest.RangoFechas();
        rango.setFechaInicio(LocalDate.now().minusDays(1));
        rango.setFechaFin(LocalDate.now());
        req.setRangoFechas(rango);

        return req;
    }

    private PagoDetalleDTO detalleDummy() {
        return new PagoDetalleDTO(
                "EN",
                "1234567890",
                BigDecimal.ONE,
                "REF-001",
                "MXN",
                LocalDateTime.now(),
                "9876543210",
                "SPEI",
                "PROCESADO",
                99
        );
    }

    @Test
    void obtenerDetalle_ok() {
        var pageMock = new PageImpl<>(Collections.singletonList(detalleDummy()), pageable(), 1);

        // NO stub al filtroBuilder.build(...): usamos el real (Spy)
        when(pagoRepository.detallePorFiltros(any(PagoDetalleFiltroDTO.class), any(Pageable.class)))
                .thenReturn(pageMock);

        var req = buildRequestValido();
        var out = pagoService.obtenerDetalle(req, pageable());

        assertThat(out.getTotalElements()).isEqualTo(1);
        assertThat(out.getContent().get(0).operacion()).isEqualTo("EN");

        // Capturamos el filtro que llegó al repo y validamos el mapeo
        ArgumentCaptor<PagoDetalleFiltroDTO> cap = ArgumentCaptor.forClass(PagoDetalleFiltroDTO.class);
        verify(pagoRepository).detallePorFiltros(cap.capture(), any(Pageable.class));
        var usado = cap.getValue();

        assertThat(usado.getOperacion()).isEqualTo("EN");
        assertThat(usado.getDivisa()).isEqualTo("MXN");
        assertThat(usado.getTipoPago()).isEqualTo("SPEI");
        assertThat(usado.getReferenciaCanal()).isEqualTo("REF-001");
        assertThat(usado.getCuentaAbono()).isEqualTo("1234567890");
        assertThat(usado.getCuentaCargo()).isEqualTo("9876543210");
        assertThat(usado.getTransactionId()).isEqualTo(99L);
        assertThat(usado.getRangoFechas()).isNotNull();
        assertThat(usado.getRangoFechas().getFechaInicio()).isNotNull();
        assertThat(usado.getRangoFechas().getFechaFin()).isNotNull();
    }

    @Test
    void sumaImportePorDivisaDetalle_ok() {
        var lista = List.of(new ImporteTotalDivisaDTO("MXN", BigDecimal.TEN));

        when(pagoRepository.sumaImportePorDivisaDetalle(any(PagoDetalleFiltroDTO.class)))
                .thenReturn(lista);

        var out = pagoService.sumaImportePorDivisaDetalle(buildRequestValido());

        assertThat(out).hasSize(1);
        assertThat(out.get(0).divisa()).isEqualTo("MXN");
        assertThat(out.get(0).importeTotal()).isEqualTo(BigDecimal.TEN);

        ArgumentCaptor<PagoDetalleFiltroDTO> cap = ArgumentCaptor.forClass(PagoDetalleFiltroDTO.class);
        verify(pagoRepository).sumaImportePorDivisaDetalle(cap.capture());
        var usado = cap.getValue();

        assertThat(usado.getOperacion()).isEqualTo("EN");
        assertThat(usado.getDivisa()).isEqualTo("MXN");
        assertThat(usado.getTransactionId()).isEqualTo(99L);
    }

    @Test
    void obtenerDetalleConTotales_ok() {
        var pageMock = new PageImpl<PagoDetalleDTO>(Collections.emptyList(), pageable(), 0);
        var totales = List.of(new ImporteTotalDivisaDTO("MXN", BigDecimal.ONE));

        when(pagoRepository.detallePorFiltros(any(PagoDetalleFiltroDTO.class), any(Pageable.class)))
                .thenReturn(pageMock);
        when(pagoRepository.sumaImportePorDivisaDetalle(any(PagoDetalleFiltroDTO.class)))
                .thenReturn(totales);

        var resp = pagoService.obtenerDetalleConTotales(buildRequestValido(), pageable());

        assertThat(resp.getContent().getTotalElements()).isZero();
        assertThat(resp.getTotalesPorDivisa()).hasSize(1);
        assertThat(resp.getTotalesPorDivisa().get(0).divisa()).isEqualTo("MXN");

        ArgumentCaptor<PagoDetalleFiltroDTO> cap1 = ArgumentCaptor.forClass(PagoDetalleFiltroDTO.class);
        verify(pagoRepository).detallePorFiltros(cap1.capture(), any(Pageable.class));
        ArgumentCaptor<PagoDetalleFiltroDTO> cap2 = ArgumentCaptor.forClass(PagoDetalleFiltroDTO.class);
        verify(pagoRepository).sumaImportePorDivisaDetalle(cap2.capture());

        assertThat(cap1.getValue())
                .usingRecursiveComparison()
                .isEqualTo(cap2.getValue());

    }
}
