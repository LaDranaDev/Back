package mx.santander.monitoreoapi.service;

import java.util.List;
import mx.santander.monitoreoapi.model.request.PagoRequest;
import mx.santander.monitoreoapi.model.response.DashboardResumenResponse;
import mx.santander.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import mx.santander.monitoreoapi.model.response.PagoDetalleDTO;
import mx.santander.monitoreoapi.model.response.PagoDetalleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio de consultas de pagos para el dashboard y el detalle.
 */
public interface IPagoService {

    /**
     * Genera el resumen global (totales y desgloses) según los filtros.
     *
     * @param filtro criterios opcionales de filtrado
     * @return resumen con totales globales y desgloses por estatus/divisa/producto
     */
    DashboardResumenResponse obtenerDashboardResumen(PagoRequest filtro);

    /**
     * Obtiene el detalle paginado de operaciones según los filtros.
     *
     * @param filtro   criterios opcionales de filtrado
     * @param pageable parámetros de paginación y ordenamiento
     * @return página con filas de detalle
     */
    Page<PagoDetalleDTO> obtenerDetalle(PagoRequest filtro, Pageable pageable);

    /**
     * Suma de importes por divisa para el conjunto filtrado del detalle.
     *
     * @param filtro criterios opcionales de filtrado
     * @return lista de importes totales por divisa
     */
    List<ImporteTotalDivisaDTO> sumaImportePorDivisaDetalle(PagoRequest filtro);

    /**
     * Wrapper que combina el detalle paginado con los totales por divisa.
     *
     * @param filtro   criterios de filtrado
     * @param pageable parámetros de paginación
     * @return respuesta con página de detalle y totales por divisa
     */
    PagoDetalleResponse obtenerDetalleConTotales(PagoRequest filtro, Pageable pageable);
}
