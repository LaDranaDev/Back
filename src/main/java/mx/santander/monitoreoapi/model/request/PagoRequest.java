package mx.santander.monitoreoapi.model.request;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDate;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO de petición para filtros de consulta de pagos.
 * <p>
 * Incluye validaciones de entrada (Bean Validation) para prevenir datos inválidos
 * y reduce exposición de datos sensibles en logs con toString() controlado.
 * </p>
 */
@Data
@ToString(onlyExplicitlyIncluded = true)
public class PagoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Código de producto H2H (B2B, SPEI, SPID...). Opcional, alfanumérico corto */
    @Pattern(regexp = "^[A-Z0-9_]{0,10}$", message = "Operación inválida")
    @ToString.Include(name = "operacion")
    private String operacion;

    /** Divisa de transacción (MXN, USD...). Opcional, exactamente 3 letras */
    @Pattern(regexp = "^[A-Z]{3}$", message = "Divisa inválida")
    @ToString.Include(name = "divisa")
    private String divisa;

    /** Tipo de pago (SPEI, SPID, INTERNACIONAL, B2B). Opcional */
    @Size(max = 20, message = "Tipo de pago demasiado largo")
    @ToString.Include(name = "tipoPago")
    private String tipoPago;

    /** Estatus de la operación (PE, PR, EN, RE, RA). Opcional */
    @Pattern(regexp = "^[A-Z]{2}$", message = "Estatus inválido")
    private String estatus;

    /** Referencia del canal. Opcional, hasta 30 caracteres */
    @Size(max = 30, message = "Referencia de canal demasiado larga")
    @ToString.Include(name = "referenciaCanal")
    private String referenciaCanal;

    /** Importe exacto. Debe ser número >= 0 con hasta 2 decimales */
    @Digits(integer = 15, fraction = 2, message = "Importe inválido")
    @PositiveOrZero
    @ToString.Include(name = "importe")
    private BigDecimal importe;

    /** Tipo de operación (alto nivel). Opcional */
    @Size(max = 20, message = "Tipo de operación demasiado largo")
    @ToString.Include(name = "tipoOperacion")
    private String tipoOperacion;

    /** Rango de fechas del envío al canal (LocalDate para mantener contrato estable). */
    @JsonUnwrapped
    private RangoFechas rangoFechas;

    /** Cuentas involucradas (cargo/abono). */
    @JsonUnwrapped
    @Valid
    private Cuentas cuentas;

    /** Identificadores/canal. */
    @JsonUnwrapped
    @Valid
    private Identificadores identificadores;

    // -------- Clases internas con validación ligera --------

    /** Rango de fechas (JSON: fechaInicio, fechaFin) en formato yyyy-MM-dd */
    @Data
    public static class RangoFechas implements Serializable {
        private static final long serialVersionUID = 1L;
        private LocalDate fechaInicio;
        private LocalDate fechaFin;
    }

    @Data
    public static class Cuentas implements Serializable {
        private static final long serialVersionUID = 1L;
        @Pattern(regexp = "^\\d{10,18}$", message = "Cuenta cargo inválida")
        private String cuentaCargo;
        @Pattern(regexp = "^\\d{10,18}$", message = "Cuenta abono inválida")
        private String cuentaAbono;

        @Override public String toString() {
            return "Cuentas(cuentaCargo=" + mask(cuentaCargo) + ", cuentaAbono=" + mask(cuentaAbono) + ")";
        }
        private static String mask(String s){ return (s == null || s.length()<4) ? "****" : "****"+s.substring(s.length()-4); }
    }

    @Data
    public static class Identificadores implements Serializable {
        private static final long serialVersionUID = 1L;
        @Size(max = 20, message = "Canal inválido")
        private String canal;
        @Pattern(regexp = "^\\d{0,18}$", message = "TransactionId inválido")
        private String transactionId;

        @Override public String toString() {
            return "Identificadores(canal=" + safe(canal) + ", transactionId=" + maskTxn(transactionId) + ")";
        }
        private static String safe(String s){ return s == null ? null : s.replaceAll("[\\r\\n]", ""); }
        private static String maskTxn(String s){ return (s == null || s.length()<3) ? "***" : "***"+s.substring(s.length()-3); }
    }
}