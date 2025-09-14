package mx.santander.h2h.monitoreoapi.model.response;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO inmutable para el detalle de operaciones.
 * <p>Se utiliza como proyección en JPQL:</p>
 * {@code select new response.model.mx.santander.h2h.monitoreoapi.PagoDetalleDTO(...)}.
 */

/*cumplimos
* la
* regla
* de
* sonar*/
public record PagoDetalleDTO(
        String operacion,
        String cuentaAbono,
        BigDecimal importe,
        String referenciaCanal,
        String divisa,
        LocalDateTime fechaCarga,
        String cuentaCargo,
        String tipoPago,
        String estatus,
        Integer transactionId
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Devuelve la cuenta de abono enmascarada para logging o UI. */
    public String cuentaAbonoMasked() {
        return mask(cuentaAbono);
    }

    /** Devuelve la cuenta de cargo enmascarada para logging o UI. */
    public String cuentaCargoMasked() {
        return mask(cuentaCargo);
    }

    /** Devuelve el transactionId enmascarado (últimos 3 dígitos). */
    public String transactionIdMasked() {
        if (transactionId == null) return "***";
        String s = transactionId.toString();
        return s.length() > 3 ? "***" + s.substring(s.length() - 3) : "***";
    }

    private static String mask(String cuenta) {
        if (cuenta == null || cuenta.length() < 4) return "****";
        return "****" + cuenta.substring(cuenta.length() - 4);
    }
}
