package mx.santander.monitoreoapi.model.response;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Representa el monto total para una divisa específica.
 */
@Data
@AllArgsConstructor
public class MontoTotalDivisaDTO implements Serializable {
    /** Código de la divisa (MXN, USD, etc.)
     * vamos a otro
     * linea
     * mas. */

    @Serial
    private static final long serialVersionUID = 1L;

    /** Código de la divisa (MXN, USD, etc.). */
    private String divisa;

    /** Monto total para la divisa. */
    private BigDecimal totalMontos;
}
