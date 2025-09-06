package mx.santander.monitoreoapi.repository;

import java.util.List;
import mx.santander.monitoreoapi.constants.QueryConstants;
import mx.santander.monitoreoapi.model.entity.CanalEntity;
import mx.santander.monitoreoapi.model.entity.CanalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de catálogos de canales.
 * <p>Expone consultas de apoyo para obtener operaciones y tipos de operación.</p>
 */
@Repository
public interface ICanalRepository extends JpaRepository<CanalEntity, CanalId> {

    /**
     * Obtiene la lista de operaciones disponibles.
     * @return identificadores/nombres de operación
     */
    @Query(QueryConstants.OBTENER_OPERACIONES)
    List<String> obtenerOperaciones();

    /**
     * Obtiene los tipos de operación configurados.
     * @return lista de tipos de operación
     */
    @Query(QueryConstants.OBTENER_TIPOS_OPERACION)
    List<String> obtenerTiposOperacion();
}
