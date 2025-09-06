package mx.santander.monitoreoapi.model.request;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * DTO de petición para filtros de consulta de pagos.
 * Agrupa campos en objetos de valor y expone JSON plano mediante @JsonUnwrapped.
 */
@Data
public class PagoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Código de producto H2H (B2B, SPEI, SPID...). Opcional. */
    private String operacion;

    /** Divisa de transacción (MXN, USD...). Opcional. */
    private String divisa;

    /** Tipo de pago (SPEI, SPID, INTERNACIONAL, B2B). Opcional. */
    private String tipoPago;

    /** Estatus de la operación (PE, PR, EN, RE, RA). Opcional. */
    private String estatus;

    /** Referencia del canal. Opcional. */
    private String referenciaCanal;

    /** Importe exacto. Opcional. */
    private BigDecimal importe;

    /** Tipo de operación (alto nivel). Opcional. */
    private String tipoOperacion;

    /** Rango de fechas del envío al canal. */
    @JsonUnwrapped
    private RangoFechas rangoFechas;

    /** Cuentas involucradas. */
    @JsonUnwrapped
    private Cuentas cuentas;

    /** Identificadores/canal. */
    @JsonUnwrapped
    private Identificadores identificadores;

    /** Rango de fechas (JSON: fechaInicio, fechaFin). */
    @Data
    public static class RangoFechas implements Serializable {
        private static final long serialVersionUID = 1L;
        /** Fecha de inicio del rango (inclusive). */
        private java.time.LocalDate fechaInicio;
        /** Fecha de fin del rango (inclusive). */
        private java.time.LocalDate fechaFin;
    }

    /** Cuentas (JSON: cuentaCargo, cuentaAbono). */
    @Data
    public static class Cuentas implements Serializable {
        private static final long serialVersionUID = 1L;
        /** Cuenta ordenante. */
        private String cuentaCargo;
        /** Cuenta receptora. */
        private String cuentaAbono;
    }

    /** Identificadores (JSON: canal, transactionId). */
    @Data
    public static class Identificadores implements Serializable {
        private static final long serialVersionUID = 1L;
        /** Canal de operación. */
        private String canal;
        /** Referencia única de la operación. */
        private String transactionId;
    }
}
