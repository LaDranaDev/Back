package mx.santander.h2h.monitoreoapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Wrapper
 *  para 
 *  la 
 *  respuesta
 *   paginada
 *    de
 *     detalle
 *      de 
 *      operaciones
 * junto
 *  con
 *   los
 *    importes
 *     totales 
 *     agrupados 
 *     por divisa.
 */

/*agregamos un comentario mas*/

@Getter
@Setter
@AllArgsConstructor
public class PagoDetalleResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Página de resultados con el detalle de operaciones (no serializable). */
    private transient Page<PagoDetalleDTO> content;

    /** Lista de importes totales por divisa, coherentes con el detalle. */
    private List<ImporteTotalDivisaDTO> totalesPorDivisa;
}



