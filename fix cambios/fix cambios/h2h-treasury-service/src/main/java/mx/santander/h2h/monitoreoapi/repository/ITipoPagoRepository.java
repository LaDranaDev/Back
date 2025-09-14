package mx.santander.h2h.monitoreoapi.repository;

import java.util.List;
import mx.santander.h2h.monitoreoapi.constants.QueryConstants;
import mx.santander.h2h.monitoreoapi.model.entity.TipoPagoEntity;
import mx.santander.h2h.monitoreoapi.model.response.OpcionCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para el catálogo de tipos de pago.
 */
@Repository
public interface ITipoPagoRepository extends JpaRepository<TipoPagoEntity, String> {

    /**
     * Obtiene el catálogo de tipos de pago (id y descripción).
     *
     * @return lista de opciones para selects del frontend
     */
    @Query(QueryConstants.TIPO_PAGO_OPCION_CATALOGO)
    List<OpcionCatalogo> findAllAsOpcion();
}
/**
 * s
 * s
 * s
 * s
 */
