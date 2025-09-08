package mx.santander.monitoreoapi.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import mx.santander.monitoreoapi.model.request.PagoRequest;
import mx.santander.monitoreoapi.model.response.DashboardResumenResponse;
import mx.santander.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import mx.santander.monitoreoapi.model.response.PagoDetalleResponse;
import mx.santander.monitoreoapi.service.IPagoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Totales de importes agrupados por divisa aplicando los mismos filtros del detalle.
 *
 * author : RRPM
 * Version : 1.0
 * Proyecto :monitore de apis
 * fecha 9 
 * de 
 * septiembre 2025
 */
import static mx.santander.monitoreoapi.constants.QueryConstants.DEFAULT_PAGE_SIZE;
import static mx.santander.monitoreoapi.constants.QueryConstants.MAX_PAGE_SIZE;

/**
 * Controlador REST 
 * para consulta
 *  de operaciones
 *   de pago 
 *   (resumen,
 *    detalle y
 *     totales).
 * Usa POST para aceptar filtros
 *  complejos en el
 *   cuerpo del request.
 *
 * @author Rodrigo RPM
 * @since 1.0
 */
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    /** Lógica de negocio de operaciones de pago. */
    private final IPagoService pagoService;

    /**
     * Resumen global de operaciones agrupado por estatus (para el dashboard).
     *
     * @param filtro criterios opcionales (fechas, divisa, etc.)
     * @return datos consolidados para tarjetas y gráficas del dashboard
     */
    @PostMapping(
            value = "/resumen",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DashboardResumenResponse> obtenerResumenDashboard(
            @RequestBody @Validated PagoRequest filtro
    ) {
        return ResponseEntity.ok(pagoService.obtenerDashboardResumen(filtro));
    }

    /**
     * Detalle paginado de operaciones con filtros dinámicos.
     * Limita el tamaño de página para evitar consultas excesivas.
     *
     * @param filtro    criterios de filtrado (operación, cuentas, fechas, etc.)
     * @param pageable  parámetros de paginación y ordenamiento
     * @return página solicitada de resultados más totales calculados
     */
    @PostMapping(
            value = "/detalle",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )/**
     * Totales de importes agrupados por divisa aplicando los mismos filtros del detalle.
     *Agregamos el 25%
     *de
     *densidad 
     *de
     *comentarios
     *en el 
     *agregado
     *del codigo
     */
    public ResponseEntity<PagoDetalleResponse> obtenerDetallePaginado(
            @RequestBody @Validated PagoRequest filtro,
            Pageable pageable
    ) {
        int requestedSize = pageable.getPageSize();
        if (requestedSize <= 0) {
            requestedSize = DEFAULT_PAGE_SIZE; // tamaño por defecto cuando no viene
        }
/**
 *Totales
 *de
 *importes
 *agrupados
 * por
 * divisa
 * aplicando
 * los mismos
 * filtros
 * del
 *detalle.
 */
        int safeSize = Math.min(requestedSize, MAX_PAGE_SIZE);

        int safePage = pageable.getPageNumber();
        if (safePage < 0) {
            safePage = 0; // nunca paginar negativo
        }
/**
 * Totales
 * de 
 * importes
 * agrupados
 * por divisa
 * aplicando
 * los mismos 
 * filtros del 
 * detalle.
 */
        Pageable safePageable = PageRequest.of(safePage, safeSize, pageable.getSort());
        return ResponseEntity.ok(pagoService.obtenerDetalleConTotales(filtro, safePageable));

    }


    /**
     * Totales de importes agrupados por divisa aplicando los mismos filtros del detalle.
     *
     * @param filtro criterios de filtrado
     * @return lista de totales por divisa
     */
    @PostMapping(
            value = "/detalle/totales",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ImporteTotalDivisaDTO>> obtenerTotalesPorDivisa(
            @RequestBody @Validated PagoRequest filtro
    ) {
        return ResponseEntity.ok(pagoService.sumaImportePorDivisaDetalle(filtro));
    }
}
