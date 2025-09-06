package mx.santander.monitoreoapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

/**
 * DTO que representa un resumen de operaciones agrupadas por producto y divisa,
 * incluyendo el número total de operaciones y su desglose por estatus.
 *
 * <p>Este DTO se utiliza principalmente en las consultas de resumen
 * de operaciones mostradas en el dashboard de monitoreo.</p>
 *
 * @author Rodrigo
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class ProductoDivisaResumenDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String operacion;          // TXT_TIPO_PRODUCT
    private String divisa;             // TXT_DIV_ORDEN
    private long totalOperaciones;
    private long recibido;
    private long rechazado;
    private long correcto;
    private long enProceso;
    private long procesado;
}
