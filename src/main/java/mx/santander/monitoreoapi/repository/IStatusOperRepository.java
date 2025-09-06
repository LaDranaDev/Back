package mx.santander.monitoreoapi.repository;

import java.util.List;
import mx.santander.monitoreoapi.constants.QueryConstants;
import mx.santander.monitoreoapi.model.entity.StatusOperEntity;
import mx.santander.monitoreoapi.model.response.OpcionCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para el catálogo de estatus de operación.
 */
@Repository
public interface IStatusOperRepository extends JpaRepository<StatusOperEntity, String> {

    /**
     * Obtiene todos los estatus de operación proyectados como opciones de catálogo
     * (par id / descripción) para poblar selects o combos del frontend.
     *
     * @return lista (posiblemente vacía) de {@link OpcionCatalogo} con el id y la descripción
     *         del estatus; nunca {@code null}.
     */
    @Query(QueryConstants.STATUS_OPER_OPCION_CATALOGO)
    List<OpcionCatalogo> findAllAsOpcion();
}
