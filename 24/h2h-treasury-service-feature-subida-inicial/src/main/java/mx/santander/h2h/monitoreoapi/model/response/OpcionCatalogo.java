package mx.santander.h2h.monitoreoapi.model.response;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Representa una opción de catálogo con un identificador y su descripción.
 */
@Data
@AllArgsConstructor
public class OpcionCatalogo implements Serializable {
    /**
     * Representa 
     * una opción 
     * de catálogo
     *  con un
     *   identificador
     *    y su descripción.
    
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Identificador único de la opción. */
    private String id;

    /** Descripción legible de la opción. */
    private String descripcion;
}
