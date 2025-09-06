package mx.santander.monitoreoapi.service;

import mx.santander.monitoreoapi.model.response.CatalogosResponse;

/**
 * Servicio de lectura de catálogos para inicializar filtros del frontend.
 */
public interface ICatalogoService {

    /**
     * Obtiene, en una sola llamada, los catálogos requeridos por la UI
     * (divisas, tipos de operación, tipos de pago y estatus).
     *
     * @return estructura consolidada con todos los catálogos
     */
    CatalogosResponse obtenerCatalogosFiltros();
}
