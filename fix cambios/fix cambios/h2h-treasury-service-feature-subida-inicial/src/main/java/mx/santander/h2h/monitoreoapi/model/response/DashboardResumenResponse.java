package mx.santander.h2h.monitoreoapi.model.response;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Respuesta del dashboard con KPIs globales y desgloce por estatus/producto/divisa.
 */
@Data
@AllArgsConstructor
public class DashboardResumenResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Totales globales (conteo y montos agregados). */
    private TotalesGlobalesDTO totales;

    /** Resumen por estatus de operación. */
    private List<ResumenEstatusGlobalDTO> resumenPorEstatus;

    /** Resumen por producto, divisa y estatus. */
    private List<ResumenProductoDivisaEstatusDTO> resumenPorProductoDivisaEstatus;

    /** Montos totales por divisa. */
    private List<MontoTotalDivisaDTO> montoTotalPorDivisa;
}
