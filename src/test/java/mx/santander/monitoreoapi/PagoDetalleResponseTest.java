package mx.santander.monitoreoapi;

import mx.santander.monitoreoapi.model.response.PagoDetalleDTO;
import mx.santander.monitoreoapi.model.response.PagoDetalleResponse;
import mx.santander.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para {@link PagoDetalleResponse}.
 * Objetivo:
 * - Validar que los métodos de acceso (getters) funcionan correctamente.
 * - Confirmar que los valores asignados en el constructor se mantienen.
 */
class PagoDetalleResponseTest {

    /**
     * Caso de prueba: validación de getters.
     * Escenario:
     * - Se construye un {@link PagoDetalleResponse} con una página vacía de resultados
     *   y una lista con un total por divisa.
     * - Se verifica que la página reporta cero elementos.
     * - Se valida que el total por divisa contiene el valor esperado.
     */
    @Test
    void getters_work() {
        // Construcción de una página vacía
        Page<PagoDetalleDTO> page = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        // Totales de prueba
        var totales = List.of(new ImporteTotalDivisaDTO("MXN", BigDecimal.ONE));

        // Creación del objeto de respuesta
        var resp = new PagoDetalleResponse(page, totales);

        // Verificaciones
        assertThat(resp.getContent().getTotalElements()).isZero();
        assertThat(resp.getTotalesPorDivisa().get(0).divisa()).isEqualTo("MXN");
    }
}
