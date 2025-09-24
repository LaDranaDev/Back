package mx.santander.h2h.monitoreoapi;

import mx.santander.h2h.monitoreoapi.model.response.ErrorResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para {@link ErrorResponse}.
 * Objetivo:
 * - Validar que el constructor de la clase asigna correctamente
 *   los valores a todos los campos.
 * - Verificar que los getters devuelven la información esperada.
 */
class ErrorResponseTest {

    /**
     * Caso de prueba: creación de un ErrorResponse y validación de getters.
     * Escenario:
     * - Se instancia un objeto con datos simulados.
     * - Se verifica que cada getter devuelve el valor asignado.
     */
    @Test
    void constructor_y_getters() {
        // Construcción del objeto con datos de prueba
        ErrorResponse e = new ErrorResponse(
                "400",
                "Filtro inválido",
                "ERROR",
                "Rango de fechas"
        );

        // Verificaciones de los campos
        assertThat(e.getCode()).isEqualTo("400");
        assertThat(e.getMessage()).contains("inválido");
        assertThat(e.getLevel()).isEqualTo("ERROR");
        assertThat(e.getDescription()).isEqualTo("Rango de fechas");
    }
}
