package mx.santander.h2h.monitoreoapi.repository;

import java.util.List;
import mx.santander.h2h.monitoreoapi.constants.QueryConstants;
import mx.santander.h2h.monitoreoapi.model.entity.OperPagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para operaciones de pago.
 * Expone consultas específicas de apoyo para filtros y catálogos.
 */
@Repository
public interface IOperPagoRepository extends JpaRepository<OperPagoEntity, Long> {

    /**
     * Obtiene las divisas distintas presentes en la tabla de operaciones.
     *
     * @return lista de códigos de divisa únicos (ej. MXN, USD)
     */
    @Query(QueryConstants.OBTENER_DIVISAS_OPER)
    List<String> obtenerDivisas();
}
