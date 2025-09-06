package mx.santander.monitoreoapi.controller;

import lombok.RequiredArgsConstructor;
import mx.santander.monitoreoapi.model.response.CatalogosResponse;
import mx.santander.monitoreoapi.service.ICatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// Si usan Swagger/OpenAPI:



/**
 * Controlador REST para exponer catálogos de apoyo (divisas, productos, estatus, etc.).
 * Orquesta la llamada a la capa de servicio y retorna respuestas normalizadas para el front.
 *
 * @author Rodrigo RPM
 * @since 1.0
 */
@RestController
@RequestMapping("/api/catalogos")
@RequiredArgsConstructor
// @Tag(name = "Catálogos", description = "Catálogos para inicializar filtros del front")
public class CatalogoController {

    /** Servicio de catálogos (lógica y acceso a datos). */
    private final ICatalogoService catalogoService;

    /**
     * Devuelve en una sola llamada los catálogos necesarios para inicializar los filtros del front.
     *
     * @return estructura consolidada de catálogos (divisas, tipos de operación, estatus, tipos de pago, productos)
     */
    @GetMapping(value = "/filtros", produces = "application/json")
    // @Operation(summary = "Catálogos para filtros", description = "Consolida todos los catálogos requeridos por el front")
    public ResponseEntity<CatalogosResponse> obtenerCatalogosFiltros() {
        return ResponseEntity.ok(catalogoService.obtenerCatalogosFiltros());
    }
}
