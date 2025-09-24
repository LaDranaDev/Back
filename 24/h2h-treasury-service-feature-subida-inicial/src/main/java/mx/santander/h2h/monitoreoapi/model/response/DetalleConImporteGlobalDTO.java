package mx.santander.h2h.monitoreoapi.model.response;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * Contenedor serializable que combina el detalle paginado de operaciones
 * con metadatos de paginación y los totales de importe por divisa.
 * <p>
 * Se evita exponer {@link Page} directamente para mantener un contrato JSON
 * explícito y cumplir reglas de serialización/calidad.
 * </p>
 *
 * @param <T> tipo de los elementos del detalle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleConImporteGlobalDTO<T extends Serializable> implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    /** Filas de la página solicitada. */
    private List<T> content;

    /** Información de paginación asociada a {@link #content}. */
    private PageInfo page;

    /** Totales de importes agregados por divisa para los mismos filtros. */
    private List<ImporteTotalDivisaDTO> importesTotales;

    /**
     * Crea una instancia del wrapper a partir de una página JPA y de la lista de totales calculados.
     *
     * @param page    página de resultados ya consultada (contenido + metadatos).
     * @param totales totales de importes agrupados por divisa que corresponden a la misma consulta.
     * @param <T>     tipo de elemento contenido en la página.
     * @return DTO listo para serializar como respuesta REST.
     */
    public static <T extends Serializable> DetalleConImporteGlobalDTO<T> from(
            Page<T> page,
            List<ImporteTotalDivisaDTO> totales
    ) {
        return new DetalleConImporteGlobalDTO<>(
                page.getContent(),
                new PageInfo(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isLast()
                ),
                totales
        );
    }

    /**
     * Componente embebido <>.
     * <p>1</p>
     * <p>Mapea
     *  1:1
     *   a
     *    columnas
     *     existentes;
     *      no
     *       cambia
     *        la 
     *        tabla.</p>
     */


    /**
     * Metadatos
     *  serializables
     *   de
     *    la
     *     paginación
     *      que 
     *      describen
     *       la
     *        página
     *         devuelta en {@link #content}.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo implements Serializable {
        @Serial private static final long serialVersionUID = 1L;

        /** Índice de página (base 0). */
        private int pageNumber;
        /** Tamaño de página solicitado. */
        private int pageSize;
        /** Número total de elementos de la consulta. */
        private long totalElements;
        /** Número total de páginas. */
        private int totalPages;
        /** Indica si esta es la última página. */
        private boolean last;
    }
}
