package mx.santander.monitoreoapi.model.response;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Respuesta consolidada de catálogos para inicializar filtros del frontend.
 * <p>Incluye tipos de operación, divisas, tipos de pago y estatus.</p>
 */
@Getter
@Setter
@AllArgsConstructor
public class CatalogosResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Catálogo de tipos de operación. */
    private List<OpcionCatalogo> tiposOperacion;

    /** Catálogo de divisas disponibles. */
    private List<OpcionCatalogo> divisas;

    /** Catálogo de tipos de pago. */
    private List<OpcionCatalogo> tiposPago;

    /** Catálogo de estatus de las operaciones. */
    private List<OpcionCatalogo> estatus;
}
