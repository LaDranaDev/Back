package mx.santander.monitoreoapi.model.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Totales globales para el dashboard: conteo de pagos, monto agregado y
 * distribución de pagos por divisa.
 */
@Data
@AllArgsConstructor
public class TotalesGlobalesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Número total de pagos en el rango consultado. */
    private long totalPagos;

    /** Suma de montos de todas las operaciones. */
    private BigDecimal totalMontos;

    /** Mapa {divisa -> número de pagos} para la gráfica por divisa. */
    private Map<String, Long> pagosPorDivisa;
}
