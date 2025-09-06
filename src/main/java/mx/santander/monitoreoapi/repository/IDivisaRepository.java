package mx.santander.monitoreoapi.repository;

import java.util.List;
import mx.santander.monitoreoapi.constants.QueryConstants;
import mx.santander.monitoreoapi.model.entity.DivisaEntity;
import mx.santander.monitoreoapi.model.response.OpcionCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad de divisas.
 * Provee consultas de apoyo para catálogos del frontend.
 */
@Repository
public interface IDivisaRepository extends JpaRepository<DivisaEntity, String> {

    /**
     * Obtiene el catálogo de divisas con los campos mínimos requeridos por la UI.
     *
     * @return lista de opciones de catálogo (id/descripcion) para combos del front
     */
    @Query(QueryConstants.DIVISAS_OPCION_CATALOGO)
    List<OpcionCatalogo> findAllAsOpcion();
}
/**
 * Repositorio
 * catálogos del
 * frontend.
 */