package mx.santander.monitoreoapi;

import mx.santander.monitoreoapi.controller.CatalogoController;
import mx.santander.monitoreoapi.model.response.CatalogosResponse;
import mx.santander.monitoreoapi.service.ICatalogoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas unitarias para {@link CatalogoController}.
 * Objetivo:
 * - Validar que los endpoints de catálogos respondan correctamente.
 * - Verificar la interacción entre el controlador y el servicio.
 */
class CatalogoControllerTest {

    /** Utilidad para simular llamadas HTTP al controlador */
    private MockMvc mvc;

    /** Servicio de catálogos simulado con Mockito */
    private ICatalogoService catalogoService;

    /**
     * Configuración inicial antes de cada prueba:
     * - Se crea un mock del servicio.
     * - Se construye el controlador usando MockMvc en modo standalone.
     */
    @BeforeEach
    void setup() {
        catalogoService = mock(ICatalogoService.class);
        CatalogoController controller = new CatalogoController(catalogoService);

        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * Caso de prueba: obtener filtros de catálogo exitosamente.
     * Escenario:
     * - Se simula que el servicio devuelve un objeto válido.
     * - Se invoca el endpoint GET /api/catalogos/filtros.
     * - Se espera un HTTP 200 OK.
     * - Se verifica que el servicio haya sido llamado exactamente una vez.
     */
    @Test
    void filtros_ok() throws Exception {
        // Configuración del mock: devolvemos una respuesta simulada
        when(catalogoService.obtenerCatalogosFiltros())
                .thenReturn(mock(CatalogosResponse.class));

        // Ejecución: llamamos al endpoint GET
        mvc.perform(get("/api/catalogos/filtros"))
                // Verificación: debe responder con status 200
                .andExpect(status().isOk());

        // Verificación: el servicio fue invocado una vez
        verify(catalogoService).obtenerCatalogosFiltros();
    }
}
