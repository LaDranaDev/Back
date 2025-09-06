package mx.santander.monitoreoapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

/**
 * DTO que representa un resumen de operaciones agrupadas por producto, divisa y estatus.
 *
 * <p>Incluye el número total de operaciones para cada combinación, utilizado en la
 * sección de detalle del dashboard y reportes exportados.</p>
 *
 * @author Rodrigo
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class ResumenProductoDivisaEstatusDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String operacion; // Concatenado
    private String divisa;
    private String estatus;
    private long total;
}
