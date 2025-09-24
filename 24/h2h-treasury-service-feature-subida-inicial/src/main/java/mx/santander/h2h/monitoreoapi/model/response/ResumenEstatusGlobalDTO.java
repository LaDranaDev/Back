package mx.santander.h2h.monitoreoapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

/**
 * DTO que representa el total de operaciones por estatus global.
 *
 * <p>Ejemplo de uso: mostrar el número de operaciones recibidas, procesadas o rechazadas
 * en la parte superior del dashboard.</p>
 *
 * @author Rodrigo
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class ResumenEstatusGlobalDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String estatus;    // Ej: "Recibido"
    private long total;        // Ej: 10
}
