package mx.santander.monitoreoapi.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Filtros de búsqueda del detalle de pagos.
 * <p>
 * Se modela con 10 campos para cumplir la regla de Sonar (máx. 10),
 * agrupando fechas en el objeto anidado {@link RangoFechas}.
 * Las propiedades deben coincidir con los nombres usados en JPQL
 * (SpEL: :#{#filtro.*}) para evitar errores en el mapeo.
 * </p>
 */
@Data
@NoArgsConstructor
public class PagoDetalleFiltroDTO implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    /** 1
     * 2
     * 3
     * 4
     * 5
     * 6
     * 7*/

    private String operacion;
    private String cuentaAbono;
    private BigDecimal importe;
    private String referenciaCanal;
    private String divisa;

    /** Rango de fechas del envío/carga en BD. */
    private RangoFechas rangoFechas = new RangoFechas(null, null); // siempre no-null

    private String cuentaCargo;
    private String tipoPago;
    private String estatus;
    private Long transactionId;

    /** Contenedor serializable para las fechas. */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RangoFechas implements Serializable {
        @Serial private static final long serialVersionUID = 1L;
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFin;
    }
}