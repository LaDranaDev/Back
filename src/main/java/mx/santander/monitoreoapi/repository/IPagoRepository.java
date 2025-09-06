package mx.santander.monitoreoapi.repository;

import java.time.LocalDateTime;
import java.util.List;

import mx.santander.monitoreoapi.constants.QueryConstants;
import mx.santander.monitoreoapi.model.entity.OperPagoEntity;
import mx.santander.monitoreoapi.model.request.PagoDetalleFiltroDTO;
import mx.santander.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import mx.santander.monitoreoapi.model.response.MontoTotalDivisaDTO;
import mx.santander.monitoreoapi.model.response.PagoDetalleDTO;
import mx.santander.monitoreoapi.model.response.ResumenEstatusGlobalDTO;
import mx.santander.monitoreoapi.model.response.ResumenProductoDivisaEstatusDTO;
import mx.santander.monitoreoapi.model.response.ResumenProductoEstatusDTO;
import mx.santander.monitoreoapi.model.response.TotalGlobalOperacionesDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para consultas de resumen y detalle de operaciones de pago.
 * <p>Las consultas se definen en {@link QueryConstants} por mantenerlas centralizadas.</p>
 */
@Repository
public interface IPagoRepository extends JpaRepository<OperPagoEntity, Long> {

    /**
     * Resumen de operaciones agrupado por producto y estatus.
     *
     * @param divisa      filtro opcional por divisa (ej. MXN, USD)
     * @param fechaInicio inicio del rango (inclusive)
     * @param fechaFin    fin del rango (inclusive)
     * @return lista con totales por combinación producto/estatus
     */
    @Query(QueryConstants.RESUMEN_PRODUCTO_ESTATUS)
    List<ResumenProductoEstatusDTO> resumenPorProductoYEstatus(
            @Param("divisa") String divisa,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    /**
     * Conteo de pagos por divisa dentro del rango indicado.
     *
     * @param fechaInicio inicio del rango (inclusive)
     * @param fechaFin    fin del rango (inclusive)
     * @return lista de tuplas [divisa, conteo] (Object[] -> [String, Long])
     */
    @Query(QueryConstants.CONTEO_PAGOS_POR_DIVISA)
    List<Object[]> conteoPagosPorDivisa(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin")   LocalDateTime fechaFin
    );

    /**
     * Totales globales (conteo y montos) para el dashboard.
     *
     * @param divisa      filtro opcional por divisa
     * @param producto    filtro opcional por producto/operación
     * @param fechaInicio inicio del rango
     * @param fechaFin    fin del rango
     * @return totales globales de operaciones
     */
    @Query(QueryConstants.TOTALES_GLOBALES)
    TotalGlobalOperacionesDTO obtenerTotalesGlobales(
            @Param("divisa") String divisa,
            @Param("producto") String producto,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    /**
     * Resumen por producto, divisa y estatus.
     *
     * @param divisa         filtro opcional por divisa
     * @param tipoPago       filtro opcional por tipo de pago
     * @param tipoOperacion  filtro opcional por tipo de operación
     * @param fechaInicio    inicio del rango
     * @param fechaFin       fin del rango
     * @return lista con totales por combinación producto/divisa/estatus
     */
    @Query(QueryConstants.RESUMEN_PRODUCTO_DIVISA_ESTATUS)
    List<ResumenProductoDivisaEstatusDTO> resumenPorProductoDivisaEstatus(
            @Param("divisa") String divisa,
            @Param("tipoPago") String tipoPago,
            @Param("tipoOperacion") String tipoOperacion,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    /**
     * Resumen global por estatus de operación.
     *
     * @param divisa      filtro opcional por divisa
     * @param fechaInicio inicio del rango
     * @param fechaFin    fin del rango
     * @return totales por estatus (recibido, procesado, rechazado, etc.)
     */
    @Query(QueryConstants.RESUMEN_POR_ESTATUS)
    List<ResumenEstatusGlobalDTO> resumenPorEstatus(
            @Param("divisa") String divisa,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    /**
     * Montos totales por divisa.
     *
     * @param fechaInicio inicio del rango
     * @param fechaFin    fin del rango
     * @return lista con montos agregados por divisa
     */
    @Query(QueryConstants.MONTO_TOTAL_POR_DIVISA)
    List<MontoTotalDivisaDTO> montoTotalPorDivisa(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin")   LocalDateTime fechaFin
    );

    /**
     * Detalle paginado aplicando los filtros dinámicos.
     *
     * @param filtro   DTO con los filtros (operación, cuentas, fechas, etc.)
     * @param pageable parámetros de paginación y ordenamiento
     * @return página con filas del detalle
     */
    @Query(QueryConstants.DETALLE_POR_FILTROS)
    Page<PagoDetalleDTO> detallePorFiltros(@Param("filtro") PagoDetalleFiltroDTO filtro, Pageable pageable);

    /**
     * Totales de importe por divisa para el mismo conjunto filtrado del detalle.
     *
     * @param filtro DTO con los mismos filtros del detalle
     * @return lista de importes totales por divisa
     */
    @Query(QueryConstants.SUMA_IMPORTE_POR_DIVISA_DETALLE)
    List<ImporteTotalDivisaDTO> sumaImportePorDivisaDetalle(@Param("filtro") PagoDetalleFiltroDTO filtro);
}
