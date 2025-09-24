package mx.santander.h2h.monitoreoapi.model.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Resumen de operaciones por producto y estatus.
 * <p>Ej.: producto = "B2B", estatus = "Recibido", total = 5.</p>
 */
@Data
@AllArgsConstructor
public class ResumenProductoEstatusDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Nombre del producto (ej. "B2B Traspaso entre cuentas"). */
    private String producto;

    /** Estatus de la operación (ej. "Recibido", "Rechazado"). */
    private String estatus;

    /** Total de operaciones para esa combinación producto/estatus. */
    private long total;
}
