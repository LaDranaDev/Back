package mx.santander.monitoreoapi.service;
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
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.santander.monitoreoapi.builder.PagoFiltroBuilder;
import mx.santander.monitoreoapi.model.request.PagoRequest;
import mx.santander.monitoreoapi.model.response.DashboardResumenResponse;
import mx.santander.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import mx.santander.monitoreoapi.model.response.PagoDetalleDTO;
import mx.santander.monitoreoapi.model.response.PagoDetalleResponse;
import mx.santander.monitoreoapi.model.response.TotalesGlobalesDTO;
import mx.santander.monitoreoapi.repository.IPagoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Servicio 
 * de consultas de pagos
 *  (dashboard, detalle y totales).
 * Se
 *  extrajo 
 *  la construcción de 
 *  filtros a {@link PagoFiltroBuilder} para
 * reducir
 *  dependencias y
 *   duplicación, y 
 *   se simplificó la
 *    lógica para
 * bajar complejidad
 *  cognitiva.

 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService implements IPagoService {

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
        //

        // Conteo por divisa -> Map<String, Long>
        Map<String, Long> pagosPorDivisa = pagoRepository
                .conteoPagosPorDivisa(fechaInicio, fechaFin)
                .stream()
                .collect(Collectors.toMap(r -> (String) r[0], r -> ((Number) r[1]).longValue()));

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
}
