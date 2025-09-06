package mx.santander.monitoreoapi.repository;

import java.util.List;
import mx.santander.monitoreoapi.constants.QueryConstants;
import mx.santander.monitoreoapi.model.entity.TipoOperacionEntity;
import mx.santander.monitoreoapi.model.response.OpcionCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para el catálogo de tipos de operación.
 */
@Repository
public interface ITipoOperacionRepository extends JpaRepository<TipoOperacionEntity, String> {

    /**
     * Obtiene el catálogo de tipos de operación (id y descripción).
     *
     * @return lista de opciones para inicializar filtros del front
     */
    @Query(QueryConstants.TIPO_OPERACION_OPCION_CATALOGO)
    List<OpcionCatalogo> findAllAsOpcion();
    /**
     * si
     * si
     * si
     * si
     */
}
