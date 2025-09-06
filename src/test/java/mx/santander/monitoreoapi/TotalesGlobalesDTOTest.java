package mx.santander.monitoreoapi;

import mx.santander.monitoreoapi.model.response.TotalesGlobalesDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para {@link TotalesGlobalesDTO}.
 * Objetivo:
 * - Validar que los getters devuelven los valores correctos.
 * - Comprobar el funcionamiento de los métodos generados por Lombok
 *   (toString, equals y hashCode).
 */
class TotalesGlobalesDTOTest {

    /**
     * Caso de prueba: validación de getters y métodos Lombok.
     * Escenario:
     * - Se construye un objeto con datos de prueba.
     * - Se verifica que los getters devuelven los valores esperados.
     * - Se valida que toString, equals y hashCode se comportan correctamente.
     */
    @Test
    void getters_y_toString() {
        // Construcción del DTO con valores de prueba
        var dto = new TotalesGlobalesDTO(5L, new BigDecimal("12.34"), Map.of("MXN", 3L));

        // Verificación de getters
        assertThat(dto.getTotalPagos()).isEqualTo(5L);
        assertThat(dto.getTotalMontos()).isEqualByComparingTo("12.34");
        assertThat(dto.getPagosPorDivisa()).containsEntry("MXN", 3L);

        // Verificación de métodos generados por Lombok
        assertThat(dto.toString()).contains("totalPagos=5");
        assertThat(dto).isEqualTo(new TotalesGlobalesDTO(5L, new BigDecimal("12.34"), Map.of("MXN", 3L)));
        assertThat(dto.hashCode()).isNotZero();
    }
}
