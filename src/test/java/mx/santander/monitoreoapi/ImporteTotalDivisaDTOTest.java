package mx.santander.monitoreoapi;

import mx.santander.monitoreoapi.model.response.ImporteTotalDivisaDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para {@link ImporteTotalDivisaDTO}.
 * Objetivo:
 * - Validar que los accesores generados por el record funcionan correctamente.
 * - Confirmar que los valores asignados en el constructor se mantienen.
 */
class ImporteTotalDivisaDTOTest {

    /**
     * Caso de prueba: creación de un record y validación de accesores.
     * Escenario:
     * - Se instancia un record con valores de divisa e importe.
     * - Se verifica que los accesores devuelven los valores esperados.
     */
    @Test
    void record_accesors() {
        // Construcción del record con datos de prueba
        var dto = new ImporteTotalDivisaDTO("USD", BigDecimal.TEN);

        // Verificación de valores
        assertThat(dto.divisa()).isEqualTo("USD");
        assertThat(dto.importeTotal()).isEqualTo(BigDecimal.TEN);
    }
}
