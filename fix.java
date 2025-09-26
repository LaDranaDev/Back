package mx.santander.h2h.monitoreoapi.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.santander.h2h.monitoreoapi.builder.PagoFiltroBuilder;
import mx.santander.h2h.monitoreoapi.model.request.PagoRequest;
import mx.santander.h2h.monitoreoapi.model.response.DashboardResumenResponse;
import mx.santander.h2h.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import mx.santander.h2h.monitoreoapi.model.response.PagoDetalleDTO;
import mx.santander.h2h.monitoreoapi.model.response.PagoDetalleResponse;
import mx.santander.h2h.monitoreoapi.model.response.TotalesGlobalesDTO;
import mx.santander.h2h.monitoreoapi.repository.IPagoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Servicio de consultas de pagos (dashboard, detalle y totales).
 * Se extrajo la construcción de filtros a {@link PagoFiltroBuilder} para
 * reducir dependencias y duplicación, y se simplificó la lógica para
 * bajar complejidad cognitiva. */
@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService implements IPagoService {

    private static final String DIVISA_SIN_VALOR = "SIN_DIVISA";

    private final IPagoRepository pagoRepository;
    private final PagoFiltroBuilder filtroBuilder;

    /** Resumen para el dashboard (totales, montos, estatus y product/divisa/estatus). */
    @Override
    public DashboardResumenResponse obtenerDashboardResumen(PagoRequest filtro) {
        log.info("Consultando dashboard resumen con filtro: {}", filtro);

        // Fechas "hoy"
        var hoy = java.time.LocalDate.now();
        var fechaInicio = hoy.atStartOfDay();
        var fechaFin = hoy.atTime(java.time.LocalTime.MAX);

        // Totales globales
        var tg = pagoRepository.obtenerTotalesGlobales(
                filtro.getDivisa(), filtro.getOperacion(), fechaInicio, fechaFin);

        // >>> Reemplazo de ternarios por if/else (2 usos)
        long totalPagos;
        java.math.BigDecimal totalMontos;

        if (tg == null) {
            totalPagos = 0L;
            totalMontos = java.math.BigDecimal.ZERO;
        } else {
            totalPagos = tg.totalOperaciones();

            java.math.BigDecimal tm = tg.totalMontos();
            if (tm == null) {
                totalMontos = java.math.BigDecimal.ZERO;
            } else {
                totalMontos = tm;
            }
        }
        /*
         * Proyecto:
         *  Monitoreo API
         * Archivo:
         *  PagoController.java
         * Descripción:
         *  Comentarios
         *   añadidos
         *    para documentar 
         *    el propósito y
         *     funcionamiento del componente.
         * Autor:
         *  rrpm
         * Versión: 
         * 1.0
         * Fecha: 
         * 2025-09-02
         */

        // Conteo por divisa -> Map<String, Long>
        Map<String, Long> pagosPorDivisa = pagoRepository
                .conteoPagosPorDivisa(fechaInicio, fechaFin)
                .stream()
                .map(r -> Map.entry(normalizarDivisa(r[0]), ((Number) r[1]).longValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Long::sum,
                        LinkedHashMap::new));

        var totalesDashboard = new TotalesGlobalesDTO(totalPagos, totalMontos, pagosPorDivisa);

        var montoTotalPorDivisa = pagoRepository.montoTotalPorDivisa(fechaInicio, fechaFin);
        var resumenPorEstatus = pagoRepository.resumenPorEstatus(filtro.getDivisa(), fechaInicio, fechaFin);
        var resumenPorProductoDivisaEstatus = pagoRepository.resumenPorProductoDivisaEstatus(
                filtro.getDivisa(), filtro.getTipoPago(), filtro.getTipoOperacion(), fechaInicio, fechaFin);

        return new DashboardResumenResponse(
                totalesDashboard, resumenPorEstatus, resumenPorProductoDivisaEstatus, montoTotalPorDivisa);
    }

    /** Detalle paginado según filtros. */
    @Override
    public Page<PagoDetalleDTO> obtenerDetalle(PagoRequest filtro, Pageable pageable) {
        log.info("Consultando detalle con filtro: {}", filtro);
        var fd = filtroBuilder.build(filtro);
        return pagoRepository.detallePorFiltros(fd, pageable);
    }

    /** Totales por divisa aplicando los mismos filtros del detalle. */
    @Override
    public java.util.List<ImporteTotalDivisaDTO> sumaImportePorDivisaDetalle(PagoRequest filtro) {
        var fd = filtroBuilder.build(filtro);
        return pagoRepository.sumaImportePorDivisaDetalle(fd);
    }

    /** Wrapper con detalle y totales. */
    @Override
    public PagoDetalleResponse obtenerDetalleConTotales(PagoRequest filtro, Pageable pageable) {
        var page = obtenerDetalle(filtro, pageable); // 'var' para reducir ruido/imports
        var totales = sumaImportePorDivisaDetalle(filtro);
        return new PagoDetalleResponse(page, totales);
    }

    private static String normalizarDivisa(Object rawValue) {
        if (rawValue == null) {
            return DIVISA_SIN_VALOR;
        }
        String divisa = rawValue.toString().trim();
        return divisa.isEmpty() ? DIVISA_SIN_VALOR : divisa;
    }
}




----
// src/test/java/mx/santander/monitoreoapi/service/PagoServiceBranchesTest.java
package mx.santander.h2h.monitoreoapi.service;

import mx.santander.h2h.monitoreoapi.builder.PagoFiltroBuilder;
import mx.santander.h2h.monitoreoapi.model.request.PagoRequest;
import mx.santander.h2h.monitoreoapi.model.response.DashboardResumenResponse;
import mx.santander.h2h.monitoreoapi.model.response.TotalGlobalOperacionesDTO;
import mx.santander.h2h.monitoreoapi.repository.IPagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
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

    /**
     * Caso de prueba: divisa nula en el resultado del repositorio.
     * Se espera que se normalice a la clave "SIN_DIVISA" para evitar errores de serialización.
     */
    @Test
    void dashboardResumen_divisaNull() {
        var filtro = new PagoRequest();

        when(repo.obtenerTotalesGlobales(any(), any(), any(), any()))
                .thenReturn(new TotalGlobalOperacionesDTO(5L, new BigDecimal("10")));
        when(repo.conteoPagosPorDivisa(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(
                        new Object[] { null, 2L },
                        new Object[] { "MXN", 3L },
                        new Object[] { " ", 1L }));

        DashboardResumenResponse r = service.obtenerDashboardResumen(filtro);

        assertThat(r.getTotales().getPagosPorDivisa())
                .contains(entry("MXN", 3L), entry("SIN_DIVISA", 3L));
    }
}

