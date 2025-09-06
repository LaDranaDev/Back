// src/test/java/mx/santander/monitoreoapi/service/PagoServiceBranchesTest.java
package mx.santander.monitoreoapi.service;

import mx.santander.monitoreoapi.builder.PagoFiltroBuilder;
import mx.santander.monitoreoapi.model.request.PagoRequest;
import mx.santander.monitoreoapi.model.response.DashboardResumenResponse;
import mx.santander.monitoreoapi.model.response.TotalGlobalOperacionesDTO;
import mx.santander.monitoreoapi.repository.IPagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link PagoService}.
 * Objetivo:
 * - Validar ramas del  {@code obtenerDashboardResumen}.
 * - Asegurar que los valores nulos en consultas del repositorio se manejan correctamente.
 * - Confirmar la construcción del objeto {@link DashboardResumenResponse}
 *   con distintos escenarios de datos.
 */
class PagoServiceBranchesTest {

    /** Repositorio mockeado */
    IPagoRepository repo;

    /** Builder mockeado para filtros de pagos */
    PagoFiltroBuilder filtroBuilder;

    /** Servicio bajo prueba */
    PagoService service;

    /**
     * Configuración antes de cada prueba:
     * - Se inicializan mocks para repositorio y filtroBuilder.
     * - Se construye la instancia del servicio con dichos mocks.
     */
    @BeforeEach
    void setup() {
        repo = mock(IPagoRepository.class);
        filtroBuilder = mock(PagoFiltroBuilder.class);
        service = new PagoService(repo, filtroBuilder);
    }

    /**
     * Caso de prueba: totales globales nulos.
     * Escenario:
     * - El repositorio devuelve {@code null} en {@code obtenerTotalesGlobales}.
     * - La consulta de pagos por divisa devuelve lista vacía.
     * - Se espera que la respuesta tenga totales inicializados en cero.
     */
    @Test
    void dashboardResumen_tgNull() {
        var filtro = new PagoRequest();

        when(repo.obtenerTotalesGlobales(any(), any(), any(), any()))
                .thenReturn(null);
        when(repo.conteoPagosPorDivisa(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        DashboardResumenResponse r = service.obtenerDashboardResumen(filtro);

        assertThat(r).isNotNull();
        assertThat(r.getTotales()).isNotNull();
        assertThat(r.getTotales().getTotalPagos()).isZero();
        assertThat(r.getTotales().getTotalMontos()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(r.getTotales().getPagosPorDivisa()).isEmpty();
    }

    /**
     * Caso de prueba: monto total nulo.
     * Escenario:
     * - El repositorio devuelve un objeto con totalPagos válido y totalMontos en {@code null}.
     * - Se devuelven registros de conteo por divisa.
     * - Se espera que totalMontos se convierta a cero en la respuesta.
     */
    @Test
    void dashboardResumen_totalMontosNull() {
        var filtro = new PagoRequest();

        when(repo.obtenerTotalesGlobales(any(), any(), any(), any()))
                .thenReturn(new TotalGlobalOperacionesDTO(7L, null));
        when(repo.conteoPagosPorDivisa(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.<Object[]>of(new Object[] { "MXN", 3L }));

        DashboardResumenResponse r = service.obtenerDashboardResumen(filtro);

        assertThat(r.getTotales().getTotalPagos()).isEqualTo(7L);
        assertThat(r.getTotales().getTotalMontos()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(r.getTotales().getPagosPorDivisa()).containsEntry("MXN", 3L);
    }

    /**
     * Caso de prueba: totales completos.
     * Escenario:
     * - El repositorio devuelve valores válidos de totalPagos y totalMontos.
     * - Se devuelven registros de conteo por divisa con varias entradas.
     * - Se espera que la respuesta refleje los datos proporcionados.
     */
    @Test
    void dashboardResumen_ok() {
        var filtro = new PagoRequest();

        when(repo.obtenerTotalesGlobales(any(), any(), any(), any()))
                .thenReturn(new TotalGlobalOperacionesDTO(10L, new BigDecimal("123.45")));
        when(repo.conteoPagosPorDivisa(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(
                        new Object[] { "MXN", 3L },
                        new Object[] { "USD", 2L }
                ));

        DashboardResumenResponse r = service.obtenerDashboardResumen(filtro);

        assertThat(r.getTotales().getTotalPagos()).isEqualTo(10L);
        assertThat(r.getTotales().getTotalMontos()).isEqualByComparingTo("123.45");
        assertThat(r.getTotales().getPagosPorDivisa()).isEqualTo(Map.of("MXN", 3L, "USD", 2L));
    }
}
